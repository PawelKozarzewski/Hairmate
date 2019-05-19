package com.example.patryk.hairfire;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.patryk.hairfire.SalonList.salon_id;


public class SalonGallery extends Fragment {

    private String[] imageUrls = new String[3];
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_salon_gallery,container,false);


        db.collection("Salons")
                .document(salon_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult() != null){
                                imageUrls[0] = task.getResult().getString("photo1");
                                imageUrls[1] = task.getResult().getString("photo2");
                                imageUrls[2] = task.getResult().getString("photo3");
                                ViewPager viewPager = view.findViewById(R.id.viewPager);
                                ImageAdapter adapter = new ImageAdapter(getActivity(), imageUrls);
                                viewPager.setAdapter(adapter);

                            }
                        }
                    }
                });


        return view;

    }
}
