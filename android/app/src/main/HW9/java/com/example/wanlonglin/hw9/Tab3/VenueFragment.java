package com.example.wanlonglin.hw9.Tab3;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.example.wanlonglin.hw9.HttpRequest;
import com.example.wanlonglin.hw9.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VenueFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VenueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenueFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private JSONArray eventDetailJsonArr;
    private String keyWord;
    private JSONArray VenueJsonArr;

    private OnFragmentInteractionListener mListener;
    private String venueJsonArrString = "";
    private String venue;
    private ProgressDialog nDialog;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private VenueDetail venueDetail = null;



    public static VenueFragment newInstance(String keyword) {
        VenueFragment fragment = new VenueFragment();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        fragment.setArguments(bundle);
        return fragment;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VenueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VenueFragment newInstance(String param1, String param2) {
        VenueFragment fragment = new VenueFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        venue = getArguments().getString("tab3");
        getVenue(venue);
        Log.d("TabOnCreate3", venue);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("TabOnCreateView", "Tab3onCreateView");
        final View view = inflater.inflate(R.layout.fragment_venue, container, false);
        if (venueDetail != null) {
            update(view);
        }
        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //get venue json
    private void getVenue(String venue) {
        String keyWord = venue;
        String url = "http://hw9ticketmaster.us-east-2.elasticbeanstalk.com/getVenueDetailApi?";
        url += "keyword=" + URLEncoder.encode(keyWord);
        HttpRequest httpRequest2 = new HttpRequest(url, getActivity(), new HttpRequest.getJson() {
            @Override
            public void getJsonArr(JSONArray response) {
                venueJsonArrString = response.toString();
                update(getView());
            }
            @Override
            public void onError(VolleyError error) {
                Log.d("httpRequestError", error.toString());
            }
        });
        httpRequest2.getJson();
    }

    private void update(View view) {
        view.findViewById(R.id.googleLinear).setVisibility(View.VISIBLE);
        view.findViewById(R.id.linlaHeaderProgress).setVisibility(View.GONE);
        try {
            venueDetail = new VenueDetail(new JSONArray(venueJsonArrString));
            String name = venueDetail.getName();
            if (!name.equals("")) {
                ((LinearLayout)view.findViewById(R.id.name)).setVisibility(View.VISIBLE);
                ((TextView)view.findViewById(R.id.nameValue)).setText(name);
            }

            String address = venueDetail.getAddress();
            if (!address.equals("")) {
                ((LinearLayout)view.findViewById(R.id.address)).setVisibility(View.VISIBLE);
                ((TextView)view.findViewById(R.id.addressValue)).setText(address);
            }

            String city = venueDetail.getCity();
            if (!city.equals("")) {
                ((LinearLayout)view.findViewById(R.id.city)).setVisibility(View.VISIBLE);
                ((TextView)view.findViewById(R.id.cityValue)).setText(city);
            }

            String phone = venueDetail.getPhoneNumber();
            if (!phone.equals("")) {
                ((LinearLayout)view.findViewById(R.id.phone)).setVisibility(View.VISIBLE);
                ((TextView)view.findViewById(R.id.phoneValue)).setText(phone);
            }

            String openHours = venueDetail.getOpenHours();
            if (!openHours.equals("")) {
                ((LinearLayout)view.findViewById(R.id.openHours)).setVisibility(View.VISIBLE);
                ((TextView)view.findViewById(R.id.openHoursValue)).setText(openHours);
            }

            String generalRule = venueDetail.getGeneralRule();
            if (!generalRule.equals("")) {
                ((LinearLayout)view.findViewById(R.id.generalRule)).setVisibility(View.VISIBLE);
                ((TextView)view.findViewById(R.id.generalRuleValue)).setText(generalRule);
            }

            String childRule = venueDetail.getChildRule();
            if (!childRule.equals("")) {
                ((LinearLayout)view.findViewById(R.id.childRule)).setVisibility(View.VISIBLE);
                ((TextView)view.findViewById(R.id.childRuleValue)).setText(childRule);
            }

            mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    float lat = Float.valueOf(venueDetail.getLat());
                    float lon = Float.valueOf(venueDetail.getLon());
                    LatLng marker = new LatLng(lat, lon);
                    googleMap.addMarker(new MarkerOptions().position(marker));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(13.50f));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
