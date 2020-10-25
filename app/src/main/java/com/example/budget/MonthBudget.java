package com.example.budget;

import android.app.AlertDialog;
import android.app.AsyncNotedAppOp;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

public class MonthBudget extends AppCompatActivity 
{
    public ArrayList<ExampleItem> mExampleItem=new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton floatingActionButton;
    FireBaseHandler db=new FireBaseHandler();
    DatePickerDialog datePickerDialog;
    Calendar c=Calendar.getInstance();
    int yyyy=c.get(Calendar.YEAR);
    int mm=c.get(Calendar.MONTH);
    int dd=c.get(Calendar.DAY_OF_MONTH);
      TextView Date1;
    ImageView LeftDate,RightDate;
    TextView item,rate;
    EditText ItemText,RateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthbudget);
        Date1=findViewById(R.id.textView_date);
        floatingActionButton=findViewById(R.id.fab);
        item=(TextView) findViewById(R.id.textView_item);
        rate=(TextView) findViewById(R.id.textView_Rate);
        ItemText=(EditText)findViewById(R.id.editText_item);
        RateText=(EditText)findViewById(R.id.editText_rate);
        buildRecyclerView();
        String date_n = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        Date1.setText(date_n);
        PopUp();
        LeftDate=(ImageView)findViewById(R.id.leftdate);
        RightDate=(ImageView)findViewById(R.id.rightdate);
        onleftDate();
        onRightDate();
        onSetDate();
    }
// to initialise data from firestore.
    @Override
    protected void onStart() {
        super.onStart();
        View();
    }
    private void PopUp(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MonthBudget.this, Popup.class);
                startActivityForResult(in,1);
            }
        });

    }
    public void changeItem(int position, String text1,String text2) {
        mExampleItem.get(position).changeText1(text1,text2);
        mAdapter.notifyItemChanged(position);
    }
    public void buildRecyclerView() {
        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mAdapter=new ExampleAdapter(mExampleItem);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener()
        {
            @Override
            public void onItemDelete(int position, ImageView edit, ImageView delete, ImageView ok, EditText mTextView1,
                                     EditText mTextView2) {
                mExampleItem.remove(position);
                mAdapter.notifyItemRemoved(position);
                Task<QuerySnapshot> data=db.View(
                        Date1.getText().toString(),mTextView1.getText().toString()
                        ,mTextView2.getText().toString(),MonthBudget.this);
                if(data.getResult().isEmpty())
                {
                    //showMessage("Error","Nothing found");
                    return;
                }
                List<String> list = new ArrayList<String>();
                for (QueryDocumentSnapshot Qdoc:data.getResult())
                {
                    Map<String ,Object> Mlist= Qdoc.getData();
                    list.add(Mlist.get("count").toString());
                }
                for (String i:list
                     ) {
                    db.delete(i,MonthBudget.this);
                }
                View();

            }

            @Override
            public void onItemEdit(int position, ImageView edit,
                                   ImageView delete, ImageView ok, EditText mTextView1, EditText mTextView2) {
                edit.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
                ok.setVisibility(View.VISIBLE);
                mTextView1.setEnabled(true);
                mTextView2.setEnabled(true);

            }

            @Override
            public void onItemOk(int position, ImageView edit,
                                 ImageView delete, ImageView ok, EditText mTextView1, EditText mTextView2) {

                changeItem(position,mTextView1.getText().toString(),mTextView2.getText().toString());
                edit.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                ok.setVisibility(View.INVISIBLE);

            }
        });

    }
            public void onleftDate() {
             LeftDate.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                      mExampleItem.clear();
                     mRecyclerView.setLayoutManager(null);
                     String oldDate =Date1.getText().toString();
                     //Specifying date format that matches the given date
                     SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                     Calendar c = Calendar.getInstance();
                     try{
                         //Setting the date to the given date
                         c.setTime(sdf.parse(oldDate));
                     }catch(ParseException e){
                         e.printStackTrace();
                     }

                     c.add(Calendar.DAY_OF_MONTH, -1);
                     String newDate = sdf.format(c.getTime());
                     Date1.setText(newDate);
                     View();

                 }
             });

            }
            public void onRightDate() {
                mExampleItem.clear();
                mRecyclerView.setLayoutManager(null);
                RightDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oldDate =Date1.getText().toString();
                        //Specifying date format that matches the given date
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Calendar c = Calendar.getInstance();
                        try{
                            //Setting the date to the given date
                            c.setTime(sdf.parse(oldDate));
                        }catch(ParseException e){
                            e.printStackTrace();
                        }
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String newDate = sdf.format(c.getTime());
                        Date1.setText(newDate);
                        View();

                    }
                });

            }
            public void onSetDate() {
        Date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExampleItem.clear();
                mRecyclerView.setLayoutManager(null);
                datePickerDialog = new DatePickerDialog(MonthBudget.this,
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
                                Date1.setText(DOM + "-" + M + "-" + year);
                                Log.d("Document:", "Month is:" + M);

                            }
                        }, yyyy, mm, dd);
                datePickerDialog.show();
                View();
            }
        });

            }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
            if(resultCode==RESULT_OK)
            {
                buildRecyclerView();
                mExampleItem.add(new ExampleItem(R.drawable.ic_android, data.getStringExtra("text1"),
                        data.getStringExtra("text2"), R.drawable.ic_edit,R.drawable.ic_delete,R.drawable.ic_save));
                db.Add(Date1.getText().toString(), data.getStringExtra("text1"),
                        data.getStringExtra("text2"),MonthBudget.this);

            }
    }
    void View(){
        mExampleItem.clear();
        mRecyclerView.setLayoutManager(null);
        Task<QuerySnapshot> data=null;
        data=db.View(
                Date1.getText().toString(),MonthBudget.this);
        if(data.getResult().isEmpty())
        {
            //showMessage("Error","Nothing found");
            return;
        }
        buildRecyclerView();
        for (QueryDocumentSnapshot Qdoc:data.getResult())
        {
            Map<String ,Object> Mlist=null;
            Mlist= Qdoc.getData();
            mExampleItem.add(new ExampleItem(R.drawable.ic_android, Mlist.get("Item").toString(),
                    Mlist.get("Rate").toString(), R.drawable.ic_edit,R.drawable.ic_delete,R.drawable.ic_save));
        }
         data=null;
    }





































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
    Button btnLogout;
    TextView txtToatl;

/*    protected void onCreate(Bundle savedInstanceState) {
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
        btnLogout= (Button)findViewById(R.id.button_logout);
        AddData();
        viewAll();
        UpdateData();
        DeleteData();
        Total();
        Logout();
    }*/

    private void Logout() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MonthBudget.this,"Logged Out",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MonthBudget.this,MainActivity.class));
            }
        });
    }

    private void Total() {
        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Task<QuerySnapshot> data=db.View(
                        editSurname.getSelectedItem().toString(),MonthBudget.this);
                if(data.getResult().isEmpty())
                {
                    showMessage("Error","Nothing found");
                    return;
                }
                List<String> list = new ArrayList<String>();
                int total=0;
                for (QueryDocumentSnapshot Qdoc:data.getResult())
                {
                    Map<String ,Object> Mlist= Qdoc.getData();
                  total+= Integer.parseInt(Mlist.get("Rate").toString());
                }
                /*Cursor res = myDb.getSelectedData(editName.getText().toString(),
                        editSurname.getSelectedItem().toString());
                while (res.moveToNext()) {
                    list.add(res.getString(3));
                }
                int total=0;
                for(String x:list)
                    total+=Integer.parseInt(x);
*/
                txtToatl.setText("Rs."+Integer.toString(total));
            }
        });
    }

  /*  public void DeleteData() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editTextId.getText().toString().isEmpty())
                            Toast.makeText(MonthBudget.this,"Please enter ID to Delete",Toast.LENGTH_SHORT).show();
                        else db.delete(editTextId.getText().toString(),MonthBudget.this);
                       *//*  myDb.deleteData(editTextId.getText().toString(),MonthBudget.this);
                       if(deletedRows > 0)
                            Toast.makeText(MonthBudget.this,"Data Deleted",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MonthBudget.this,"Data not Deleted",Toast.LENGTH_SHORT).show();*//*
                    }
                }
        );
    }*/
    public void UpdateData() {
        btnviewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editTextId.getText().toString().isEmpty())
                            Toast.makeText(MonthBudget.this,"Please enter ID to Update",Toast.LENGTH_SHORT).show();
                        else if(editSurname.getSelectedItem().toString()=="All")
                            Toast.makeText(MonthBudget.this,"Please select Item Other than All",Toast.LENGTH_SHORT).show();
                        else db.Update(editTextId.getText().toString(),
                                editName.getText().toString(),
                                editSurname.getSelectedItem().toString(),editMarks.getText().toString(),MonthBudget.this);
                        /*boolean isUpdate = myDb.updateData(editTextId.getText().toString(),
                                editName.getText().toString(),
                                editSurname.getSelectedItem().toString(),editMarks.getText().toString());
                        if(isUpdate == true)
                            Toast.makeText(MonthBudget.this,"Data Update",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MonthBudget.this,"Data not Updated",Toast.LENGTH_SHORT).show();*/
                    }
                }
        );
    }

    /*public void viewAll() {
        btnviewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Task<QuerySnapshot> data=db.View( editName.getText().toString(),
                                editSurname.getSelectedItem().toString(),MonthBudget.this);
                        if(data.getResult().isEmpty())
                        {
                            showMessage("Error","Nothing found");
                            return;
                        }
                        List<String> list = new ArrayList<String>();
                        for (QueryDocumentSnapshot Qdoc:data.getResult())
                        {
                            Map<String ,Object> Mlist= Qdoc.getData();
                            list.add(Mlist.get("count").toString()+"__"+Mlist.get("Date").toString()+
                                    "__"+Mlist.get("Item").toString()+"__"+Mlist.get("Rate").toString());
                        }

                       *//* Cursor res = myDb.getAllData();
                       if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        while (res.moveToNext()) {
                            list.add(res.getString(0)+"__"+res.getString(1)+"__"+
                                    res.getString(2)+"__"+res.getString(3));

                        }*//*
                        // Show all data
                        String[] items=list.toArray(new String[0]);
                        // Navigate to ViewPage with Parameter items String Array.
                        Intent in = new Intent(MonthBudget.this, ViewPage.class);
                        in.putExtra("Item",items);
                        startActivity(in);
                    }

                });
    }*/

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