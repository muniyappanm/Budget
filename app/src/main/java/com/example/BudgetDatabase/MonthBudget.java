package com.example.BudgetDatabase;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MonthBudget extends AppCompatActivity 
{
    DatabaseHandler myDb;
    List<String> list = new ArrayList<String>();
    public ArrayList<ExampleItem> mExampleItem=new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton floatingActionButton;
    DatePickerDialog datePickerDialog;
    Calendar c=Calendar.getInstance();
    int yyyy=c.get(Calendar.YEAR);
    int mm=c.get(Calendar.MONTH);
    int dd=c.get(Calendar.DAY_OF_MONTH);
      TextView Date1,Total;
    ImageView LeftDate,RightDate;
    TextView item,rate;
    EditText ItemText,RateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Expenditure");
        setContentView(R.layout.activity_monthbudget);
        Date1=findViewById(R.id.textView_date);
        Total=findViewById(R.id.textView_total);
        floatingActionButton=findViewById(R.id.fab);
        item=(TextView) findViewById(R.id.textView_item);
        rate=(TextView) findViewById(R.id.textView_Rate);
        ItemText=(EditText)findViewById(R.id.editText_item);
        RateText=(EditText)findViewById(R.id.editText_rate);
        myDb = new DatabaseHandler(this);
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
        overridePendingTransition(0, 0);
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
                Cursor res = myDb.getSelectedData(Date1.getText().toString(),mTextView1.getText().toString()
                        ,mTextView2.getText().toString());
                if(res.getCount() == 0) {
                    // show message
                    return;
                }
                List<String> list = new ArrayList<String>();
                while (res.moveToNext()) {
                    list.add(res.getString(0));

                }
                for (String i:list
                     ) {
                    myDb.deleteData(i,MonthBudget.this);
                }

                Total.setText("0");
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
                Cursor res = myDb.getSelectedData(Date1.getText().toString(),mTextView1.getText().toString()
                        ,mTextView2.getText().toString());
                if(res.getCount() == 0) {
                    // show message
                    return;
                }
                list = new ArrayList<String>();
                while (res.moveToNext()) {
                    list.add(res.getString(0));

                }
                /*Task<QuerySnapshot> data=db.View(
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
                Log.d("List.get(0)",list.get(0));*/
            }

            @Override
            public void onItemOk(int position, ImageView edit,
                                 ImageView delete, ImageView ok, EditText mTextView1, EditText mTextView2) {

                changeItem(position,mTextView1.getText().toString(),mTextView2.getText().toString());

                myDb.updateData(list.get(0),
                        Date1.getText().toString(),mTextView1.getText().toString(),mTextView1.getText().toString());
                /*db.Update(list.get(0),
                        Date1.getText().toString(),mTextView2.getText().toString(),MonthBudget.this);*/
                edit.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                ok.setVisibility(View.INVISIBLE);
                mTextView1.setEnabled(false);
                mTextView2.setEnabled(false);
                Total.setText("0");
                View();

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
                     Total.setText("0");
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
                        Total.setText("0");
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
                                Total.setText("0");
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
                int i=Integer.parseInt(Total.getText().toString())+Integer.parseInt(data.getStringExtra("text2"));
                Total.setText(""+i);
                myDb.insertData(Date1.getText().toString(), data.getStringExtra("text1"),
                        data.getStringExtra("text2"));
            }
    }
    void View(){
        mExampleItem.clear();
        mRecyclerView.setLayoutManager(null);
        buildRecyclerView();
        Cursor res = null;
        if(myDb.getbyDate(Date1.getText().toString())==null) return;
        else  res = myDb.getbyDate(Date1.getText().toString());
        int i=0;
        while (res.moveToNext()) {
            mExampleItem.add(new ExampleItem(R.drawable.ic_money, res.getString(2),
                    res.getString(3), R.drawable.ic_edit,R.drawable.ic_delete,R.drawable.ic_save));

          i+=Integer.parseInt(res.getString(3));
        }
        Total.setText(""+i);

        /*Task<QuerySnapshot> data=null;

        data=db.View(
                Date1.getText().toString());
        if(data.getResult().isEmpty())
        {
            //showMessage("Error","Nothing found");
            return;
        }
        buildRecyclerView();
        int i=0;
        for (QueryDocumentSnapshot Qdoc:data.getResult())
        {
            Map<String ,Object> Mlist=null;
            Mlist= Qdoc.getData();
            mExampleItem.add(new ExampleItem(R.drawable.ic_money, Mlist.get("Item").toString(),
                    Mlist.get("Rate").toString(), R.drawable.ic_edit,R.drawable.ic_delete,R.drawable.ic_save));
            i+=Integer.parseInt(Mlist.get("Rate").toString());
        }
        Total.setText(""+i);
         data=null;*/
    }


}
