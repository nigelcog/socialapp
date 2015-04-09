package com.creativecub.socialapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.creativecub.socialapp.R;
import com.creativecub.socialapp.adapter.SimpleAdapterFeed;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ActivityFeed extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    static int refreshFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        sharedPreferences = getSharedPreferences("Social App", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_feed, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            ActivityMyAccount.intflag = 1;
            startActivity(new Intent(getApplicationContext(), ActivityMyAccount.class));
            //finish();
            return true;
        }
        else if (id == R.id.action_post) {

            startActivity(new Intent(getApplicationContext(), ActivityPost.class));
            //finish();
            return true;
        }
        else if (id == R.id.action_refresh) {

            PlaceholderFragment placeholderFragment = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag(
                    "android:switcher:"+R.id.pager+":0");
            placeholderFragment.refresh();
            placeholderFragment = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag(
                    "android:switcher:"+R.id.pager+":1");
            placeholderFragment.refresh();

            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);

            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        ArrayList<String> alPosts;
        ArrayList<String> alName;
        ArrayList<ParseFile> alImage;
        ArrayList<Bitmap> alBitmap;

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        ListView listViewFeed;
        String category = "";

        ProgressDialog progress;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity_feed, container, false);
            Bundle bundle = this.getArguments();
            int i = bundle.getInt(ARG_SECTION_NUMBER);
           // Toast.makeText(getActivity(),"Position "+i,Toast.LENGTH_SHORT).show();

            sharedPreferences = getActivity().getSharedPreferences("Social App", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();

             listViewFeed = (ListView)rootView.findViewById(R.id.lvFragmentFeed);

            progress = new ProgressDialog(getActivity());
            progress.setTitle("Please wait..");
            progress.setMessage("Loading..");
            progress.setCancelable(true);

            if(i == 1){
                category = sharedPreferences.getString("local","");
                retrievePost(listViewFeed,category);
            }
            else if(i == 2){
                category = sharedPreferences.getString("global","");
                retrievePost(listViewFeed,category);
            }



            return rootView;
        }

        public void refresh(){

            retrievePost(listViewFeed,category);

        }

        @Override
        public void onResume() {
            super.onResume();

            if(refreshFlag == 1){

                retrievePost(listViewFeed,category);
                refreshFlag = 0;

            }

        }

        public void retrievePost(final ListView lvFeed, String category){

            progress.show();

            alPosts = new ArrayList<String>();
            alName = new ArrayList<String>();
            alImage = new ArrayList<ParseFile>();
            alBitmap = new ArrayList<Bitmap>();


// Create query for objects of type "Post"
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

            // Restrict to cases where the author is the current user.
            query.whereEqualTo("category", category);

            // Run the query
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> postList,
                                 ParseException e) {
                    if (e == null) {
                        // If there are results, update the list of posts
                        // and notify the adapter
                        //  posts.clear();
                        for (ParseObject post : postList) {
                            alPosts.add(post.getString("postContent"));
                            alName.add(post.getString("userName"));
                            alImage.add(post.getParseFile("image"));
                        }

                        if(alPosts.size() == 0){

                            Toast.makeText(getActivity(),"No feed. Add a new post.",Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Collections.reverse(alPosts);
                            Collections.reverse(alName);
                            Collections.reverse(alImage);
                        }

                        getImage(0);

                        //((ArrayAdapter<String>)getListAdapter()).notifyDataSetChanged();
                    } else {
                        Log.d("Post retrieval", "Error: " + e.getMessage());
                    }
                }

            });

        }

        public void getImage(int i)
        {
            alBitmap.add(null);
            ParseFile parseFile = (ParseFile) alImage.get(i);
            final int finalI = i;
            parseFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    // something went wrong
                    if (e == null) {
                        // data has the bytes for the resume
                        //  Toast.makeText(context, "Got image", Toast.LENGTH_SHORT).show();
                        Bitmap bitmap = compress(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                        alBitmap.add(finalI, bitmap);
                        if(finalI == alImage.size()-1)
                        {
                            SimpleAdapterFeed adapterFeed = new SimpleAdapterFeed(getActivity(),alPosts,alName,alBitmap);
                            listViewFeed.setAdapter(adapterFeed);
                            progress.dismiss();
                            return;
                        }
                        getImage(finalI+1);
                    } else {
                        alBitmap.add(finalI,null);
                        if(finalI == alPosts.size()-1)
                        {
                            SimpleAdapterFeed adapterFeed = new SimpleAdapterFeed(getActivity(),alPosts,alName,alBitmap);
                            listViewFeed.setAdapter(adapterFeed);
                            progress.dismiss();
                            return;
                        }
                        getImage(finalI+1);
                    }
                }
            });
        }
        public Bitmap compress(Bitmap b)
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inTempStorage = new byte[24*1024];
            options.inJustDecodeBounds = false;
            options.inSampleSize=32;
            return ThumbnailUtils.extractThumbnail(b, 50, 50);
        }
    }

}
