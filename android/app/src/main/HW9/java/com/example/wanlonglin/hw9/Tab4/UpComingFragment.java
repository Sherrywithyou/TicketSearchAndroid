package com.example.wanlonglin.hw9.Tab4;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.android.volley.VolleyError;
import com.example.wanlonglin.hw9.HttpRequest;
import com.example.wanlonglin.hw9.R;

import org.json.JSONArray;

import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpComingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpComingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpComingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String upComingJsonArrString;
    private JSONArray eventDetailJsonArr;
    private OnFragmentInteractionListener mListener;
    private String venue;
    private String venueJsonArrString;
    private ProgressDialog nDialog;
    private RecyclerView upComingListView;
    private UpComingList upComingList = null;
    private UpComingList originalComingList;
    private UpComingList choosenUpComingList;
    private UpComingList sortedUpComingList;
    private Spinner leftDropdown;
    private Spinner rightDropdown;

    public UpComingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpComingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpComingFragment newInstance(String param1, String param2) {
        UpComingFragment fragment = new UpComingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TabOnCreate", "Tab4onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        venue = getArguments().getString("tab4");
        upComingList = null;
        choosenUpComingList = null;
        originalComingList = null;
        getVenue(venue);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("TabOnCreateView", "Tab4onCreateView");
        final View view = inflater.inflate(R.layout.fragment_up_coming, container, false);
        if (upComingList != null) {
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

    public void update(final View view_out) {
        view_out.findViewById(R.id.linlaHeaderProgress).setVisibility(View.GONE);
//        ((TextView)view.findViewById(R.id.upcoming)).setText(venueJsonArrString);
        leftDropdown = view_out.findViewById(R.id.leftSpinner);
        String[] categoryLists = new String[]{"Default", "Event Name", "Time", "Artist", "Type"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, categoryLists);
        leftDropdown.setAdapter(adapter);
        leftDropdown.setEnabled(false);
        rightDropdown = view_out.findViewById(R.id.rightSpinner);
        String[] unitLists = new String[]{"Ascending", "Descending"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, unitLists);
        rightDropdown.setAdapter(adapter2);
        rightDropdown.setEnabled(false);

        if (upComingList != null) {
            leftDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        choosenUpComingList = upComingList.sort(position, rightDropdown.getSelectedItemPosition());
                        rightDropdown.setEnabled(true);
                    } else {
                        choosenUpComingList = new UpComingList(originalComingList);
                        rightDropdown.setEnabled(false);
                    }

                    setAdapter(view_out);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            rightDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (leftDropdown.getSelectedItemPosition() != 0) {
                        choosenUpComingList = upComingList.sort(leftDropdown.getSelectedItemPosition(), position);
                        setAdapter(view_out);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if (upComingList.getLength() == 0) {
                view_out.findViewById(R.id.noResult).setVisibility(View.VISIBLE);
            } else {
                leftDropdown.setEnabled(true);
            }
        }
    }

    //get venue json
    private void getVenue(String venue) {
        upComingJsonArrString = "";
        String keyWord = venue;
        String url = "http://hw9ticketmaster.us-east-2.elasticbeanstalk.com/getSongkickDetailApi?";
        url += "query=" + URLEncoder.encode(keyWord);
        Log.d("URLppp",url);
        HttpRequest httpRequest2 = new HttpRequest(url, getActivity(), new HttpRequest.getJson() {
            @Override
            public void getJsonArr(JSONArray response) {
                upComingJsonArrString = response.toString();
                upComingList = new UpComingList(response);
                originalComingList = new UpComingList(upComingList);
                choosenUpComingList = new UpComingList(upComingList);
                update(getView());
            }
            @Override
            public void onError(VolleyError error) {
                Log.d("httpRequestError", error.toString());
            }
        });
        httpRequest2.getJson();
    }

    private void setAdapter(View view) {
        upComingListView = (RecyclerView) view.findViewById(R.id.upcomingListView);
        upComingListView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        upComingListView.setAdapter(new UpComingAdapter(getActivity(), choosenUpComingList));
    }
}