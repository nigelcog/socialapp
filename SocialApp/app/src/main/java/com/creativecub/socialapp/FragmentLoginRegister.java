package com.creativecub.socialapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentLoginRegister extends Fragment implements View.OnClickListener {


    Button login,register;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_login_register, container, false);
        login = (Button) root.findViewById(R.id.login);
        register = (Button) root.findViewById(R.id.register);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

        return root;
    }
    @Override
    public void onClick(View v) {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        switch (v.getId())
        {
            case R.id.login:
                ft.replace(R.id.container, new FragmentLogin(), "login");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.register:
                ft.replace(R.id.container, new FragmentRegister(), "register");
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }

}
