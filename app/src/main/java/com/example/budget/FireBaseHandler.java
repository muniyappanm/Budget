package com.example.budget;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FireBaseHandler extends AppCompatActivity
{
    public void Add(String Date, String Item, String Rate, MonthBudget monthBudget)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Map<String ,Object> data= new HashMap<>();
        DocumentReference doc=FirebaseFirestore.getInstance().collection("Budget").document("counter");
        doc.update("count",FieldValue.increment(1));
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot m=task.getResult();
                data.put("count",m.getLong("count").toString());
                data.put("Date",Date);
                data.put("Item",Item);
                data.put("Rate",Rate);
                db.collection("Budget").document(data.get("count").toString()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(monthBudget,"Success",Toast.LENGTH_SHORT).show();
            }
             });
                if(m.exists()) {
                    Log.d("Document", data.get("count").toString());
                    Log.d("Document1", data.toString());


                }
                else
                    Log.d("Document","No Data");
            }
        });

    }


    public Task<QuerySnapshot> View(String Date, String Item,MonthBudget monthBudget)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Map<String ,Object> data= new HashMap<>();
        Task<QuerySnapshot> doc=null;
        if(!Date.isEmpty())
        doc= FirebaseFirestore.getInstance().collection("Budget")
                .whereEqualTo("Date",Date).whereEqualTo("Item",Item).
                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Toast.makeText(monthBudget,"Success",Toast.LENGTH_SHORT).show();
                    }
                });
        else if(Date.isEmpty())
            doc= FirebaseFirestore.getInstance().collection("Budget")
                    .whereEqualTo("Item",Item).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Toast.makeText(monthBudget,"Success",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        while (!doc.isComplete())
            continue;
        return doc;

    }
}
