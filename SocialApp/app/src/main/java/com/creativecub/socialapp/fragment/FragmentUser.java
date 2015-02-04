package com.creativecub.socialapp.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativecub.socialapp.R;
import com.creativecub.socialapp.activity.ActivityRegisterLogin;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUser extends Fragment implements View.OnClickListener {

    TextView tvFullName;
    TextView tvEmail;
    TextView tvProfession;

    Button btnLogout;
    Button btnEditInfo;

    ImageView ivProfilePic;

    ParseUser user;

    private final int SELECT_PHOTO = 1;

    String imageName, imgType;
    Bitmap selectedImage = null;
    byte[] data;

    public FragmentUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        tvFullName = (TextView)rootView.findViewById(R.id.tvFullName);
        tvEmail = (TextView)rootView.findViewById(R.id.tvEmail);
        tvProfession = (TextView)rootView.findViewById(R.id.tvProfession);

        btnLogout = (Button)rootView.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

        btnEditInfo = (Button)rootView.findViewById(R.id.btnEditInfo);
        btnEditInfo.setOnClickListener(this);

        ivProfilePic = (ImageView)rootView.findViewById(R.id.ivProfilePic);
        ivProfilePic.setOnClickListener(this);


        user = ParseUser.getCurrentUser();

        try {
            tvFullName.setText(user.get("full_name").toString());
        }
        catch (Exception e){
            e.printStackTrace();

        }


        tvEmail.setText(user.getEmail());
        tvProfession.setText(user.get("iam").toString());


        ParseFile file = (ParseFile)user.get("image");

        if(file != null){
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        // data has the bytes for the resume
                        Toast.makeText(getActivity(), "Got image", Toast.LENGTH_SHORT).show();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
                        ivProfilePic.setImageBitmap(bitmap);

                    } else {
                        // something went wrong
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else {

            Toast.makeText(getActivity(), "Image not saved", Toast.LENGTH_SHORT).show();
        }

        //byte[] data = Parse.




        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnLogout:

                ParseUser.logOut();
                startActivity(new Intent(getActivity(),ActivityRegisterLogin.class));
                getActivity().finish();

                break;


            case R.id.btnEditInfo:

//                user.put("full_name","The Mask");
//                user.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            // Hooray! Let them use the app now.
//                            Toast.makeText(getActivity(), "Data saved", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            // Sign up didn't succeed. Look at the ParseException
//                            // to figure out what went wrong
//                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container_myaccount, new FragmentEditInfo(), "edit_info");
                ft.addToBackStack(null);
                ft.commit();


                break;

            case R.id.ivProfilePic:



                break;
        }

    }





}
