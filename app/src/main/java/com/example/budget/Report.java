package com.example.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Report extends AppCompatActivity {
    TextView from,to;
    TextView loading_graph,To,customreport;
    Button btn;
    DatePickerDialog datePickerDialog;
    Calendar e=Calendar.getInstance();
    int yyyy=e.get(Calendar.YEAR);
    int mm=e.get(Calendar.MONTH);
    int dd=e.get(Calendar.DAY_OF_MONTH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTitle("Report");
        String date_n = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        from=(TextView)findViewById(R.id.fromdate);
        to=(TextView)findViewById(R.id.todate);
        btn=(Button)findViewById(R.id.viewreport);
        loading_graph=(TextView)findViewById(R.id.loading_graph);
        To=(TextView)findViewById(R.id.to);
        customreport=(TextView)findViewById(R.id.textView);
        from.setText(date_n);
        to.setText(date_n);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(Report.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month += 1;
                                String DOM = "" + dayOfMonth;
                                if (DOM.length() == 1)
                                    DOM = "0" + DOM;
                                String M = "" + month;
                                if (M.length() == 1)
                                    M = "0" + M;
                                from.setText(DOM + "-" + M + "-" + year);
                                Log.d("Document:", "Month is:" + M);

                            }
                        }, yyyy, mm, dd);
                datePickerDialog.show();
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(Report.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month += 1;
                                String DOM = "" + dayOfMonth;
                                if (DOM.length() == 1)
                                    DOM = "0" + DOM;
                                String M = "" + month;
                                if (M.length() == 1)
                                    M = "0" + M;
                                to.setText(DOM + "-" + M + "-" + year);
                                Log.d("Document:", "Month is:" + M);

                            }
                        }, yyyy, mm, dd);
                datePickerDialog.show();
            }
        });
        Graph();
    }
    @Override
    protected void onStart() {
        super.onStart();
        btn.setVisibility(View.VISIBLE);
        from.setVisibility(View.VISIBLE);
        to.setVisibility(View.VISIBLE);
        To.setVisibility(View.VISIBLE);
        customreport.setVisibility(View.VISIBLE);
        loading_graph.setVisibility(View.INVISIBLE);
    }

    private void Graph() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            Intent in=new Intent(Report.this,ReportChart.class);
                            in.putExtra("from",from.getText().toString());
                            in.putExtra("to",to.getText().toString());
                            startActivity(in);
                            loading_graph.setVisibility(View.VISIBLE);
                            btn.setVisibility(View.INVISIBLE);
                            from.setVisibility(View.INVISIBLE);
                            to.setVisibility(View.INVISIBLE);
                            To.setVisibility(View.INVISIBLE);
                            customreport.setVisibility(View.INVISIBLE);

            }
        });
    }

}