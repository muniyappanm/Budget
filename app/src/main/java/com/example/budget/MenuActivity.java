package com.example.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {
    Button expenditure,report,logout;
    TextView loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        expenditure=(Button)findViewById(R.id.button_expenditure);
        report=(Button)findViewById(R.id.button_report);
        logout=(Button)findViewById(R.id.button_logout);
        loading=(TextView)findViewById(R.id.loading);
        Expenditure();
        Logout();
        Report();
    }

    @Override
    protected void onStart() {
        super.onStart();
        expenditure.setVisibility(View.VISIBLE);
        report.setVisibility(View.VISIBLE);
        logout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.INVISIBLE);
    }

    private void Expenditure() {
        expenditure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MenuActivity.this, MonthBudget.class);
                startActivity(in);
                expenditure.setVisibility(View.INVISIBLE);
                report.setVisibility(View.INVISIBLE);
                logout.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
            }
        });
    }
    private void Logout() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MenuActivity.this,MainActivity.class));
            }
        });
    }
    private void Report() {
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,Report.class));
            }
        });
    }

}
