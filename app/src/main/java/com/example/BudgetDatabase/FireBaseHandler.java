package com.example.BudgetDatabase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FireBaseHandler extends AppCompatActivity
{
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    public void Add(String Date, String Item, String Rate, MonthBudget monthBudget)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Map<String ,Object> data= new HashMap<>();
        DocumentReference doc=FirebaseFirestore.getInstance().collection("Budget").
                document(user.getUid()).collection("Counter")
                .document("Counter");
        doc.update("count",FieldValue.increment(1));
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot m=task.getResult();
                data.put("count",m.getLong("count").toString());
                data.put("Date",Date);
                data.put("Item",Item);
                data.put("Rate",Rate);
                db.collection("Budget").document(user.getUid()).collection("budget").
                        document(data.get("count").toString()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Log.d("Document", "ok");
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
    public void AddItem(String Item)
    {
              FirebaseFirestore db=FirebaseFirestore.getInstance();
             DocumentReference doc=FirebaseFirestore.getInstance().collection("Budget").
                document(user.getUid()).collection("Counter")
                .document("Itemcount");
            doc.update("itemcount",FieldValue.increment(1));
        Map<String ,Object> data= new HashMap<>();
         doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot m=task.getResult();
                data.put("Item"+m.getLong("itemcount").toString(),Item);
                db.collection("Budget").document(user.getUid()).collection("Itemlist").
                        document("itemlist").set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Log.d("Document", "ok");
                    }
                });

            }
        });
    }
    public Task<QuerySnapshot> View()
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Task<QuerySnapshot> doc=null;
        Map<String ,Object> data= new HashMap<>();
        doc= FirebaseFirestore.getInstance().collection("Budget").
                document(user.getUid()).collection("Itemlist").
                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Document", "ok");
                    }
                });
        while (!doc.isComplete())
            continue;
        return doc;
    }

    public Task<QuerySnapshot> View(String Date)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Task<QuerySnapshot> doc=null;
        Map<String ,Object> data= new HashMap<>();
        doc= FirebaseFirestore.getInstance().collection("Budget").
                    document(user.getUid()).collection("budget")
                .whereEqualTo("Date",Date).
                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Document", "ok");
                    }
                });
        while (!doc.isComplete())
            continue;
        return doc;
    }
    public Task<QuerySnapshot> View(String Date,String Item,String Rate)
    {
        FirebaseFirestore db=null;
        db=FirebaseFirestore.getInstance();
        Map<String ,Object> data=null;
        data= new HashMap<>();
        Task<QuerySnapshot> doc=null;
        doc= FirebaseFirestore.getInstance().collection("Budget").
                document(user.getUid()).collection("budget")
                .whereEqualTo("Date",Date).whereEqualTo("Rate",Rate).whereEqualTo("Item",Item).
                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Document", "ok");
                    }
                });
        while (!doc.isComplete())
            continue;
        return doc;
    }

    public void delete(String ID,MonthBudget monthBudget) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        FirebaseFirestore.getInstance().collection("Budget").document(user.getUid()).
                collection("budget").document(ID).delete().
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Document", "ok");
            }
        });
    }

    public void Update(String ID, String Date, String Rate, MonthBudget monthBudget) {
        DocumentReference doc=FirebaseFirestore.getInstance().collection("Budget").document(user.getUid()).
                collection("budget").document(ID);
        doc.update("Date",Date,"Rate",Rate);
        /*Toast.makeText(monthBudget,"Data Updated",Toast.LENGTH_SHORT).show();*/
    }

}
