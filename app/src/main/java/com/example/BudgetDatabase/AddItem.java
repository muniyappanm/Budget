package com.example.BudgetDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItem extends AppCompatActivity {

    EditText item;
    Button add;
    DataBaseHandlerItem ItemDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ItemDb=new DataBaseHandlerItem(this);
        item=findViewById(R.id.add_add);
        add=(Button)findViewById(R.id.button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getText().toString().isEmpty())
                    Toast.makeText(AddItem.this,"Empty not alloweded",Toast.LENGTH_SHORT).show();
                else {
                    ItemDb.insertData(item.getText().toString());
                    item.setText("");
                    Toast.makeText(AddItem.this,"Item Added Successfully",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}