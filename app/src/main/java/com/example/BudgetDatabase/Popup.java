package com.example.BudgetDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Popup extends AppCompatActivity {
    FireBaseHandler db=new FireBaseHandler();
    private FloatingActionButton floatingActionButton;
    private Spinner Item;
    private EditText mTextView2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small);
        Item = findViewById(R.id.spinner_item);
        mTextView2 = findViewById(R.id.editText_rate);
        floatingActionButton=findViewById(R.id.add);
        GetSpinnerItem();
        Add();
    }

    private void GetSpinnerItem() {
        List<String> spinnerArray =  new ArrayList<String>();
        Task<QuerySnapshot> data=null;
        data=db.View();
        if(data.getResult().isEmpty())
        {
            //showMessage("Error","Nothing found");
            return;
        }
        for (QueryDocumentSnapshot Qdoc:data.getResult())
        {
            Map<String ,Object> Mlist=null;
            Mlist= Qdoc.getData();
            for(int i=1;i<=Mlist.size();i++)
                spinnerArray.add(Mlist.get("Item"+i).toString());
        }
        data=null;


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Item.setAdapter(adapter);
    }

    private void Add(){
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if( Item.getSelectedItem().toString().isEmpty()||mTextView2.getText().toString().isEmpty())
                    Toast.makeText(Popup.this,
                            "Empty Data not alloweded",Toast.LENGTH_SHORT).show();
                else {
                    Intent in = new Intent();
                    in.putExtra("text1",
                            Item.getSelectedItem().toString()).putExtra("text2", mTextView2.getText().toString());
                    setResult(RESULT_OK, in);
                    finish();
                }
            }
        });
    }
}

