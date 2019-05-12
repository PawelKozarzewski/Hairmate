package com.example.patryk.hairfire;

import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class CalendarView extends Fragment {
    RelativeLayout calendar_layout;
    Button reservationButton;
    ListView hourListView;
    LinearLayout header, hour_layout;
    Button btnToday, btnPrev, btnNext;
    TextView txtDateDay, txtDisplayDate, txtDateYear, loginInformation, chooseHourText;
    GridView gridView;
    int monthOffset = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private int previousSelectedPosition = -1;
    private int chosenDayPosition = -1;
    public String date_choice;
    final ArrayList<String> hoursReserved = new ArrayList<String>();
    String hourSelected;
    String clientName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.calendar_view,container,false);
        calendar_layout = (RelativeLayout) view.findViewById(R.id.calendar_layout);
        hourListView = (ListView) view.findViewById(R.id.hour_listview);
        hour_layout = (LinearLayout) view.findViewById(R.id.hour_layout);
        header = (LinearLayout) view.findViewById(R.id.calendar_header);
        btnPrev = (Button) view.findViewById(R.id.calendar_prev_button);
        btnNext = (Button) view.findViewById(R.id.calendar_next_button);
        txtDateDay = (TextView) view.findViewById(R.id.date_display_day);
        txtDateYear = (TextView) view.findViewById(R.id.date_display_year);
        txtDisplayDate = (TextView) view.findViewById(R.id.date_display_date);
        btnToday = (Button) view.findViewById(R.id.today_button);
        gridView = (GridView) view.findViewById(R.id.calendar_grid);
        CustomDayLinearLayout cdll_layout = (CustomDayLinearLayout) view.findViewById(R.id.linear_layout_day_item);
        loginInformation = (TextView) view.findViewById(R.id.login_information);
        reservationButton = (Button) view.findViewById(R.id.reservation_button);
        chooseHourText = (TextView) view.findViewById(R.id.choose_hour);


        updateDate(this.monthOffset);
        getHoursReserved();

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevMonth();
            }

        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonth();
            }

        });
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseHour();
            }

        });

        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservation();
            }
        });

        hourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hourSelected = (String) hourListView.getItemAtPosition(position);
            }
        });


        return view;
    }

    public void chooseHour(){
        chooseHourText.setText(chooseHourText.getText() + " (" + date_choice + ")");
        calendar_layout.setVisibility(View.GONE);
        if(user==null){
            loginInformation.setVisibility(View.VISIBLE);
        }else {
            reservationButton.setVisibility(View.VISIBLE);
        }
        hour_layout.setVisibility(View.VISIBLE);
        ArrayList<String> hours = new ArrayList<String>();
        for(int i = 9; i<17; i++){
           if(checkHoursReserved(hoursReserved,i+":"+"00")==false) {
                hours.add(i + ":" + "00");
           }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, hours);
        hourListView.setAdapter(adapter);

    }

    public void getHoursReserved(){
        hoursReserved.clear();
        db.collection("Visits")
                .whereEqualTo("salon_id", SalonList.salon_id)
                .whereEqualTo("date", date_choice)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                hoursReserved.add(document.get("hour").toString());
                            }
                        }
                    }
                });
    }

    public boolean checkHoursReserved(ArrayList<String> hoursReserved, String hour){
        if(hoursReserved.contains(hour)){
            return true;
        }else {
            return false;
        }
    }

    public void reservation(){
        DocumentReference userRef = db.collection("Users").document(user.getUid());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String tmp_name = document.getString("name");
                        String tmp_surname = document.getString("surname");
                        clientName = tmp_name + " " + tmp_surname;

                        final Map<String, Object> reservation = new HashMap<>();
                        reservation.put("date", date_choice);
                        reservation.put("hour", hourSelected);
                        reservation.put("salon_id", SalonList.salon_id);
                        reservation.put("salon_name", InformationTab.salonName);
                        reservation.put("user_id", user.getUid());
                        reservation.put("user_name", clientName);

                        db.collection("Visits")
                                .add(reservation)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        reservationSuccess();
                                    }
                                });
                    }
                }
            }
        });


    }

    public void reservationSuccess(){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Informacja");
        alertDialog.setMessage("Twoja wizyta została zarezerowana!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Zamknij", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        alertDialog.show();
    }

    public void prevMonth(){
        if(this.monthOffset > 0){
            this.monthOffset--;
            updateDate(this.monthOffset);
        }
    }

    public void nextMonth(){
        this.monthOffset++;
        updateDate(this.monthOffset);
    }

    public void updateDate(int offset){
        TimeZone tz = TimeZone.getTimeZone("GMT");
        tz.setRawOffset(3600000);
        TimeZone.setDefault(tz);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(tz);
        cal.add(Calendar.MONTH, monthOffset);

        Date currentDate = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,d,MMM,yyyy", new Locale("pl"));
        String[] dateToday = sdf.format(currentDate).split(",");
        date_choice = new SimpleDateFormat("dd-MM-yyyy").format(currentDate);
        dateToday[0] = dateToday[0].substring(0, 1).toUpperCase() + dateToday[0].substring(1);
        dateToday[2] = dateToday[2].substring(0, 1).toUpperCase() + dateToday[2].substring(1);
        String d_plus_m = dateToday[1] + ' ' + dateToday[2];

        txtDateDay.setText(dateToday[0]);
        txtDisplayDate.setText(d_plus_m);
        txtDateYear.setText(dateToday[3]);

        final ArrayList<Date> cells = new ArrayList<>();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = cal.get(Calendar.DAY_OF_WEEK) - 2;
        cal.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);


        //Date of current default item
        SimpleDateFormat tmpSdf = new SimpleDateFormat("d,M,yyyy", new Locale("pl"));
        String[] tmpDateToday = tmpSdf.format(currentDate).split(",");
        int day = Integer.parseInt(tmpDateToday[0]);
        int month = Integer.parseInt(tmpDateToday[1]);
        int year = Integer.parseInt(tmpDateToday[2]);

        for(int i=0; i<35; i++) {
            if(cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH)+1 == month && cal.get(Calendar.DAY_OF_MONTH) == day){
                previousSelectedPosition = i; //Day selected by default
            }

            cells.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        gridView.setAdapter(new CalendarAdapter(getContext(), cells, previousSelectedPosition));


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomDayLinearLayout selectedLayout = (CustomDayLinearLayout) view;
                selectedLayout.setBackgroundColor(Color.parseColor("#2296F3"));
                CustomDayLinearLayout previousSelectedLayout = (CustomDayLinearLayout) gridView.getChildAt(previousSelectedPosition);

                Date tmpDate = cells.get(position);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE,d,MMM,yyyy", new Locale("pl"));
                String[] dateToday = sdf.format(tmpDate).split(",");
                date_choice = new SimpleDateFormat("dd-MM-yyyy").format(tmpDate);
                dateToday[0] = dateToday[0].substring(0, 1).toUpperCase() + dateToday[0].substring(1);
                dateToday[2] = dateToday[2].substring(0, 1).toUpperCase() + dateToday[2].substring(1);
                String d_plus_m = dateToday[1] + ' ' + dateToday[2];
                txtDateDay.setText(dateToday[0]);
                txtDisplayDate.setText(d_plus_m);
                txtDateYear.setText(dateToday[3]);

                date_choice = new SimpleDateFormat("dd-MM-yyyy").format(cells.get(position));
                getHoursReserved();
                //If there is an already selected item
                if (previousSelectedPosition != -1 && previousSelectedPosition != position)
                {
                    previousSelectedLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                }

                previousSelectedPosition = position;
            }
        });
    }

}