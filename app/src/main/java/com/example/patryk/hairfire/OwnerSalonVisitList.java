package com.example.patryk.hairfire;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OwnerSalonVisitList extends Fragment {
    private static final String TAG = "OwnerSalonVisitList";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String salon_id, user_id;
    ListView visitList;
    List<Visit> visitsList = new ArrayList<>();
    String date, hour, clientName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_salon_visits_list,container,false);
        visitList = (ListView) view.findViewById(R.id.visits_listview);

        Bundle bundle = getActivity().getIntent().getExtras();
        salon_id = bundle.getString("EXTRA_SALON_ID");

        db.collection("Visits")
                .whereEqualTo("salon_id", salon_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task_1) {
                        if (task_1.isSuccessful()) {
                            for (QueryDocumentSnapshot document_1 : task_1.getResult()) {
                                date = document_1.getString("date");
                                hour = document_1.getString("hour");
                                salon_id = document_1.getString("salon_id");
                                user_id = document_1.getString("user_id");
                                clientName = document_1.getString("user_name");

                                Visit visit = new Visit(clientName, "Data: " + date, "Godzina: " + hour);
                                visitsList.add(visit);
                            }

                            Log.d(TAG, "03_user_id: " + user_id);
                            Log.d(TAG, "03__salon_id: " + salon_id);

                            VisitsListAdapter adapter = new VisitsListAdapter(getActivity(), visitsList);
                            adapter.notifyDataSetChanged();
                            visitList.setAdapter(adapter);
                        }
                    }
                });


        return view;
    }
}