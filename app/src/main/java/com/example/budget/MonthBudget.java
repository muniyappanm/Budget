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
    List<String> list = new ArrayList<String>();
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
                        ,mTextView2.getText().toString());
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
                mTextView1.setEnabled(false);
                mTextView2.setEnabled(true);
                Task<QuerySnapshot> data=db.View(
                        Date1.getText().toString(),mTextView1.getText().toString()
                        ,mTextView2.getText().toString());
                if(data.getResult().isEmpty())
                {
                    //showMessage("Error","Nothing found");
                    return;
                }
                for (QueryDocumentSnapshot Qdoc:data.getResult())
                {
                    Map<String ,Object> Mlist= Qdoc.getData();
                    list.add(Mlist.get("count").toString());
                }
                Log.d("List.get(0)",list.get(0));
            }

            @Override
            public void onItemOk(int position, ImageView edit,
                                 ImageView delete, ImageView ok, EditText mTextView1, EditText mTextView2) {

                changeItem(position,mTextView1.getText().toString(),mTextView2.getText().toString());
                db.Update(list.get(0),
                        Date1.getText().toString(),mTextView2.getText().toString(),MonthBudget.this);
                edit.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                ok.setVisibility(View.INVISIBLE);
                mTextView1.setEnabled(false);
                mTextView2.setEnabled(false);

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
                mExampleItem.clear();
                mRecyclerView.setLayoutManager(null);
        Date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                View();
                                Log.d("Document:", "Month is:" + M);

                            }
                        }, yyyy, mm, dd);
                datePickerDialog.show();

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
                mExampleItem.add(new ExampleItem(R.drawable.ic_money, data.getStringExtra("text1"),
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
                Date1.getText().toString());
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
            mExampleItem.add(new ExampleItem(R.drawable.ic_money, Mlist.get("Item").toString(),
                    Mlist.get("Rate").toString(), R.drawable.ic_edit,R.drawable.ic_delete,R.drawable.ic_save));
        }
         data=null;
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