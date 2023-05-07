package com.example.gymtec_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;

import java.util.Calendar;
import java.util.regex.Pattern;


public class SignUpPageActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page_activity);

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

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPageActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Log.i("SIGNUP TEST", "MEETS ALL THE REQUIEREMENTS");
                } else {
                    Log.i("SIGNUP TEST", "DOESNT MEET THE REQUIEREMENTS");
                }
            }
        });
    }
    private boolean verifyInformation(){
        boolean status = true;
        if (verifyNotNulls()){
            if (!verifyIDNonExistance(Integer.parseInt(edit_ID.getText().toString()))){
                Log.e("SIGNUP TEST", "FAILED AT ALREADY EXISTING ID");
                status = false;
            }
            if (!verifyValidID(Integer.parseInt(edit_ID.getText().toString()))){
                Log.e("SIGNUP TEST", "FAILED AT VALID ID");
                status = false;
            }
            if (!verifyValidEmail(edit_ID.getText().toString())){
                Log.e("SIGNUP TEST", "FAILED AT VALID EMAIL");
                status = false;
            }
        } else {
            status = false;
            Log.i("SIGNUP TEST", "FAILED AT NULLS");
        }
        return status;
    }

    private boolean verifyIDNonExistance(int ID){

        return true;
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

    private boolean verifyValidEmail(String email){
        return Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*"
                        + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
                .matcher(email)
                .matches();
    }
    private boolean verifyValidID(int ID){

        if(ID < 100000000){
            return false;
        } else {
            return true;
        }
    }

    private boolean verifyNotNulls(){
        boolean status = true;
        if(edit_fname.getText().toString() == ""){
            Log.e("SIGNUP TEST", "FAILED AT NAME NULL");
            status = false;
        }else if(edit_flast.getText().toString() == ""){
            Log.e("SIGNUP TEST", "FAILED AT LAST NAME NULL");
            status = false;
        }else if(edit_provincia.getText().toString() == ""){
            Log.e("SIGNUP TEST", "FAILED AT PROVINCIA NULL");
            status = false;
        }else if(edit_canton.getText().toString() == ""){
            Log.e("SIGNUP TEST", "FAILED AT CANTON NULL");
            status = false;
        }else if(edit_distrito.getText().toString() == ""){
            Log.e("SIGNUP TEST", "FAILED AT DISTRITO NULL");
            status = false;
        }else if(currentSQLDate == ""){
            Log.e("SIGNUP TEST", "FAILED AT DATE NULL");
            status = false;
        }else if(edit_weight.getText().toString() == ""){
            Log.e("SIGNUP TEST", "FAILED AT WEIGHT NULL");
            status = false;
        }else if(edit_IMC.getText().toString() == ""){
            Log.e("SIGNUP TEST", "FAILED AT IMC NULL");
            status = false;
        }else if(edit_email.getText().toString() == ""){
            Log.e("SIGNUP TEST", "FAILED AT EMAIL NULL");
            status = false;
        }else if(edit_ID.getText().toString() == ""){
            Log.e("SIGNUP TEST", "FAILED AT ID NULL");
            status = false;
        }else if(edit_password.getText().toString() == ""){
            Log.e("SIGNUP TEST", "FAILED AT PASSWORD NULL");
            status = false;
        }
        return status;
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                currentSQLDate = makeDateSQL(day, month, year);
                back_button.setText(date);

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

    private String makeDateString(int day, int month, int year) {
        return day+" "+getMonthFormat(month)+" "+year;
    }
    private String makeDateSQL(int day, int month, int year) {
        return year+"-"+month+"-"+day;
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



    private void openDatePicker(View view){
        datePickerDialog.show();
    }
}
