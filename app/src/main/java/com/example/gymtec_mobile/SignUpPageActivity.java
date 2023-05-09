package com.example.gymtec_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class SignUpPageActivity extends AppCompatActivity {
    private SQLiteManager sqLiteManager;

    private DatePickerDialog datePickerDialog;
    private String currentSQLDate;
    private Client NewUser;
    private Button back_button;
    private Button signup_button;
    private EditText edit_fname;
    private EditText edit_flast;
    private EditText edit_slast;
    private EditText edit_provincia;
    private EditText edit_canton;
    private EditText edit_distrito;
    private Button edit_birthdate;
    private EditText edit_weight;
    private EditText edit_IMC;
    private EditText edit_email;
    private EditText edit_ID;
    private EditText edit_password;
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page_activity);
        sqLiteManager = SQLiteManager.instanceOfDatabase(this);

        initDatePicker();

        back_button = (Button) findViewById(R.id.signup_back_button);
        signup_button = (Button) findViewById(R.id.signup_signup_button);
        edit_fname = (EditText) findViewById(R.id.signup_editTextFName);
        edit_flast = (EditText) findViewById(R.id.signup_editTextFLast);
        edit_slast = (EditText) findViewById(R.id.signup_editTextSLast);
        edit_provincia = (EditText) findViewById(R.id.signup_editTextProvincia);
        edit_canton = (EditText) findViewById(R.id.signup_editTextCanton);
        edit_distrito = (EditText) findViewById(R.id.signup_editTextDistrito);
        edit_birthdate = (Button) findViewById(R.id.signup_buttonDate);
        edit_weight = (EditText) findViewById(R.id.signup_editTextWeight);
        edit_IMC = (EditText) findViewById(R.id.signup_editTextIMC);
        edit_email = (EditText) findViewById(R.id.signup_editTextEmail);
        edit_ID = (EditText) findViewById(R.id.signup_editTextID);
        edit_password = (EditText) findViewById(R.id.signup_editTextPassword);

        edit_birthdate.setText(getTodaysDate());

        edit_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPageActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ClassService.classArrayList.clear();
                //sqLiteManager.initialize();
                sqLiteManager.getClasses();
                if (verifyInformation()){
                    NewUser = new Client(
                            edit_fname.getText().toString(),
                            edit_flast.getText().toString(),
                            edit_slast.getText().toString(),
                            edit_provincia.getText().toString(),
                            edit_canton.getText().toString(),
                            edit_distrito.getText().toString(),
                            edit_birthdate.getText().toString(),
                            Integer.parseInt(edit_weight.getText().toString()),
                            Integer.parseInt(edit_IMC.getText().toString()),
                            edit_email.getText().toString(),
                            Integer.parseInt(edit_ID.getText().toString()),
                            edit_password.getText().toString()
                            );
                    sqLiteManager.addClient(NewUser);
                    Client.ID_client = NewUser.getID();
                    Intent intent = new Intent(SignUpPageActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i("SIGNUP TEST", "DOESNT MEET THE REQUIREMENTS");
                }
            }
        });
    }

    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        currentSQLDate = makeDateSQL(day, month, year);
        return makeDateString(day, month, year);

    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                currentSQLDate = makeDateSQL(day, month, year);
                Log.i("DATE TEST","SETTING DATE CORRECTLY");
                edit_birthdate.setText(date);

            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
    private void openDatePicker(View view){
        datePickerDialog.show();
    }

    private String makeDateString(int day, int month, int year) {
        return day+" "+getMonthFormat(month)+" "+year;
    }
    private String makeDateSQL(int day, int month, int year) {
        return month+"/"+day+"/"+year;
    }

    private String getMonthFormat(int month){
        switch (month){
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "JAN";
        }
    }

    private boolean verifyValidEmail(String email){
        String ePattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern p = java.util.regex.Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }
    private boolean verifyValidID(int ID){

        if(ID < 100000000){
            return false;
        } else {
            return true;
        }
    }
    private boolean verifyInformation(){
        boolean status = true;
        if (verifyNotNulls()){
            AlertDialog.Builder errorMessage = new AlertDialog.Builder(this);
            errorMessage.setCancelable(true);
            if (!sqLiteManager.verifyIDAvailability(Integer.parseInt(edit_ID.getText().toString()))){
                Log.e("SIGNUP TEST", "FAILED AT ALREADY EXISTING ID");
                errorMessage.setMessage("The client is already registered.");
                status = false;
            } else if (!verifyValidID(Integer.parseInt(edit_ID.getText().toString()))){
                Log.e("SIGNUP TEST", "FAILED AT VALID ID");
                errorMessage.setMessage("Enter a valid ID for the client.");
                status = false;
            } else if (!verifyValidEmail(edit_email.getText().toString())){
                Log.e("SIGNUP TEST", "FAILED AT VALID EMAIL");
                errorMessage.setMessage("Enter a valid email for the client.");
                status = false;
            } if (!status){
                AlertDialog errorAlert = errorMessage.create();
                errorAlert.show();
            }
        } else {
            status = false;
            Log.i("SIGNUP TEST", "FAILED AT NULLS");
        }
        return status;
    }
    private boolean verifyNotNulls(){
        boolean status = true;
        AlertDialog.Builder errorMessage = new AlertDialog.Builder(this);
        errorMessage.setCancelable(true);
        if(edit_fname.getText().toString().equals("")){
            Log.e("SIGNUP TEST", "FAILED AT NAME NULL");
            errorMessage.setMessage("Name Box has to be filled.");
            status = false;
        }else if(edit_flast.getText().toString().equals("")){
            Log.e("SIGNUP TEST", "FAILED AT LAST NAME NULL");
            errorMessage.setMessage("First Last Name Box has to be filled.");
            status = false;
        }else if(edit_provincia.getText().toString().equals("")){
            Log.e("SIGNUP TEST", "FAILED AT PROVINCIA NULL");
            errorMessage.setMessage("Provincia Box has to be filled.");
            status = false;
        }else if(edit_canton.getText().toString().equals("")){
            Log.e("SIGNUP TEST", "FAILED AT CANTON NULL");
            errorMessage.setMessage("Canton Box has to be filled.");
            status = false;
        }else if(edit_distrito.getText().toString().equals("")){
            Log.e("SIGNUP TEST", "FAILED AT DISTRITO NULL");
            errorMessage.setMessage("Distrito Box has to be filled.");
            status = false;
        }else if(currentSQLDate.equals("")){
            Log.e("SIGNUP TEST", "FAILED AT DATE NULL");
            errorMessage.setMessage("Date Box has to be selected.");
            status = false;
        }else if(edit_weight.getText().toString().equals("")){
            Log.e("SIGNUP TEST", "FAILED AT WEIGHT NULL");
            errorMessage.setMessage("Weight Box has to be filled.");
            status = false;
        }else if(edit_IMC.getText().toString().equals("")){
            Log.e("SIGNUP TEST", "FAILED AT IMC NULL");
            errorMessage.setMessage("IMC Box has to be filled.");
            status = false;
        }else if(edit_email.getText().toString().equals("")){
            Log.e("SIGNUP TEST", "FAILED AT EMAIL NULL");
            errorMessage.setMessage("Email Box has to be filled.");
            status = false;
        }else if(edit_ID.getText().toString().equals("")){
            Log.e("SIGNUP TEST", "FAILED AT ID NULL");
            errorMessage.setMessage("ID Box has to be filled.");
            status = false;
        }else if(edit_password.getText().toString().equals("")){
            Log.e("SIGNUP TEST", "FAILED AT PASSWORD NULL");
            errorMessage.setMessage("Password Box has to be filled.");
            status = false;
        }
        if (!status){
            AlertDialog errorAlert = errorMessage.create();
            errorAlert.show();
        }
        return status;
    }

    private void printValues(){
        Log.i("DATA NAME",edit_fname.getText().toString());
        Log.i("DATA 1 LAST NAME",edit_flast.getText().toString());
        Log.i("DATA 2 LAST NAME",edit_slast.getText().toString());
        Log.i("DATA PROVINCIA",edit_provincia.getText().toString());
        Log.i("DATA CANTON",edit_canton.getText().toString());
        Log.i("DATA DISTRITO",edit_distrito.getText().toString());
        Log.i("DATA DATE",currentSQLDate);
        Log.i("DATA WEIGHT",edit_weight.getText().toString());
        Log.i("DATA IMC",edit_IMC.getText().toString());
        Log.i("DATA EMAIL",edit_email.getText().toString());
        Log.i("DATA ID",edit_ID.getText().toString());
        Log.i("DATA PASSWORD",edit_password.getText().toString());
    }
}
