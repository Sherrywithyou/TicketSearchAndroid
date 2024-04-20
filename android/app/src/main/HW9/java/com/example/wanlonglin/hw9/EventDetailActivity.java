package com.example.wanlonglin.hw9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.wanlonglin.hw9.Tab1.EventFragment;
import com.example.wanlonglin.hw9.Tab2.ArtistsFragment;
import com.example.wanlonglin.hw9.Tab3.VenueFragment;
import com.example.wanlonglin.hw9.Tab4.UpComingFragment;
import org.json.JSONException;
import org.json.JSONObject;


public class EventDetailActivity extends AppCompatActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private JSONObject choosenEventJson;
    private String choosenEventJsonArrString;
    private Fragment fragment_tab1;
    private Fragment fragment_tab2;
    private Fragment fragment_tab3;
    private Fragment fragment_tab4;
    private Fragment currentFragment;
    private String artistsArrString;
    private String venue;
    private String eventId;
    private String segment;
    private String eventName;
    private String buyTicketAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ModeHolder.getInstance().setData(1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        fragment_tab1 = new EventFragment();
        fragment_tab2 = new ArtistsFragment();
        fragment_tab3 = new VenueFragment();
        fragment_tab4 = new UpComingFragment();

        JSONObject jsonObject = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            choosenEventJsonArrString = bundle.getString("choosenEvent");
            venue = bundle.getString("venue");
            eventId = bundle.getString("eventId");
            artistsArrString = bundle.getString("artistsArr");
            segment = bundle.getString("segment");
            try {
                choosenEventJson = new JSONObject(choosenEventJsonArrString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //set eventName as toolbar title
        try {
            buyTicketAt = choosenEventJson.getString("buyTicketAt");
            eventName = choosenEventJson.getString("eventName");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((Toolbar)findViewById(R.id.toolbar)).setTitle(eventName);
        ImageView twitter = (ImageView) findViewById(R.id.twitterPic);
        Glide.with(EventDetailActivity.this).load("http://csci571.com/hw/hw9/images/android/twitter_ic.png").into(twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "https://twitter.com/intent/tweet?text=Check out " +  eventName + " at " + venue +  " Website: " + buyTicketAt + " %23CSCI571EventSearch";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        final ImageView heart = (ImageView) findViewById(R.id.heart);
        heart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sharedPreference(eventId, choosenEventJsonArrString, heart);
            }
        });
        check(eventId, heart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        private static final String arg = "test";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, int test) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            args.putInt(arg, test);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            textView.setText(getString(R.string.section_format, getArguments().getInt(arg)));
            return rootView;
        }
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
        public Fragment getItem(final int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//             return PlaceholderFragment.newInstance(position + 1, position + 1);
            Bundle bundle = new Bundle();
            switch(position) {
                case 0:
                    bundle.putString("tab1",eventId);
                    currentFragment = fragment_tab1;
                    currentFragment.setArguments(bundle);
                    return currentFragment;
                case 1:
                    bundle.putString("tab2",artistsArrString);
                    bundle.putString("tab2_segment",segment);
                    currentFragment = fragment_tab2;
                    currentFragment.setArguments(bundle);
                    return currentFragment;

                case 2:
                    bundle.putString("tab3",venue);
                    currentFragment = fragment_tab3;
                    currentFragment.setArguments(bundle);
                    return currentFragment;
                case 3:
                    bundle.putString("tab4",venue);
                    currentFragment = fragment_tab4;
                    currentFragment.setArguments(bundle);
                    return currentFragment;
            }
            return currentFragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    private void sharedPreference(String eventId, String choosenJsonString, ImageView heart) {
        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        if (!prefs.contains(eventId)) {
            String text = eventName + "was added to favorites";
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = getSharedPreferences("app", MODE_PRIVATE).edit();
            editor.putString(eventId, choosenJsonString);
            editor.apply();
            heart.setImageResource(R.drawable.heart_fill_red);
        } else {
            String text = eventName + "was removed from favorites";
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = getSharedPreferences("app", MODE_PRIVATE).edit();
            editor.remove(eventId).commit();
            heart.setImageResource(R.drawable.heart_fill_white);
        }
    }

    private void check(String eventId, ImageView heart) {
        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        if (!prefs.contains(eventId)) {
            heart.setImageResource(R.drawable.heart_fill_white);
        } else {
            heart.setImageResource(R.drawable.heart_fill_red);
        }
    }
}
