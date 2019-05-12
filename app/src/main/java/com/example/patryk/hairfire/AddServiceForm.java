package com.example.patryk.hairfire;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddServiceForm extends AppCompatActivity {
    public static EditText serviceName, servicePrice, serviceDuration;
    FloatingActionButton confirm_button;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_form);

        serviceName = (EditText) findViewById(R.id.service_name);
        servicePrice = (EditText) findViewById(R.id.price);
        serviceDuration = (EditText) findViewById(R.id.duration);

        serviceName.addTextChangedListener(formTextWatcher);
        servicePrice.addTextChangedListener(formTextWatcher);
        serviceDuration.addTextChangedListener(formTextWatcher);
        confirm_button = (FloatingActionButton) findViewById(R.id.add);

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addService();
            }
        });
    }


    public void addService() {
        String service_name = serviceName.getText().toString();
        String service_price = servicePrice.getText().toString();
        String service_duration = serviceDuration.getText().toString();
        String salon_id = OwnerInformationTab.salon_id;


        final Map<String, Object> newService = new HashMap<>();
        newService.put("name", service_name);
        newService.put("price", service_price);
        newService.put("duration", service_duration);
        newService.put("salon_id", salon_id);
        db.collection("Services")
                .add(newService)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        onBackPressed();
                    }
                });

    }




    public boolean allFormsFilled(){
        boolean is_name = serviceName.getText().toString().isEmpty();
        boolean is_price = servicePrice.getText().toString().isEmpty();
        boolean is_duration = serviceDuration.getText().toString().isEmpty();


        if(is_name==true || is_price==true || is_duration==true) {
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
        Intent intent = new Intent(AddServiceForm.this, OwnerSalonServiceList.class);
        startActivity(intent);
        finish();
    }
}
