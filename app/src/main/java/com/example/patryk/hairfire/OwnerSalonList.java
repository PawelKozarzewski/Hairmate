package com.example.patryk.hairfire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OwnerSalonList extends AppCompatActivity {

    ListView SalonList;
    TextView infotext;
    FloatingActionButton btnAdd;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String salon_id;
    List<Salon> salonlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_salon_list);
        infotext = (TextView) findViewById(R.id.list_salon_header);
        btnAdd = (FloatingActionButton) findViewById(R.id.fab);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSalon();
            }

        });

        Intent i = getIntent();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String RegisteredUserID = currentUser.getUid();

        db.collection("Salons")
                .whereEqualTo("user_id", RegisteredUserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final List<String> salons_id = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Salon sal = document.toObject(Salon.class);
                                salons_id.add(document.getId());
                                salonlist.add(sal);
                            }
                            SalonList = (ListView) findViewById(R.id.list_salon_listview);
                            SalonListAdapter adapter = new SalonListAdapter(OwnerSalonList.this, salonlist);
                            adapter.notifyDataSetChanged();
                            SalonList.setAdapter(adapter);

                            SalonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(OwnerSalonList.this, OwnerSalonView.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("data", salonlist.get(position));
                                    salon_id = salons_id.get(position);
                                    bundle.putString("EXTRA_SALON_ID", salon_id);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OwnerSalonList.this, OwnerActivity.class);
        startActivity(intent);
        finish();
    }


    public void addSalon(){
        Intent intent = new Intent(OwnerSalonList.this, AddSalonForm.class);
        startActivity(intent);
        finish();
    }


}
