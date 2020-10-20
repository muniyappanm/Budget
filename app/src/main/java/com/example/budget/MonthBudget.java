package com.example.budget;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthBudget extends AppCompatActivity 
{
    DatabaseHandler myDb;
    EditText editName;
    Spinner editSurname;
    EditText editMarks;
    EditText editTextId;
    Button btnAddData;
    Button btnviewAll;
    Button btnDelete;
    Button btnviewUpdate;
    Button btnTotal;
    TextView txtToatl;
    DatePickerDialog datePickerDialog;
    Calendar c=Calendar.getInstance();
    int yyyy=c.get(Calendar.YEAR);
    int mm=c.get(Calendar.MONTH);
    int dd=c.get(Calendar.DAY_OF_MONTH);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Monthly Budget");
        setContentView(R.layout.activity_monthbudget);
        myDb = new DatabaseHandler(this);
        editName = (EditText)findViewById(R.id.editText_name);
        editName.setOnClickListener
                (new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v)
                    {
                        datePickerDialog=new DatePickerDialog(MonthBudget.this, new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                editName.setText(dayOfMonth+"-"+month+"-"+year);

                            }
                        }, yyyy,mm,dd);
                        datePickerDialog.show();
                    }
                });
        editSurname = (Spinner)findViewById(R.id.editText_surname);
        editMarks = (EditText)findViewById(R.id.editText_Marks);
        editTextId = (EditText)findViewById(R.id.editText_id);
        btnAddData = (Button)findViewById(R.id.button_add);
        btnviewAll = (Button)findViewById(R.id.button_viewAll);
        btnviewUpdate= (Button)findViewById(R.id.button_update);
        btnDelete= (Button)findViewById(R.id.button_delete);
        btnTotal= (Button)findViewById(R.id.button_total);
        txtToatl= (TextView)findViewById(R.id.textView_total);
        AddData();
        viewAll();
        UpdateData();
        DeleteData();
        Total();
    }

    private void Total() {
        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getSelectedData(editName.getText().toString(),
                        editSurname.getSelectedItem().toString());
                List<String> list = new ArrayList<String>();
                while (res.moveToNext()) {
                    list.add(res.getString(3));
                }
                int total=0;
                for(String x:list)
                    total+=Integer.parseInt(x);

                txtToatl.setText("Rs."+Integer.toString(total));
            }
        });
    }

    public void DeleteData() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteData(editTextId.getText().toString());
                        if(deletedRows > 0)
                            Toast.makeText(MonthBudget.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MonthBudget.this,"Data not Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public void UpdateData() {
        btnviewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = myDb.updateData(editTextId.getText().toString(),
                                editName.getText().toString(),
                                editSurname.getSelectedItem().toString(),editMarks.getText().toString());
                        if(isUpdate == true)
                            Toast.makeText(MonthBudget.this,"Data Update",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MonthBudget.this,"Data not Updated",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public  void AddData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted=false;
                        if(!editName.getText().toString().equals("")& !editSurname.getSelectedItem().toString().equals("")&
                                !editMarks.getText().toString().equals(""))
                        {
                            isInserted = myDb.insertData(editName.getText().toString(),
                                    editSurname.getSelectedItem().toString(),
                                    editMarks.getText().toString());

                        }
                        else Toast.makeText(MonthBudget.this,
                                "Enter Date,Item, Rate and then Press Create Button",Toast.LENGTH_LONG).show();
                        if(isInserted == true)
                            Toast.makeText(MonthBudget.this,"Data Inserted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void viewAll() {
        btnviewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        List<String> list = new ArrayList<String>();
                        while (res.moveToNext()) {
                            list.add(res.getString(0)+"__"+res.getString(1)+"__"+
                                    res.getString(2)+"__"+res.getString(3));

                        }
                        // Show all data
                        String[] items=list.toArray(new String[0]);
                        // Navigate to ViewPage with Parameter items String Array.
                        Intent in = new Intent(MonthBudget.this, ViewPage.class);
                        in.putExtra("Item",items);
                        startActivity(in);
                    }

                });
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

//public class MainActivity extends AppCompatActivity {
//    DatabaseHandler myDB;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//   