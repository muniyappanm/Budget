package com.example.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button btnLogin;
    Button btnRegister;
    String user, pass;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_main);
        email = (EditText)findViewById(R.id.editText_email);
        password = (EditText)findViewById(R.id.editText_password);
        btnLogin = (Button)findViewById(R.id.button_login);
        btnRegister = (Button)findViewById(R.id.button_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, Register.class);
                startActivity(in);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                user = email.getText().toString();
                pass = password.getText().toString();
                validate(user, pass);
                AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
            }
        });
    }


    public void validate(String user, String pass) {
        if (user.equals("") && pass.equals("")) {
            Intent in = new Intent(MainActivity.this, MonthBudget.class);
            startActivity(in);
        } else {
            AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
            build.setTitle("Error").setMessage("Wrong username or password. Please enter correct credentials.")
                    .setPositiveButton("ok", null).setCancelable(false);
            AlertDialog alert = build.create();
            alert.show();

        }
    }

    }