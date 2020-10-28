package com.example.budget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {
    Button expenditure,report;
    TextView loading;
    FireBaseHandler db=new FireBaseHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        expenditure=(Button)findViewById(R.id.button_expenditure);
        report=(Button)findViewById(R.id.button_report);
        loading=(TextView)findViewById(R.id.loading);
        Expenditure();
        Report();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_scrolling,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        switch (id){
            case R.id.logoutmenu:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MenuActivity.this,MainActivity.class));
                break;
            case R.id.additem_menu:
                startActivity(new Intent(MenuActivity.this,AddItem.class));
                break;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        expenditure.setVisibility(View.VISIBLE);
        report.setVisibility(View.VISIBLE);
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
                loading.setVisibility(View.VISIBLE);
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
