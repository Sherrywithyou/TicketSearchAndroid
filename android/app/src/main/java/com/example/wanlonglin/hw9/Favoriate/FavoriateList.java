package com.example.wanlonglin.hw9.Favoriate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.wanlonglin.hw9.EventDetailActivity;
import com.example.wanlonglin.hw9.EventList.EventList;
import com.example.wanlonglin.hw9.EventList.EventListAdapter;
import com.example.wanlonglin.hw9.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriateList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriateList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriateList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView eventListView;
    private String choosenJsonObjectString;
    private EventList eventList;

    public FavoriateList() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriateList.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriateList newInstance(String param1, String param2) {
        FavoriateList fragment = new FavoriateList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_event_list, container, false);
        view.findViewById(R.id.linlaHeaderProgress).setVisibility(View.GONE);
        getList(view);
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

    private void setAdapter(final View veiw, final EventList eventListArr) {
        eventListView = (RecyclerView) veiw.findViewById(R.id.eventListView);
        eventListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventListView.addItemDecoration(new DividerItemDecoration(eventListView.getContext(), DividerItemDecoration.VERTICAL));
        eventListView.setAdapter(new EventListAdapter(getActivity(), eventListArr, new EventListAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(final int pos) {
                if (pos == -1) {
                    veiw.findViewById(R.id.noResult).setVisibility(View.VISIBLE);
                    return;
                }
                try {
                    JSONObject choosenJsonObj = eventListArr.getChoosenJsonObject(pos);
                    choosenJsonObjectString = eventListArr.getChoosenJsonObjectString(pos);
                    final JSONArray artistsArr = eventListArr.getArtistsArr(pos);
                    Log.d("kkkk", artistsArr.toString());
                    final String venue = eventListArr.getVenue(pos);
                    Intent intent2 = new Intent(getActivity(), EventDetailActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("choosenEvent", choosenJsonObjectString);
                    bundle2.putString("eventId", choosenJsonObj.getString("eventId"));
                    bundle2.putString("artistsArr", artistsArr.toString());
                    bundle2.putString("venue", venue);
                    bundle2.putString("segment", eventListArr.getSegment(pos));
                    intent2.putExtras(bundle2);
                    startActivity(intent2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 1));
    }

    private void getList(View view) {
        SharedPreferences prefs = getActivity().getSharedPreferences("app", MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        JSONArray favoriateList = new JSONArray();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String itemObjectString = entry.getValue().toString();
            JSONObject itemObject = null;
            try {
                itemObject = new JSONObject(itemObjectString);
                favoriateList.put(itemObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        eventList = new EventList(favoriateList);
        if (eventList.length() == 0) {
            view.findViewById(R.id.noResult).setVisibility(View.VISIBLE);
        }
        setAdapter(view, eventList);
    }
}