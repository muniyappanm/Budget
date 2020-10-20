package com.example.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity
{
    EditText email;
    EditText password;
    Button btnRegister;
    String mail,pass;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Register");
        setContentView(R.layout.activity_register);
        email = (EditText)findViewById(R.id.editText_email);
        password = (EditText)findViewById(R.id.editText_password);
        btnRegister = (Button)findViewById(R.id.button_register);
        auth=FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail=email.getText().toString();
                pass=password.getText().toString();
                if(mail.isEmpty()||pass.isEmpty())
                    Toast.makeText(Register.this,"Empty Credentials",Toast.LENGTH_LONG).show();
                else if(pass.length()<4)
                    Toast.makeText(Register.this,"Password too short",Toast.LENGTH_LONG).show();
                else
                    RegisterUser(mail,pass);
            }
        });
    }

    private void RegisterUser(String mail, String pass)
    {
        auth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(Register.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful()) {
                    Toast.makeText(Register.this, "Register Successful", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(Register.this, MainActivity.class);
                    startActivity(in);

                }
                else
                    Toast.makeText(Register.this, "Register Failed... Try Again and make sure internet connected", Toast.LENGTH_LONG).show();

            }
        });
    }
}
