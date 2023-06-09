package com.example.gymtec_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class LogInPageActivity  extends AppCompatActivity  {
    private SQLiteManager sqLiteManager;
    public Button back_button;
    public Button login_button;
    public EditText edit_ID;
    public EditText edit_password;
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_activity);

        sqLiteManager = SQLiteManager.instanceOfDatabase(this);

        back_button = (Button) findViewById(R.id.login_back_button);
        login_button = (Button) findViewById(R.id.login_button);
        edit_ID = (EditText) findViewById(R.id.login_editTextNumber);
        edit_password = (EditText) findViewById(R.id.login_editTextPassword);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInPageActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ClassService.classArrayList.clear();
                //sqLiteManager.initialize();
                sqLiteManager.getClasses();
                if (verifyInformation()){
                    Client.ID_client = Integer.parseInt(edit_ID.getText().toString());
                    Intent intent = new Intent(LogInPageActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private boolean verifyInformation(){
        boolean status = true;
        if (verifyNotNulls()){
            AlertDialog.Builder errorMessage = new AlertDialog.Builder(this);
            errorMessage.setCancelable(true);
            if(sqLiteManager.verifyIDAvailability(Integer.parseInt(edit_ID.getText().toString()))){
                Log.e("LOGIN TEST", "FAILED AT NON EXISTING ID");
                errorMessage.setMessage("The client doesn't exist.");
                status = false;
            } else {
                if (!sqLiteManager.verifyPassword(Integer.parseInt(edit_ID.getText().toString()), edit_password.getText().toString())){
                    Log.e("LOGIN TEST", "FAILED AT CHECKING PASSWORD");
                    errorMessage.setMessage("Incorrect password.");
                    status = false;
                }
            } if (!status){
                AlertDialog errorAlert = errorMessage.create();
                errorAlert.show();
            }
        } else {
            status = false;
            Log.i("LOGIN TEST", "FAILED AT NULLS");
        }
        return status;
    }

    private boolean verifyNotNulls(){
        boolean status = true;
        AlertDialog.Builder errorMessage = new AlertDialog.Builder(this);
        errorMessage.setCancelable(true);
        if(edit_ID.getText().toString().equals("")){
            Log.e("LOGIN TEST", "FAILED AT ID NULL");
            errorMessage.setMessage("ID Box has to be filled.");
            status = false;
        }else if(edit_password.getText().toString().equals("")){
            Log.e("LOGIN TEST", "FAILED AT PASSWORD NULL");
            errorMessage.setMessage("Password Box has to be filled.");
            status = false;
        } if (!status){
            AlertDialog errorAlert = errorMessage.create();
            errorAlert.show();
        }
        return status;
    }
}

