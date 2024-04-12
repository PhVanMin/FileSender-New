package com.example.xender.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.xender.R;
import com.example.xender.model.Contact;

import java.util.ArrayList;
import java.util.Random;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private Context context;
    ArrayList<Contact> contacts;
    public static int avatars [] = {R.drawable.avatar1,
            R.drawable.avatar2,
            R.drawable.avatar3,
            R.drawable.avatar4,
            R.drawable.avatar5,
            R.drawable.avatar6,
            R.drawable.avatar7,
            R.drawable.avatar8,
            R.drawable.avatar9,
            R.drawable.avatar10,
    };

    public ContactAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        this.context = context ;
        this.contacts= objects;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row ;

        row = inflater.inflate(R.layout.contact, null);
        TextView name = (TextView) row.findViewById(R.id.name_textview);
        TextView phone = (TextView) row.findViewById(R.id.phone_number_textview);

        Random rand = new Random();
        int index= rand.nextInt(10);



       ImageView imageView = (android.widget.ImageView) row.findViewById(R.id.avatar_imgView);
       imageView.setImageResource(avatars[index]);


        name.setText(contacts.get(position).getName());
        phone.setText(contacts.get(position).getPhoneNumber());



        return row;
    }
}
