package com.example.budget;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FireBaseHandler extends AppCompatActivity
{

    public void Add(String Date, String Item, String Rate)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Map<String,Object> budget= new HashMap<>();
        budget.put("Date",Date);
        budget.put("Item",Item);
        budget.put("Rate",Rate);
        db.collection("Budget").document("1").set(budget).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(FireBaseHandler.this,"Success",Toast.LENGTH_SHORT);
            }
        });

    }

}
