package com.example.gymtec_mobile;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class SQLiteManager extends SQLiteOpenHelper
{
    //Instance of the SQLite DataBase
    private static SQLiteManager sqLiteManager;

    //Information of the DataBase
    private static final String DATABASE_NAME = "DataBase";
    private static final int DATABASE_VERSION = 1;

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public SQLiteManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context)
    {
        if(sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(SQLFiles.SQLCreationCliente);
        sqLiteDatabase.execSQL(SQLFiles.SQLCreationClase);
        sqLiteDatabase.execSQL(SQLFiles.SQLCreationServicios);
        sqLiteDatabase.execSQL(SQLFiles.SQLCreationClienteClase);
        sqLiteDatabase.execSQL(SQLFiles.SQLPopulation_1);
        sqLiteDatabase.execSQL(SQLFiles.SQLTest_1);
        sqLiteDatabase.execSQL(SQLFiles.SQLTest_2);
        sqLiteDatabase.execSQL(SQLFiles.SQLTest_3);
        Log.i("CREATION IS BACK","HONEWWWWWWY IM HOMEEEEE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
    }

    public void addClient(Client client)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Cedula", client.getID());
        contentValues.put("Nombre", client.getFname());
        contentValues.put("Apellido1", client.getFlast());
        contentValues.put("Apellido2", client.getSlast());
        contentValues.put("Provincia", client.getProvincia());
        contentValues.put("Canton", client.getCanton());
        contentValues.put("Distrito", client.getDistrito());
        contentValues.put("Email", client.getEmail());
        contentValues.put("Contrasena", client.getPassword());
        contentValues.put("FechaNacimiento", client.getBirthdate());
        contentValues.put("Peso", client.getWeight());
        contentValues.put("IMC", client.getIMC());

        sqLiteDatabase.insert("Cliente", null, contentValues);
    }

    public boolean isEnrrolled(int clientID, int classID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT IdClase FROM ClienteClase WHERE IdClase = "+ String.valueOf(classID) +" AND CedulaCliente = "+String.valueOf(clientID)+";", null))
        {
            if(result.getCount() != 0)
            {
                return false;
            } else {
                return true;
            }
        }
    }

    public void addClientClass(int clientID, int classID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IdClase ", classID);
        contentValues.put("CedulaCliente ", clientID);

        sqLiteDatabase.insert("ClienteClase", null, contentValues);
        updateClassCapacity(classID,-1);

    }

    public void removeClientClass(int clientID, int classID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete("ClienteClase", "CedulaCliente=? AND IdClase=?", new String[]{String.valueOf(clientID), String.valueOf(classID)});

        updateClassCapacity(classID,1);
    }


    public void getClasses()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ClassService.classArrayList.clear();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT Servicios.Nombre, Servicios.Descripcion, Clase.HoraInicio, Clase.HoraFinalizacion, Clase.Fecha, Clase.Capacidad, Clase.EsGrupal, Clase.NombreEmpleado, Clase.NombreSucursal, Clase.Id  FROM Clase INNER JOIN Servicios ON Clase.IdServicio = Servicios.Id;", null)) {
            if (result.getCount() > 0 && result.moveToFirst()) {
                do {
                    Log.i("RESULT", result.toString());
                    ClassService classService = new ClassService(
                            result.getString(0),
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getInt(5),
                            (result.getInt(6) == 1),
                            result.getString(7),
                            result.getString(8),
                            result.getInt(9)

                    );
                    ClassService.classArrayList.add(classService);
                } while (result.moveToNext());
                Log.i("ARRAY LIST", ClassService.classArrayList.toString());
            } else {
                Log.e("GET CLASS", "NO CLASSES FOUND");
            }
        }

    }

    public void updateClassCapacity(int classID, int deltaCapacity)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT Capacidad FROM Clase WHERE Id = "+ String.valueOf(classID) +";", null))
        {
            if(result.getCount() != 0)
            {
                result.moveToFirst();
                int capacity = result.getInt(0);
                Log.i("CAPACITY TEST", Integer.toString(capacity));
                capacity += deltaCapacity;

                ContentValues contentValues = new ContentValues();
                contentValues.put("Capacidad", capacity);

                sqLiteDatabase.update("Clase", contentValues, "Id=?", new String[]{String.valueOf(classID)});
            } else {
                Log.e("UPDATE CLASS CAPACITY","CLASS NOT FOUND");
            }
        }
    }

    public boolean verifyPassword(int clientID, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String right_password = "";

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT Contrasena FROM Cliente WHERE Cedula = "+ String.valueOf(clientID) +";", null))
        {
            if(result.moveToFirst()) // Move cursor to first row
            {
                right_password = result.getString(0);
            }
            else
            {
                Log.e("VERIFY PASSWORD","CLIENT NOT FOUND");
            }
        }
        catch (Exception e)
        {
            Log.e("VERIFY PASSWORD","An error occurred while verifying password: " + e.getMessage());
        }

        if(password.equals(right_password)){
            return true;
        } else {
            return false;
        }
    }

    public boolean verifyIDAvailability(int clientID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT Cedula FROM Cliente WHERE Cedula = "+ String.valueOf(clientID) +";", null))
        {
            if(result.getCount() != 0)
            {
                return false;
            } else {
                return true;
            }
        }
    }
}