package com.example.wanlonglin.hw9.Tab2;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.example.wanlonglin.hw9.HttpRequest;
import com.example.wanlonglin.hw9.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArtistsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArtistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int index;
    private JSONArray eventDetailJsonArr;
    private JSONObject[] picsJsonObjectArr;
    private JSONArray picsJsonArr;
    private String picsJsonArrString;
    private boolean isJsonBack = false;
    private String eventDetailJsonArrString;

    private OnFragmentInteractionListener mListener;
    private int index1;
    private int index2;
    private boolean isDone1 = false;
    private boolean isDone2 = false;
    private String musicJsonArrString;
    private JSONObject[] musicJsonObjectArr;
    private JSONArray musicJsonArr;
    private String musicJsonArvrString;
    private String artistsDetail;
    private RecyclerView artistsListView;
    private String artistsArrString;
    private JSONArray artistTeamArr;
    private String segment;
    private ProgressDialog nDialog;
    private PicList picList;
    private MusicList musicList;

    public ArtistsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistsFragment newInstance(String param1, String param2) {
        ArtistsFragment fragment = new ArtistsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void setAdapter(View view, PicList picList, MusicList musicList) {
        artistsListView = (RecyclerView) view.findViewById(R.id.tab2ListView);
        artistsListView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        artistsListView.addItemDecoration(new DividerItemDecoration(artistsListView.getContext(), DividerItemDecoration.VERTICAL));
        artistsListView.setAdapter(new ArtistsAdapter(getActivity(), picList, musicList));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        artistsArrString = getArguments().getString("tab2");
        segment = getArguments().getString("tab2_segment");
        picList = null;
        musicList = null;

        Log.d("TabOnCreate2", artistsArrString);
        Log.d("TabOnCreate2", segment);
        try {
            artistTeamArr = new JSONArray(artistsArrString);
            if (artistTeamArr.length() == 0) {
                isDone1 = true;
                isDone2 = true;
            } else {
                getArtistsJson(artistTeamArr);
                Log.d("lkj", segment);
                if (segment.equals("Music")) {
                    getMusicJson(artistTeamArr);
                } else {
                    isDone2 = true;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        if (isDone1 && isDone2) {
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


    private void getArtistsJson(JSONArray artistTeamArr) {
        picsJsonArrString = "123";
        try {
            picsJsonObjectArr = new JSONObject[artistTeamArr.length() > 2 ? 2 : artistTeamArr.length()];
            index1 = 0;
            for (int i = 0; i < picsJsonObjectArr.length; i++) {
//                String url = "http://10.0.2.2:8081/getArtistTeamsApi?";
                String url = "http://hw9ticketmaster.us-east-2.elasticbeanstalk.com/getArtistTeamsApi?";
                String name = artistTeamArr.getString(i);
                url += "name=" + URLEncoder.encode(name);
                Log.d("URL", Integer.toString(i));
                final int finalI = i;

                HttpRequest httpRequest2 = new HttpRequest(url, getActivity(), new HttpRequest.getJson() {
                    @Override
                    public void getJsonArr(JSONArray response) {
                        try {
                            picsJsonObjectArr[finalI] = response.getJSONObject(0);
                            picsJsonArr = new JSONArray(Arrays.asList(picsJsonObjectArr));
                            picsJsonArrString = picsJsonArr.toString();
                            index1++;
                            Log.d("lkj1", String.valueOf(index1));
                            if (index1 == picsJsonObjectArr.length) {
                                isDone1 = true;
                                picsJsonArr = new JSONArray(Arrays.asList(picsJsonObjectArr));
                                picsJsonArrString = picsJsonArr.toString();
                                picList = new PicList(picsJsonArr);
                                if (isDone1 && isDone2) {

                                    update(getView());
                                }
                            }
                        } catch (JSONException e) {
                            index1++;
                            if (index1 == picsJsonObjectArr.length) {
                                isDone1 = true;
                                picsJsonArr = new JSONArray(Arrays.asList(picsJsonObjectArr));
                                picsJsonArrString = picsJsonArr.toString();
                                picList = new PicList(picsJsonArr);
                                if (isDone1 && isDone2) {

                                    update(getView());
                                }
                            }
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(VolleyError error) {
                        Log.d("httpRequestError", error.toString());
                    }
                });
                httpRequest2.getJson();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getMusicJson(JSONArray artistTeamArr) {
        musicJsonArrString = "321";
        try {
            musicJsonObjectArr = new JSONObject[artistTeamArr.length() > 2 ? 2 : artistTeamArr.length()];
            index2 = 0;
            for (int i = 0; i < musicJsonObjectArr.length; i++) {
                String url = "http://hw9ticketmaster.us-east-2.elasticbeanstalk.com/spotifyApi?";
                String name = artistTeamArr.getString(i);
                url += "name=" + URLEncoder.encode(name);
                final int finalI = i;

                HttpRequest httpRequest2 = new HttpRequest(url, getActivity(), new HttpRequest.getJson() {
                    @Override
                    public void getJsonArr(JSONArray response) {
                        try {
                            musicJsonObjectArr[finalI] = response.getJSONObject(0);
                            index2++;
                            Log.d("lkj2", String.valueOf(index2));
                            if (index2 == musicJsonObjectArr.length) {
                                isDone2 = true;
                                musicJsonArr = new JSONArray(Arrays.asList(musicJsonObjectArr));
                                musicJsonArvrString = musicJsonArr.toString();
                                musicList = new MusicList(musicJsonArr);
                                if (isDone1 && isDone2) {
                                    update(getView());
                                }
                            }
                        } catch (JSONException e) {
                            index2++;
                            Log.d("lkj2.2", String.valueOf(index2));
                            if (index2 == musicJsonObjectArr.length) {
                                isDone2 = true;
                                musicJsonArr = new JSONArray(Arrays.asList(musicJsonObjectArr));
                                musicJsonArvrString = musicJsonArr.toString();
                                musicList = new MusicList(musicJsonArr);
                                if (isDone1 && isDone2) {
                                    update(getView());
                                }
                            }
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(VolleyError error) {
                        index2++;
                        if (index2 == musicJsonObjectArr.length) {
                            isDone2 = true;
                            musicList = null;
                            if (isDone1 && isDone2) {
                                update(getView());
                            }
                        }
                        Log.d("lkj2.3", String.valueOf(index2));
                        Log.d("lkj2.3", error.toString());
                    }
                });
                httpRequest2.getJson();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void update(View view) {
        view.findViewById(R.id.linlaHeaderProgress).setVisibility(View.GONE);
        if (artistTeamArr.length() == 0) {
            view.findViewById(R.id.noResult).setVisibility(View.VISIBLE);
        }
        if (isDone1 && isDone2) {
            setAdapter(view, picList, musicList);
        }
    }

    private void showProgressBar() {
        nDialog = new ProgressDialog(getActivity());
        nDialog.setMessage("Searching Events..");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }
}