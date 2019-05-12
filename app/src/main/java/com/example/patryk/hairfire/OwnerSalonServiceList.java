package com.example.patryk.hairfire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OwnerSalonServiceList extends Fragment {
    private static final String TAG = "OwnerSalonServiceList";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView serviceListView;
    String salon_name, salon_id, price, duration;
    FloatingActionButton btnAdd;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_owner_salon_service_list,container,false);

        String salonId = OwnerInformationTab.salon_id;

        btnAdd = (FloatingActionButton) view.findViewById(R.id.add_service);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addService();
            }
        });

        db.collection("Services")
                .whereEqualTo("salon_id", salonId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Service> serviceList = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                salon_name = document.getString("name");
                                salon_id = document.getString("salon_id");
                                price = document.getString("price");
                                duration = document.getString("duration");

                                Service service = new Service(salon_id, salon_name, "Cena: " + price + " zł", "Czas trwania: " + duration + " minut");
                                serviceList.add(service);
                            }
                            serviceListView = (ListView) view.findViewById(R.id.list_service_listview);
                            ServiceListAdapter adapter = new ServiceListAdapter(getContext(), serviceList);
                            adapter.notifyDataSetChanged();
                            serviceListView.setAdapter(adapter);

                        }
                    }
                });

        return view;
    }

    public void addService(){
        Intent intent = new Intent(getContext(), AddServiceForm.class);
        startActivity(intent);
        getActivity().finish();
    }
}
