package com.example.patryk.hairfire;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ServiceListAdapter extends ArrayAdapter<Service> {
    private List<Service> data;
    private Context context;

    public ServiceListAdapter(Context context, List<Service> data){
        super(context, R.layout.service_list_row);
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
            row = inflater.inflate(R.layout.service_list_row, null);

            holder = new ViewHolder();
            holder.name = (TextView) row.findViewById(R.id.listview_item_service_name);
            holder.price = (TextView) row.findViewById(R.id.listview_item_price);
            holder.duration = (TextView) row.findViewById(R.id.listview_item_duration);


            row.setTag(holder);
        } else{
            holder = (ViewHolder) row.getTag();
        }


        holder.name.setText(data.get(position).getName());
        holder.price.setText(data.get(position).getPrice());
        holder.duration.setText(data.get(position).getDuration());

        return row;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class ViewHolder{
        TextView name;
        TextView price;
        TextView duration;
    }
}
