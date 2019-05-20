package com.example.patryk.hairfire;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OpinionAdapter extends ArrayAdapter<Opinion> {

    private List<Opinion> data;
    private Context context;

    public OpinionAdapter(Context context, List<Opinion> data) {
        super(context, R.layout.opinion_row);
        this.data=data;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.opinion_row, null);

            holder = new ViewHolder();
            holder.photo = (ImageView) row.findViewById(R.id.opinion_image);
            holder.title = (TextView) row.findViewById(R.id.opinion_item_title);
            holder.content = (TextView) row.findViewById(R.id.opinion_item_content);
            holder.author = (TextView) row.findViewById(R.id.opinion_item_author);
            holder.date = (TextView) row.findViewById(R.id.opinion_item_date);
            holder.image = (ImageView) row.findViewById(R.id.opinion_photo);
            holder.info = (TextView) row.findViewById(R.id.photo_info);

            row.setTag(holder);
        } else{
            holder = (ViewHolder) row.getTag();
        }

        Picasso.get()
                .load(data.get(position).getPhoto())
                .centerCrop()
                .resize(800,600)
                .into(holder.image);
        holder.title.setText(data.get(position).getTitle());
        holder.content.setText(data.get(position).getContent());
        holder.author.setText("Autor: " + data.get(position).getAuthor());
        holder.date.setText("Data: " + data.get(position).getDate());

        if(data.get(position).getPhoto().equals("null")){
            holder.info.setVisibility(View.GONE);
        }


        return row;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class ViewHolder{
        ImageView photo;
        ImageView image;
        TextView info;
        TextView title;
        TextView content;
        TextView author;
        TextView date;
    }
}
