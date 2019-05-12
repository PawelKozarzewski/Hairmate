package com.example.patryk.hairfire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VisitsList extends AppCompatActivity {

    ListView visitList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static String salon_id;
    List<Visit> visitsList = new ArrayList<>();
    String date, hour, salonName;
    String dateToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits_list);


        db.collection("Visits")
                .whereEqualTo("user_id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                date = document.getString("date");
                                hour = document.getString("hour");
                                salon_id = document.getString("salon_id");
                                salonName = document.getString("salon_name");
                                Visit visit = new Visit(salonName, "Data: " + date, "Godzina: " + hour);
                                visitsList.add(visit);
                                }
                            visitList = (ListView) findViewById(R.id.visits_listview);
                            VisitsListAdapter adapter = new VisitsListAdapter(VisitsList.this, visitsList);
                            adapter.notifyDataSetChanged();
                            visitList.setAdapter(adapter);
                        }
                    }
                });

    }


}
