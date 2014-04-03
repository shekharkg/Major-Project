package com.example1.cp.gridpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SKG on 03-Apr-14.
 */
public class ImageListAdapter extends ArrayAdapter<String> {
    Context context;
    List<String> imageArray = new ArrayList<String>();
    List<String> imageTitle = new ArrayList<String>();


       ImageListAdapter(Context c,List<String> sampleImage,List<String> title){
        super(c,R.layout.sample_images,R.id.text_sample, title);
        this.context= c;
        this.imageArray = sampleImage;
        this.imageTitle = title;
    }

    class MyViewHolder{
        ImageView myImage;
        TextView myText;

        MyViewHolder(View v){
            myImage = (ImageView) v.findViewById(R.id.image_sample);
            myText = (TextView) v.findViewById(R.id.text_sample);
        }

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        MyViewHolder holder = null;
        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.sample_images, parent,false);
            holder = new MyViewHolder(row);
            row.setTag(holder);
        }
        else{
            holder = (MyViewHolder) row.getTag();
        }

        Ion.with(holder.myImage).placeholder(R.drawable.product).error(R.drawable.product).load(imageArray.get(position));
        holder.myText.setText(imageTitle.get(position));
        return row;

    }
}
