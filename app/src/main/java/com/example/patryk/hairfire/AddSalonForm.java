package com.example.patryk.hairfire;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSalonForm extends AppCompatActivity {

    public static EditText name, street, city, phone, email, description;
    FloatingActionButton confirm_button;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String RegisteredUserID = currentUser.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_add_salon);

        name = (EditText) findViewById(R.id.salon_name);
        street = (EditText) findViewById(R.id.address_street);
        city = (EditText) findViewById(R.id.address_city);
        phone = (EditText) findViewById(R.id.phone_number);
        email = (EditText) findViewById(R.id.email);
        description = (EditText) findViewById(R.id.description);
        confirm_button = (FloatingActionButton) findViewById(R.id.fab);

        name.addTextChangedListener(formTextWatcher);
        street.addTextChangedListener(formTextWatcher);
        city.addTextChangedListener(formTextWatcher);
        phone.addTextChangedListener(formTextWatcher);
        email.addTextChangedListener(formTextWatcher);
        description.addTextChangedListener(formTextWatcher);

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSalon();
            }
        });
    }

    public void addSalon(){
        String s_name = name.getText().toString();
        String s_street = street.getText().toString();
        String s_city = city.getText().toString();
        String s_phone = phone.getText().toString();
        int i_phone = Integer.valueOf(s_phone);
        String s_email = email.getText().toString();
        String s_description = description.getText().toString();
        String s_user_id = RegisteredUserID;
        String s_photo = "path";

        final Map<String, Object> newSalon = new HashMap<>();
        newSalon.put("address", s_street);
        newSalon.put("city", s_city);
        newSalon.put("description", s_description);
        newSalon.put("email", s_email);
        newSalon.put("name", s_name);
        newSalon.put("phone_number", i_phone);
        newSalon.put("photo", s_photo);
        newSalon.put("user_id", s_user_id);
        db.collection("Salons")
                .add(newSalon)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        onBackPressed();
                    }
                });


//        Salon newSalon = new Salon(s_name, s_city, s_street, s_email, i_phone, s_description, s_photo, s_userID);
//        db.collection("Salons").add(newSalon);
    }

    public boolean allFormsFilled(){
        boolean is_name = name.getText().toString().isEmpty();
        boolean is_street = street.getText().toString().isEmpty();
        boolean is_city = city.getText().toString().isEmpty();
        boolean is_phone = phone.getText().toString().isEmpty();
        boolean is_email = email.getText().toString().isEmpty();
        boolean is_description = description.getText().toString().isEmpty();

        if(is_name==true || is_street==true || is_city==true || is_phone==true || is_email==true || is_description==true) {
            return false;
        } else{
            return true;
        }
    }

    private TextWatcher formTextWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if( allFormsFilled() == false) {
                confirm_button.setVisibility(View.GONE);
            } else{
                confirm_button.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddSalonForm.this, OwnerSalonList.class);
        startActivity(intent);
        finish();
    }
}