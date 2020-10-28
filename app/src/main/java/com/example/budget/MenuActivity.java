package com.example.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {
    Button expenditure,report,logout,add;
    TextView loading,additem;
    EditText item;
    FireBaseHandler db=new FireBaseHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        expenditure=(Button)findViewById(R.id.button_expenditure);
        report=(Button)findViewById(R.id.button_report);
        logout=(Button)findViewById(R.id.button_logout);
        loading=(TextView)findViewById(R.id.loading);
        additem=(TextView)findViewById(R.id.additem);
        add=(Button)findViewById(R.id.button_additem);
        item=(EditText)findViewById(R.id.add);
        Expenditure();
        Logout();
        Report();
        Additem();
    }
    private void Additem() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.AddItem(item.getText().toString());
                item.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        expenditure.setVisibility(View.VISIBLE);
        report.setVisibility(View.VISIBLE);
        logout.setVisibility(View.VISIBLE);
        additem.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        item.setVisibility(View.VISIBLE);
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
                additem.setVisibility(View.INVISIBLE);
                add.setVisibility(View.INVISIBLE);
                item.setVisibility(View.INVISIBLE);
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
