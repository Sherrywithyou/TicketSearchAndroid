package com.example.wanlonglin.hw9.Tab1;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.example.wanlonglin.hw9.HttpRequest;
import com.example.wanlonglin.hw9.R;
import org.json.JSONArray;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private JSONArray eventDetailJsonArr = null;

    private OnFragmentInteractionListener mListener;
    private String eventId;
    private ProgressDialog nDialog;
    private RecyclerView artistsListView;

    public EventFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TabOnCreate1", "Tab1onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        eventId = getArguments().getString("tab1");
        getJson(eventId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("TabOnCreateView", "Tab1onCreateView");
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        if (eventDetailJsonArr != null) {
            updateView(view);
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

    private void updateView(View view) {
        view.findViewById(R.id.linlaHeaderProgress).setVisibility(View.GONE);
        EventDetail eventDetail = new EventDetail(eventDetailJsonArr);
        String artName = eventDetail.getArtName();
        if (!artName.equals("")) {
            ((TextView)view.findViewById(R.id.artistValue)).setText(artName);
            ((LinearLayout)view.findViewById(R.id.artist)).setVisibility(View.VISIBLE);
        }

        String venue = eventDetail.getVenue();
        if (!venue.equals("")) {
            ((TextView)view.findViewById(R.id.venueValue)).setText(venue);
            ((LinearLayout) view.findViewById(R.id.venue)).setVisibility(View.VISIBLE);
        }


        String time = eventDetail.getTime();
        if (!time.equals("")) {
            ((TextView)view.findViewById(R.id.timeValue)).setText(time);
            ((LinearLayout) view.findViewById(R.id.time)).setVisibility(View.VISIBLE);
        }

        String category = eventDetail.getCategory();
        if (!category.equals("")) {
            ((TextView)view.findViewById(R.id.categoryValue)).setText(category);
            ((LinearLayout) view.findViewById(R.id.category)).setVisibility(View.VISIBLE);
        }

        String priceRange = eventDetail.getPriceRangeValue();
        if (!priceRange.equals("")) {
            ((TextView)view.findViewById(R.id.priceRangeValue)).setText(priceRange);
            ((LinearLayout) view.findViewById(R.id.priceRange)).setVisibility(View.VISIBLE);
        }

        String ticket = eventDetail.getTicket();
        if (!ticket.equals("")) {
            ((TextView)view.findViewById(R.id.ticketValue)).setText(ticket);
            ((LinearLayout) view.findViewById(R.id.ticket)).setVisibility(View.VISIBLE);
        }

        String buy = eventDetail.getBuy();
        if (!buy.equals("")) {
            String buyTicketUrl = "<a href=\"" +  buy + "\">TicketMaster</a>";
            ((TextView)view.findViewById(R.id.buyValue)).setText(Html.fromHtml(buyTicketUrl));
            ((TextView)view.findViewById(R.id.buyValue)).setMovementMethod(LinkMovementMethod.getInstance());
            ((LinearLayout) view.findViewById(R.id.buy)).setVisibility(View.VISIBLE);
        }

        String map = eventDetail.getMap();
        if (!map.equals("")) {
            String seatMapUrl = "<a href=\"" +  map + "\">View Here</a>";
            ((TextView)view.findViewById(R.id.seatMapValue)).setText(Html.fromHtml(seatMapUrl));
            ((TextView)view.findViewById(R.id.seatMapValue)).setMovementMethod(LinkMovementMethod.getInstance());
            ((LinearLayout) view.findViewById(R.id.seatMap)).setVisibility(View.VISIBLE);
        }
    }

    private void getJson(String eventId) {
        String url = "http://hw9ticketmaster.us-east-2.elasticbeanstalk.com/getEventDetailApi?";
        url = url + "eventId=" + eventId;
        HttpRequest httpRequest = new HttpRequest(url, getActivity(), new HttpRequest.getJson() {
            @Override
            public void getJsonArr(JSONArray response) {
                eventDetailJsonArr = response;
                updateView(getView());
            }
            @Override
            public void onError(VolleyError error) {
                Log.d("httpRequestError", error.toString());
            }
        });
        httpRequest.getJson();
    }
}
