package com.creativecub.socialapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativecub.socialapp.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.ArrayList;

public class SimpleAdapterFeed extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> alPost;
    private final ArrayList<String> alName;
    private final ArrayList<ParseFile> alImage;



    public SimpleAdapterFeed(Context context, ArrayList<String> alPost, ArrayList<String> alName, ArrayList<ParseFile> alImage) {
        super(context, R.layout.list_row, alPost);
        this.context = context;
        this.alPost = alPost;
        this.alName = alName;
        this.alImage = alImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_row, null, false);
        TextView textView = (TextView) rowView.findViewById(R.id.tvPost);
        TextView tvListRowTitle = (TextView) rowView.findViewById(R.id.tvListRowTitle);
        ImageView ivListRow = (ImageView) rowView.findViewById(R.id.ivListRow);

        tvListRowTitle.setText(alName.get(position));
        textView.setText(alPost.get(position));


        ParseFile file = (ParseFile) alImage.get(position);
        loadImage(ivListRow, file);



        return rowView;
    }

    public void loadImage(final ImageView ivImage, ParseFile parseFile){

        if (parseFile != null) {
            parseFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        // data has the bytes for the resume
                        //  Toast.makeText(context, "Got image", Toast.LENGTH_SHORT).show();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ivImage.setImageBitmap(bitmap);
                        // bmGlobal = bitmap;

                    } else {
                        // something went wrong
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {

            Toast.makeText(context, "Image not saved", Toast.LENGTH_SHORT).show();
        }

    }
}  