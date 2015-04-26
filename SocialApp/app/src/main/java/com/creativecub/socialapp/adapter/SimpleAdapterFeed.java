package com.creativecub.socialapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativecub.socialapp.R;

import java.util.ArrayList;

public class SimpleAdapterFeed extends ArrayAdapter<String> {
    private  Context context = null;
    private  ArrayList<String> alPost =  null;
    private  ArrayList<String> alName = null;
    private  ArrayList<String> alImageLink = null;

    LayoutInflater inflater = null;

    public SimpleAdapterFeed(Context context, ArrayList<String> alPost, ArrayList<String> alName, ArrayList<String> alImageLink) {
        super(context, R.layout.list_row, alPost);
        this.context = context;
        this.alPost = alPost;
        this.alName = alName;
        this.alImageLink = alImageLink;
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
        holder.tvListRowTitle.setText(alName.get(position));
        holder.tvPost.setText(alPost.get(position));

        if(alImageLink.get(position)!=null)
            new ImageLoader(holder.ivListRow,context).execute(new String[]{alImageLink.get(position)});

        return convertView;
    }

    private class ViewHolder {

        public TextView tvPost;
        public TextView tvListRowTitle;
        public ImageView ivListRow;
    }
}  