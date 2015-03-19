package com.creativecub.socialapp.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creativecub.socialapp.R;
import com.creativecub.socialapp.activity.ActivityFeed;
import com.creativecub.socialapp.activity.ActivityMyAccount;
import com.creativecub.socialapp.activity.ActivityPost;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class FragmentLogin extends Fragment implements View.OnClickListener {

    EditText username,password;
    Button btnlogin;
    TextView forgot;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.frament_login, container, false);
        username = (EditText)root.findViewById(R.id.username);
        password = (EditText)root.findViewById(R.id.password);
        forgot = (TextView)root.findViewById(R.id.forgot);
        btnlogin = (Button)root.findViewById(R.id.btnlogin);
        forgot.setOnClickListener(this);
        btnlogin.setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences("Social App", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String strUsername = sharedPreferences.getString("email","");
        username.setText(strUsername);

        progress = new ProgressDialog(getActivity());
        progress.setTitle("Please wait..");
        progress.setMessage("Signing in");
        progress.setCancelable(true);

        return root;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnlogin:
                if(getPassword()!=null && getEmailId()!=null) {

                    progress.show();

                    ParseUser.logInInBackground(getEmailId(), getPassword(), new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                // Hooray! The user is logged in.
                                if(user.getBoolean("emailVerified")) {

                                    Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_LONG).show();


                                    editor.putString("email",username.getText().toString());
                                    editor.putString("local", user.get("iam").toString());

                                    if(user.get("iam").toString().equals("Student")){
                                        editor.putString("global", "Professor");
                                    }
                                    else {
                                        editor.putString("global", "Student");
                                    }

                                    editor.commit();

                                    ParseFile file = (ParseFile)user.get("image");

                                    if(file != null){
                                        file.getDataInBackground(new GetDataCallback() {
                                            @Override
                                            public void done(byte[] bytes, ParseException e) {
                                                if (e == null) {
                                                    // data has the bytes for the resume
                                                    Toast.makeText(getActivity(), "Got image", Toast.LENGTH_SHORT).show();
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                                    ActivityPost.bmGlobal = bitmap;

                                                    progress.dismiss();
                                                    startActivity(new Intent(getActivity(), ActivityFeed.class));
                                                    getActivity().finish();

                                                } else {
                                                    // something went wrong
                                                    progress.dismiss();
                                                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(getActivity(), ActivityFeed.class));
                                                    getActivity().finish();
                                                }
                                            }
                                        });

                                    }
                                    else {

                                        progress.dismiss();
                                        ActivityMyAccount.intflag = 2;
                                        startActivity(new Intent(getActivity(), ActivityMyAccount.class));
                                        getActivity().finish();
                                        Toast.makeText(getActivity(), "Image not saved", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else {
                                   // Toast.makeText(getActivity(), "Verify your Email", Toast.LENGTH_LONG).show();
                                    progress.dismiss();
                                    showOkDialog();
                                    editor.putString("email",username.getText().toString());
                                    editor.commit();
                                }
                            } else {
                                progress.dismiss();
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                editor.putString("email",username.getText().toString());
                                editor.commit();
                            }
                        }
                    });
                }
                break;
            case R.id.forgot:
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, new FragmentReset(), "reset");
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }
    public String getEmailId()
    {
        if(!username.getText().toString().trim().equals("") && username.getText().toString()!=null)
        {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches())
            {
                return username.getText().toString();
            }
            else
            {
                Toast.makeText(getActivity(),"Enter correct email id",Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }
    public String getPassword()
    {
        if(!password.getText().toString().trim().equals("") && password.getText().toString()!=null)
        {
            return password.getText().toString();
        }
        return null;
    }

    public void showOkDialog()
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please verify your email first. \n\nWe have sent you an email with the verification link.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
