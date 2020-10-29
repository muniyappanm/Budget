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
    FireBaseHandler db=new FireBaseHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        item=findViewById(R.id.add_add);
        add=(Button)findViewById(R.id.button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getText().toString().isEmpty())
                    Toast.makeText(AddItem.this,"Empty not alloweded",Toast.LENGTH_SHORT).show();
                else {
                    db.AddItem(item.getText().toString());
                    item.setText("");
                    Toast.makeText(AddItem.this,"Item Added Successfully",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}