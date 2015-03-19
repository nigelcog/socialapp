package com.creativecub.socialapp.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.creativecub.socialapp.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRegister extends Fragment implements View.OnClickListener{


    EditText fullname,emailid,password,repassword;
    Button btnregister;
    RadioGroup radioGroup;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressDialog progress;

    public FragmentRegister() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register, container, false);

        progress = new ProgressDialog(getActivity());
        progress.setTitle("Please wait..");
        progress.setMessage("Registering");
        progress.setCancelable(true);


        fullname = (EditText) root.findViewById(R.id.fullname);
        emailid = (EditText) root.findViewById(R.id.emailid);
        password = (EditText) root.findViewById(R.id.password);
        repassword = (EditText) root.findViewById(R.id.repassword);
        radioGroup =(RadioGroup) root.findViewById(R.id.rg);

        btnregister = (Button)root.findViewById(R.id.btnregister);
        btnregister.setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences("Social App", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        return root;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnregister:
                if(getWhoAmI()!=null && getFullname()!=null && getEmailId()!=null && getPassword()!=null)
                {


                    progress.show();



                    SharedPreferences preferences = getActivity().getSharedPreferences("default", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    this.editor.putString("email",getEmailId());
                    this.editor.commit();

                    editor.putString("fullname",getFullname());
                    editor.commit();

                    ParseUser user = new ParseUser();
                    user.put("full_name",getFullname());
                    user.setUsername(getEmailId());
                    user.setPassword(getPassword());
                    user.setEmail(getEmailId());

// other fields can be set just like with ParseObject
                    user.put("iam", getWhoAmI());

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now.
                              //  Toast.makeText(getActivity(), "Please verify your email. Then Login", Toast.LENGTH_LONG).show();

                                progress.dismiss();
                                showOkDialog();
                                //startActivity(new Intent(getActivity(),ActivityMyAccount.class));
                                //Toast.makeText(getActivity(),"Thanks for registering to us",Toast.LENGTH_LONG).show();
                                //getActivity().finish();
                                getFragmentManager().popBackStackImmediate();
                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                progress.dismiss();
                                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                break;
        }

    }

    public void showOkDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please verify your email and then Login. \n\nWe have sent you an email with the verification link.")
                .setTitle("Registration Successful!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    public String getWhoAmI()
    {
        int id = radioGroup.getCheckedRadioButtonId();
        if (id == -1){
            Toast.makeText(getActivity(),"Choose I am student/professor",Toast.LENGTH_LONG).show();
            return null;
        }
        else{
            if (id == R.id.rstudent){
                return "Student";
            }
            else if (id == R.id.rprofessor)
            {
                return "Professor";
            }
            else
            {
                Toast.makeText(getActivity(),"Enter correct email id",Toast.LENGTH_LONG).show();
                return null;
            }
        }
    }

    public String getFullname()
    {
        if(!fullname.getText().toString().trim().equals("") && fullname.getText().toString()!=null)
        {
            return fullname.getText().toString();
        }
        else
        {
            Toast.makeText(getActivity(),"Enter full name",Toast.LENGTH_LONG).show();
        }
        return null;
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
                Toast.makeText(getActivity(),"Enter correct email id",Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }

    public String getPassword()
    {
        if(!password.getText().toString().trim().equals("") && password.getText().toString()!=null)
        {
            if(!repassword.getText().toString().trim().equals("") && repassword.getText().toString()!=null)
            {
                if(password.getText().toString().equals(repassword.getText().toString()))
                {
                    return password.getText().toString();
                }
                else
                {
                    Toast.makeText(getActivity(),"Both Password don\'t match",Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getActivity(),"Re-Enter Password",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(),"Enter Password",Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
