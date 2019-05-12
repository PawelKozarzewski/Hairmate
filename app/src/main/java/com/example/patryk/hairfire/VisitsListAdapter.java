package com.example.patryk.hairfire;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VisitsListAdapter extends ArrayAdapter<Visit> {
    private List<Visit> data;
    private Context context;

    public VisitsListAdapter(Context context, List<Visit> data){
        super(context, R.layout.visit_list_row);
        this.data=data;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.visit_list_row, null);

            holder = new ViewHolder();
            //holder.photo = (ImageView) row.findViewById(R.id.listview_image);
            holder.date = (TextView) row.findViewById(R.id.visit_date);
            holder.hour = (TextView) row.findViewById(R.id.visit_hour);
            holder.name = (TextView) row.findViewById(R.id.visit_salon_name);


            row.setTag(holder);
        } else{
            holder = (ViewHolder) row.getTag();
        }


        //Picasso.get()
                //.load(data.get(position).getPhoto())
               // .into(holder.photo);
        holder.date.setText(data.get(position).getDate());
        holder.hour.setText(data.get(position).getHour());
        holder.name.setText(data.get(position).getSalonName());

        return row;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class ViewHolder{
        //ImageView photo;
        TextView date;
        TextView hour;
        TextView name;
    }
}

