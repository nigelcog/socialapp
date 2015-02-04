package com.creativecub.socialapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.creativecub.socialapp.R;
import com.creativecub.socialapp.adapter.MySimpleArrayAdapter;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;


public class ActivityPost extends ActionBarActivity implements View.OnClickListener {

    // TextView tvPost;
    EditText etPost;
    ListView lvPost;
    Button btnPost;

    ArrayList<String> alPost;

    MySimpleArrayAdapter adapter;

    public static Bitmap bmGlobal = null;
    public static String fullNameGlobal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getSupportActionBar().setTitle("Write Post");

        etPost = (EditText) findViewById(R.id.etPost);
        btnPost = (Button) findViewById(R.id.btnPost);
        lvPost = (ListView) findViewById(R.id.lvPost);

        btnPost.setOnClickListener(this);

        alPost = new ArrayList<String>();

        ParseUser user = ParseUser.getCurrentUser();
        Log.d("name",user.getUsername());
        Log.d("email",user.getEmail());
        fullNameGlobal = user.get("full_name").toString();
       // Log.d("name",user.get());

        if(bmGlobal==null) {
            ParseFile file = (ParseFile) user.get("image");

            if (file != null) {
                file.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        if (e == null) {
                            // data has the bytes for the resume
                            Toast.makeText(getApplicationContext(), "Got image", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            bmGlobal = bitmap;

                        } else {
                            // something went wrong
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            } else {

                Toast.makeText(getApplicationContext(), "Image not saved", Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_logout) {
//            ParseUser.logOut();
//            startActivity(new Intent(this,ActivityRegisterLogin.class));
//            finish();
//            return true;
//        }
//        else

        if (id == R.id.action_settings) {

            startActivity(new Intent(getApplicationContext(), ActivityMyAccount.class));
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (etPost.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Post Cannot be Empty", Toast.LENGTH_SHORT).show();
        } else {
            alPost.add(etPost.getText().toString());

            etPost.setText("");
            Toast.makeText(getApplicationContext(), "Post Added", Toast.LENGTH_SHORT).show();

            ArrayList<String> alRev = new ArrayList<String>();
            for(int i=alPost.size();i>=1;i--){

            alRev.add(alPost.get(i-1));

            }
            adapter = new MySimpleArrayAdapter(getApplicationContext(), alRev);
            lvPost.setAdapter(adapter);

            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(etPost.getWindowToken(), 0);


        }


    }
}
