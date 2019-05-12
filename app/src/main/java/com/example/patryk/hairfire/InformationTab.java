package com.example.patryk.hairfire;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class InformationTab extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "InformationTab";
    public static String id;
    ImageView logo;
    TextView name, address, phone_number,email, description;
    public static String salonName;
    public String strAddress;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);

        Bundle bundle = getActivity().getIntent().getExtras();
        Salon salon = bundle.getParcelable("data");


        strAddress = salon.getAddress() + " " + salon.getCity();

        logo = (ImageView) view.findViewById(R.id.photo_tab);
        Picasso.get()
                .load(salon.getPhoto())
                .into(logo);

        name = view.findViewById(R.id.tab_salon_name);
        name.setText(salon.getName());
        salonName = salon.getName();

        address = view.findViewById(R.id.tab_salon_address);
        address.setText(salon.getAddress() + ", " + salon.getCity());

        phone_number = view.findViewById(R.id.tab_salon_tel);
        phone_number.setText(Integer.toString(salon.getPhone_number()));

        email = view.findViewById(R.id.tab_salon_email);
        email.setText(salon.getEmail());

        description = view.findViewById(R.id.tab_salon_opis);
        description.setText(salon.getDescription());

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return view;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(strAddress, 1);
            if(addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                LatLng location = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(location).title(salonName));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
