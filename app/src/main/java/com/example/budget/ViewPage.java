package com.example.budget;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class ViewPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("List of Purchase");
        setContentView(R.layout.activity_main);

        ListView lstview=(ListView)findViewById(R.id.listview);
        // Inflate header view
        ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.header, lstview,false);
        // Add header view to the ListView
        lstview.addHeaderView(headerView);

        String[] items=getIntent().getExtras().getStringArray("Item");
        // Get the string array defined in strings.xml file
        //String[] items=getResources().getStringArray(R.array.list_items);
        // Create an adapter to bind data to the ListView
        LstAdapter adapter=new LstAdapter(this,R.layout.rowlayout,R.id.txtsno,items);
        // Bind data to the ListView
        lstview.setAdapter(adapter);

    }
}
