package com.example.patryk.hairfire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SalonList extends AppCompatActivity {

    ListView SalonList;
    TextView infotext;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Intent i;
    public static String str_where;
    public static String salon_id;
    List<Salon> salonlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_list);
        infotext = (TextView) findViewById(R.id.list_salon_header);

        Intent i = getIntent();
        str_where = i.getStringExtra("city");

        infotext.setText(getString(R.string.list_salon_header_text) + " " + str_where);


        db.collection("Salons")
                .whereEqualTo("city", str_where)
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
                            SalonListAdapter adapter = new SalonListAdapter(SalonList.this, salonlist);
                            adapter.notifyDataSetChanged();
                            SalonList.setAdapter(adapter);

                            SalonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(SalonList.this, SalonView.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("data", salonlist.get(position));
                                    intent.putExtras(bundle);
                                    salon_id = salons_id.get(position);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SalonList.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}
