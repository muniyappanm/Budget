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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
                    Toast.makeText(Register.this,"Empty Credentials",Toast.LENGTH_SHORT).show();
                else if(pass.length()<4)
                    Toast.makeText(Register.this,"Password too short",Toast.LENGTH_SHORT).show();
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
                    Map<String ,Object> data= new HashMap<>();
                    data.put("count",0);
                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                    db.collection("Budget").document(user.getUid()).collection("Counter")
                            .document("Counter").set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(Register.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(Register.this, MainActivity.class);
                                startActivity(in);
                            }
                        }
                    });
                }
                else
                    Toast.makeText(Register.this, "Register Failed... Try Again and make sure internet connected", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
