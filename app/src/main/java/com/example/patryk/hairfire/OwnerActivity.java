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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OwnerActivity extends AppCompatActivity {

    Button my_salons_button, login_button, register_button, profile_button;
    public List<String> cities = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_owner);

        my_salons_button = (Button) findViewById(R.id.main_activity_my_salons_button);
        my_salons_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerActivity.this, OwnerSalonList.class);
                startActivity(intent);
                finish();
            }
        });


        login_button = (Button) findViewById(R.id.main_activity_login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        register_button = (Button) findViewById(R.id.main_activity_register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerActivity.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        profile_button = (Button) findViewById(R.id.main_activity_profile_button);
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerActivity.this, UserProfile.class);
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
    }
}
