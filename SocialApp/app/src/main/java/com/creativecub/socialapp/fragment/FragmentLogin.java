package com.creativecub.socialapp.fragment;


import android.content.Intent;
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
import com.creativecub.socialapp.activity.ActivityPost;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class FragmentLogin extends Fragment implements View.OnClickListener {

    EditText username,password;
    Button btnlogin;
    TextView forgot;
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
        return root;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnlogin:
                if(getPassword()!=null && getEmailId()!=null) {
                    ParseUser.logInInBackground(getEmailId(), getPassword(), new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                // Hooray! The user is logged in.
                                if(user.getBoolean("emailVerified")) {
                                    startActivity(new Intent(getActivity(), ActivityPost.class));
                                    getActivity().finish();
                                    Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(getActivity(), "Verify your Email", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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
}
