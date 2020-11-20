package com.dhumuni.BudgetDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectCurrency extends AppCompatActivity {
    private DatabaseCurrency CurrencyDb=new DatabaseCurrency(this);
    AutoCompleteTextView autocompletecurrency;
    Button ok;
    ArrayList<String> currency=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcurrency);
        setTitle("Currency Selection");
        CurrencyDb=new DatabaseCurrency(this);
        autocompletecurrency=findViewById(R.id.autocompletecurrency);
        ok=findViewById(R.id.button_ok);
        View();
        Ok();
        AutoComplete(autocompletecurrency);
    }
    private void Ok() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autocompletecurrency.getText().toString().isEmpty())
                    Toast.makeText(SelectCurrency.this,"Empty not alloweded",Toast.LENGTH_SHORT).show();
                else {

                    CurrencyDb.updateData(autocompletecurrency.getText().toString());
                    Toast.makeText(SelectCurrency.this, "Currency Selected Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SelectCurrency.this, MainActivity.class));
                }
            }
        });
    }

    private void View() {
        Cursor res = CurrencyDb.getAllData();
        if(res.getCount() == 0) {
            // show message
            return;
        }
        while (res.moveToNext()) {
            currency.add(res.getString(1));

        }
    }
    private void AutoComplete(AutoCompleteTextView autocompletecurrency){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, currency);

        autocompletecurrency.setThreshold(1);
        autocompletecurrency.setAdapter(adapter);

    }
}
