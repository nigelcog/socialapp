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
    private LayoutInflater inflater = null;

    public MySimpleArrayAdapter(Context context, ArrayList<String> alString) {
        super(context, R.layout.list_row, alString);
        this.context = context;
        this.alString = alString;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            if (inflater == null)
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_row, null, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvPost = (TextView) convertView.findViewById(R.id.tvPost);
            viewHolder.tvListRowTitle = (TextView) convertView.findViewById(R.id.tvListRowTitle);
            viewHolder.ivListRow = (ImageView) convertView.findViewById(R.id.ivListRow);
            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.tvListRowTitle.setText(ActivityPost.fullNameGlobal);
        holder.tvPost.setText(alString.get(position));
        holder.ivListRow.setImageBitmap(ActivityPost.bmGlobal);

        return convertView;
    }

    private class ViewHolder {

        public TextView tvPost;
        public TextView tvListRowTitle;
        public ImageView ivListRow;
    }
}