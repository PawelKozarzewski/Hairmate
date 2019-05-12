package com.example.patryk.hairfire;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class OwnerInformationTab extends Fragment {
    private static final String TAG = "OwnerInformationTab";
    public static String salon_id;
    ImageView logo;
    TextView name, address, phone, email, description;
    EditText name_edit, street_edit, city_edit, phone_edit, email_edit, description_edit;
    ViewSwitcher name_switcher, address_switcher, phone_switcher, email_switcher, description_switcher;
    FloatingActionButton btnEdit, btnConfirm;
    public static String salonName;
    String street, city;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.owner_tab1_fragment,container,false);

        Bundle bundle = getActivity().getIntent().getExtras();
        salon_id = bundle.getString("EXTRA_SALON_ID");
        Salon salon = bundle.getParcelable("data");

        logo = (ImageView) view.findViewById(R.id.photo_tab);
        Picasso.get()
                .load(salon.getPhoto())
                .into(logo);

        name = view.findViewById(R.id.tab_salon_name);
        name.setText(salon.getName());
        salonName = salon.getName();

        address = view.findViewById(R.id.tab_salon_address);
        address.setText(salon.getAddress() + ", " + salon.getCity());
        street = salon.getAddress();
        city = salon.getCity();

        phone = view.findViewById(R.id.tab_salon_phone);
        phone.setText(Integer.toString(salon.getPhone_number()));

        email = view.findViewById(R.id.tab_salon_email);
        email.setText(salon.getEmail());

        description = view.findViewById(R.id.tab_salon_description);
        description.setText(salon.getDescription());


        name_switcher = view.findViewById(R.id.switcher_name);
        address_switcher = view.findViewById(R.id.switcher_address);
        phone_switcher = view.findViewById(R.id.switcher_phone);
        email_switcher = view.findViewById(R.id.switcher_email);
        description_switcher = view.findViewById(R.id.switcher_description);

        name_edit = name_switcher.findViewById(R.id.tab_salon_name_edit);
        street_edit = address_switcher.findViewById(R.id.tab_salon_address_street_edit);
        city_edit = address_switcher.findViewById(R.id.tab_salon_address_city_edit);
        phone_edit = phone_switcher.findViewById(R.id.tab_salon_phone_edit);
        email_edit = email_switcher.findViewById(R.id.tab_salon_email_edit);
        description_edit = description_switcher.findViewById(R.id.tab_salon_description_edit);

        name_edit.addTextChangedListener(formTextWatcher);
        street_edit.addTextChangedListener(formTextWatcher);
        city_edit.addTextChangedListener(formTextWatcher);
        phone_edit.addTextChangedListener(formTextWatcher);
        email_edit.addTextChangedListener(formTextWatcher);
        description_edit.addTextChangedListener(formTextWatcher);

        btnEdit = view.findViewById(R.id.fab_edit);
        btnConfirm = view.findViewById(R.id.fab_ok);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSalon();
            }

        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSalon();
            }

        });

        return view;
    }

    public void editSalon(){
        btnEdit.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.VISIBLE);

        name_switcher.showNext();
        address_switcher.showNext();
        phone_switcher.showNext();
        email_switcher.showNext();
        description_switcher.showNext();

        name_edit.setText(name.getText().toString());
        street_edit.setText(street);
        city_edit.setText(city);
        phone_edit.setText(phone.getText().toString());
        email_edit.setText(email.getText().toString());
        description_edit.setText(description.getText().toString());
    }

    public void updateSalon(){
        btnConfirm.setVisibility(View.GONE);
        btnEdit.setVisibility(View.VISIBLE);

        name_switcher.showPrevious();
        address_switcher.showPrevious();
        phone_switcher.showPrevious();
        email_switcher.showPrevious();
        description_switcher.showPrevious();

        name.setText(name_edit.getText().toString());
        String tmp_address = street_edit.getText().toString() + ", " + city_edit.getText().toString();
        address.setText(tmp_address);
        phone.setText(phone_edit.getText().toString());
        email.setText(email_edit.getText().toString());
        description.setText(description_edit.getText().toString());

        Bundle bundle = getActivity().getIntent().getExtras();
        Salon salon = bundle.getParcelable("data");

        salon.setName(name.getText().toString());
        salon.setAddress(street_edit.getText().toString());
        salon.setCity(city_edit.getText().toString());
        salon.setPhone_number(Integer.valueOf(phone.getText().toString()));
        salon.setEmail(email.getText().toString());
        salon.setDescription(description.getText().toString());


        DocumentReference salonRef = db.collection("Salons").document(salon_id);

        salonRef
                .update(
                        "address", street_edit.getText().toString(),
                        "city", city_edit.getText().toString(),
                        "description", description.getText().toString(),
                        "email", email.getText().toString(),
                        "name", name.getText().toString(),
                        "phone_number", Integer.valueOf(phone.getText().toString())
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Salon info successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating salon info", e);
                    }
                });
    }

    public boolean allFormsFilled(){
        boolean is_name = name_edit.getText().toString().isEmpty();
        boolean is_street = street_edit.getText().toString().isEmpty();
        boolean is_city = city_edit.getText().toString().isEmpty();
        boolean is_phone = phone_edit.getText().toString().isEmpty();
        boolean is_email = email_edit.getText().toString().isEmpty();
        boolean is_description = description_edit.getText().toString().isEmpty();

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
                btnConfirm.setVisibility(View.GONE);

            } else{
                btnConfirm.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}

