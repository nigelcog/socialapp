package com.creativecub.socialapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativecub.socialapp.R;
import com.creativecub.socialapp.activity.ActivityPost;

import java.util.ArrayList;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> alString;

    public MySimpleArrayAdapter(Context context, ArrayList<String> alString) {
        super(context, R.layout.list_row, alString);
        this.context = context;
        this.alString = alString;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_row, null, false);
        TextView textView = (TextView) rowView.findViewById(R.id.tvPost);
        TextView tvListRowTitle = (TextView) rowView.findViewById(R.id.tvListRowTitle);
        ImageView ivListRow = (ImageView) rowView.findViewById(R.id.ivListRow);

        tvListRowTitle.setText(ActivityPost.fullNameGlobal);
        textView.setText(alString.get(position));
        ivListRow.setImageBitmap(ActivityPost.bmGlobal);

        return rowView;
    }
}  