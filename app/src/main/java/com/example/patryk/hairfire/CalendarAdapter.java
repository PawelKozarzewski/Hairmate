package com.example.patryk.hairfire;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CalendarAdapter extends BaseAdapter {
    private ArrayList<Date> days;
    private LayoutInflater inflater;
    private Context context;
    private int defaultDay;

    public CalendarAdapter(Context context, ArrayList<Date> days, int defaultDay)
    {
        this.days = days;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.defaultDay = defaultDay;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        tz.setRawOffset(3600000);
        TimeZone.setDefault(tz);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(tz);

        Date date = days.get(position);
        cal.setTime(date);
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        int year = cal.get(Calendar.YEAR);

        Calendar calToday = Calendar.getInstance();
        calToday.setTimeZone(tz);
        if(dayOfYear < calToday.get(Calendar.DAY_OF_YEAR) || year < calToday.get(Calendar.YEAR))
            return false;
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        //Day in question
        TimeZone tz = TimeZone.getTimeZone("GMT");
        tz.setRawOffset(3600000);
        TimeZone.setDefault(tz);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(tz);

        Date date = days.get(position);
        cal.setTime(date);
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        //Chosen month
        Date todayMonth = days.get(days.size()/2);
        Calendar calTodayMonth = Calendar.getInstance();
        calTodayMonth.setTimeZone(tz);
        calTodayMonth.setTime(todayMonth);

        //Current day
        Calendar calToday = Calendar.getInstance();
        calToday.setTimeZone(tz);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.custom_calendar_day, parent, false);

            TextView textHeading = (TextView) convertView.findViewById(R.id.day_item);

            SimpleDateFormat tmp_sdf = new SimpleDateFormat("d");
            String day_number = tmp_sdf.format(days.get(position));
            textHeading.setText(day_number);

            //If a day is outside chosen month, grey it out
            if(month != calTodayMonth.get(Calendar.MONTH) || year != calTodayMonth.get(Calendar.YEAR)) {
                textHeading.setTextColor(Color.parseColor("#888888"));
            }

//            String tmp_day = Integer.toString(day+2);
            if(dayOfYear < calToday.get(Calendar.DAY_OF_YEAR)){
                textHeading.setTextColor(Color.parseColor("#e0e0e0"));
            }

            if(dayOfYear == calToday.get(Calendar.DAY_OF_YEAR) || year < calToday.get(Calendar.YEAR)){
                textHeading.setTextColor(Color.parseColor("#ff0000"));
            }
        }

        //Check current day by default
        if(position == defaultDay){
            CustomDayLinearLayout cdll = (CustomDayLinearLayout) convertView.findViewById(R.id.linear_layout_day_item);
            cdll.setBackgroundColor(Color.parseColor("#2296F3"));
        }

        return convertView;
    }
}