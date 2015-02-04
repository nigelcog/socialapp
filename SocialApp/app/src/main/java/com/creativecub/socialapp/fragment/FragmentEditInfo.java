package com.creativecub.socialapp.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.creativecub.socialapp.R;
import com.creativecub.socialapp.activity.ActivityPost;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEditInfo extends Fragment implements View.OnClickListener{

    Button btnSaveInfo;
    EditText etFullName, etEmail;
    RadioButton rbStudent, rbProfessor;
    ImageView ivEditProfilePic;

    private final int SELECT_PHOTO = 1;
    String imageName, imgType = "";
    Bitmap selectedImage = null;
    byte[] data;

    ParseUser user;

    public FragmentEditInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_edit, container, false);

        btnSaveInfo = (Button)rootView.findViewById(R.id.btnSaveInfo);
        etFullName = (EditText)rootView.findViewById(R.id.etFullName);
        etEmail = (EditText)rootView.findViewById(R.id.etEmail);
        rbStudent = (RadioButton)rootView.findViewById(R.id.rbStudent);
        rbProfessor = (RadioButton)rootView.findViewById(R.id.rbProfessor);
        ivEditProfilePic = (ImageView)rootView.findViewById(R.id.ivEditProfilePic);

        btnSaveInfo.setOnClickListener(this);
        ivEditProfilePic.setOnClickListener(this);
        rbStudent.setOnClickListener(this);
        rbProfessor.setOnClickListener(this);

        user = ParseUser.getCurrentUser();

        etFullName.setText(user.get("full_name").toString());
        etEmail.setText(user.getEmail());

        if (user.get("iam").toString().equals("Student")){

           rbStudent.setChecked(true);
        }
        else{
            rbProfessor.setChecked(true);
        }


        ParseFile file = (ParseFile)user.get("image");

        if(file != null){
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        // data has the bytes for the resume
                        Toast.makeText(getActivity(), "Got image", Toast.LENGTH_SHORT).show();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
                        ivEditProfilePic.setImageBitmap(bitmap);

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



        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.ivEditProfilePic:

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);

                break;

            case R.id.btnSaveInfo:



                if(etFullName.getText().toString().trim().equals("")){

                    Toast.makeText(getActivity(), "Please enter Full Name", Toast.LENGTH_SHORT).show();

                }
                else if(!getEmailId()){

                }
                else {

                    ActivityPost.fullNameGlobal = etFullName.getText().toString();

                    user.put("full_name", etFullName.getText().toString());
                    user.setEmail(etEmail.getText().toString());
                    user.setUsername(etEmail.getText().toString());
                    if(rbStudent.isChecked()){
                        user.put("iam","Student");
                    }
                    else{

                        user.put("iam","Professor");
                    }

                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now.
                                Toast.makeText(getActivity(), "Data saved", Toast.LENGTH_SHORT).show();
                                if(imgType.equals("")){
                                   getActivity().onBackPressed();
                                }


                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if(!imgType.equals("")){
                    new LongOperation().execute();
                }


                break;

            case R.id.rbStudent:

                rbStudent.setChecked(true);
                rbProfessor.setChecked(false);

                break;

            case R.id.rbProfessor:

                rbProfessor.setChecked(true);
                rbStudent.setChecked(false);


                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == getActivity().RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        ivEditProfilePic.setImageBitmap(selectedImage);

                        Cursor returnCursor =
                                getActivity().getContentResolver().query(imageUri, null, null, null, null);

                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        returnCursor.moveToFirst();
                        imageName = returnCursor.getString(nameIndex);

                        imgType = imageName.substring(imageName.length()-3);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    public class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            if(imgType.equalsIgnoreCase("jpg")){

                selectedImage.compress(Bitmap.CompressFormat.JPEG, 10, stream);
            }

            else
            {

                selectedImage.compress(Bitmap.CompressFormat.PNG, 10, stream);
            }

            data = stream.toByteArray();

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            saveImage();

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    public void saveImage(){

        ActivityPost.bmGlobal = selectedImage;

        final ParseFile file = new ParseFile("image.png", data);

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    // Hooray! Let them use the app now.
                    Toast.makeText(getActivity(), "Image saved", Toast.LENGTH_SHORT).show();
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("image", file);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {
                                // Hooray! Let them use the app now.
                                Toast.makeText(getActivity(), "ProfilePic saved", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();

                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    public Boolean getEmailId()
    {
        if(!etEmail.getText().toString().trim().equals("") && etEmail.getText().toString()!=null)
        {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
            {
                return true;
            }
            else
            {
                Toast.makeText(getActivity(),"Enter correct email id",Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }


}
