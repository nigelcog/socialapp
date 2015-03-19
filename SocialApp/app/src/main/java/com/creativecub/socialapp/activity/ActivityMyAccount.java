package com.creativecub.socialapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.creativecub.socialapp.R;
import com.creativecub.socialapp.fragment.FragmentEditInfo;
import com.creativecub.socialapp.fragment.FragmentUser;
import com.parse.ParseUser;


public class ActivityMyAccount extends ActionBarActivity {

    public static int intflag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getSupportActionBar().setTitle("My Account");

        if (savedInstanceState == null) {

            /*if(ActivityMyAccount.intflag == 0) {
                ActivityMyAccount.intflag = 1;
            }*/

            if(ActivityMyAccount.intflag == 1) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.container_myaccount, new FragmentUser(), "user");
                ft.commit();
            }
            else if(ActivityMyAccount.intflag == 2){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.container_myaccount, new FragmentEditInfo(), "edit_info2");
                ft.commit();

            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_my_account, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            ParseUser.logOut();
            startActivity(new Intent(this, ActivityRegisterLogin.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
