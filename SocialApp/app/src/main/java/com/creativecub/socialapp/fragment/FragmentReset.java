package com.creativecub.socialapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.creativecub.socialapp.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentReset extends Fragment {

    EditText emailid;
    Button reset;
    public FragmentReset() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fagment_reset, container, false);
        emailid = (EditText)root.findViewById(R.id.emailid);
        reset = (Button) root.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEmailId()!=null)
                {
                    ParseUser.requestPasswordResetInBackground(getEmailId(),
                            new RequestPasswordResetCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        // An email was successfully sent with reset instructions.
                                        Toast.makeText(getActivity(),"Password reset link sent",Toast.LENGTH_LONG).show();
                                        getFragmentManager().popBackStackImmediate();

                                    } else {
                                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
        return root;
    }

    public String getEmailId()
    {
        if(!emailid.getText().toString().trim().equals("") && emailid.getText().toString()!=null)
        {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailid.getText().toString()).matches())
            {
                return emailid.getText().toString();
            }
            else
            {
                Toast.makeText(getActivity(), "Enter correct email id", Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }
}
