package com.example.patryk.hairfire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView city;
    Button search_button, login_button, register_button, profile_button;
    public List<String> cities = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(user!=null){
            checkTypeUser();
        }
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);




        city = (AutoCompleteTextView) findViewById(R.id.main_activity_city_textview);
        city.setThreshold(1);

        search_button = (Button) findViewById(R.id.main_activity_search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(city.getText().toString().trim().length() > 0 && cities.contains((city.getText().toString()))){
                    Intent intent = new Intent(MainActivity.this, SalonList.class);
                    intent.putExtra("city", city.getText().toString());
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, R.string.error_city_empty, Toast.LENGTH_LONG).show();
                }
            }
        });



        login_button = (Button) findViewById(R.id.main_activity_login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        register_button = (Button) findViewById(R.id.main_activity_register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        profile_button = (Button) findViewById(R.id.main_activity_profile_button);
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserProfile.class);
                startActivity(intent);
                finish();
            }
        });

        if(user!=null){
            login_button.setVisibility(View.GONE);
            register_button.setVisibility(View.GONE);
            profile_button.setVisibility(View.VISIBLE);
        } else{
            login_button.setVisibility(View.VISIBLE);
            register_button.setVisibility(View.VISIBLE);
            profile_button.setVisibility(View.GONE);
        }


        db.collection("Salons")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String city = document.getString("city");
                                if(!cities.contains(city)) {
                                    cities.add(city);
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String> (MainActivity.this, android.R.layout.select_dialog_item, cities);
                            city.setAdapter(adapter);
                        }
                    }
                });



    }

    public void checkTypeUser(){
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful()){
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               if (document.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                   boolean typeUser = document.getBoolean("client");
                                   if (typeUser==false) {
                                       Intent intent = new Intent(MainActivity.this, OwnerActivity.class);
                                       startActivity(intent);
                                       finish();
                                   }
                               }
                           }
                        }
                    }
                });

    }




}
