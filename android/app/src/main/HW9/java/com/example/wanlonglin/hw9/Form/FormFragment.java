package com.example.wanlonglin.hw9.Form;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.wanlonglin.hw9.ApiCall;
import com.example.wanlonglin.hw9.AutoSuggestAdapter;
import com.example.wanlonglin.hw9.EventListActivity;
import com.example.wanlonglin.hw9.R;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormFragment extends Fragment  implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private RadioGroup locationRadio;
    private Button searchButton;
    private Button clearButton;
    private int locationIndex = 0;
    private String category = "All";
    private String distance = "";
    private String userLat = "34.0266";
    private String userLon = "-118.283";
    private String unit = "Miles";
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 99;
    private LocationManager mLocationManager;

    public FormFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FormFragment newInstance(String param1, String param2) {
        FormFragment fragment = new FormFragment();
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
        location();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        
        final View view = inflater.inflate(R.layout.fragment_form, container, false);
        Spinner categoryDropdown = view.findViewById(R.id.categorySpinner);
        String[] categoryLists = new String[]{"All", "Music", "Sports", "Arts & Theatre", "Film", "Miscellaneous"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, categoryLists);
        categoryDropdown.setAdapter(adapter);

        Spinner unitDropdown = view.findViewById(R.id.unitSpinner);
        String[] unitLists = new String[]{"Miles", "Kilometers"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, unitLists);
        unitDropdown.setAdapter(adapter2);

        locationRadio = (RadioGroup) view.findViewById(R.id.radioGroup);
        locationRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                locationIndex = locationRadio.indexOfChild(group.findViewById(checkedId));
                EditText input = (EditText) view.findViewById(R.id.locationInput);
                if (locationIndex == 0) {
                    input.setText("");
                    input.setEnabled(false);
                } else {
                    input.setEnabled(true);
                    input.requestFocus();
                }
            }
        });

        searchButton = (Button) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInValid = false;
                EditText locationInput = (EditText) view.findViewById(R.id.locationInput);
                EditText keywordInput = (EditText) view.findViewById(R.id.keywordInout);
                TextView keywordErrorMsg = (TextView) view.findViewById(R.id.keywordError);
                String keyword = keywordInput.getText().toString().trim();
                if (keyword.equals("")) {
                    keywordErrorMsg.setVisibility(View.VISIBLE);
                    isInValid = true;
                } else {
                    keywordErrorMsg.setVisibility(View.GONE);
                }

                String location = locationInput.getText().toString().trim();
                TextView locationErrorMsg = (TextView) view.findViewById(R.id.locationError);
                if (locationIndex == 1 && location.equals("")) {
                    locationErrorMsg.setVisibility(View.VISIBLE);
                    isInValid = true;
                } else {
                    locationErrorMsg.setVisibility(View.GONE);
                }

                if (isInValid == true) {
                    Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_LONG).show();
                } else {
                    category = ((Spinner)view.findViewById(R.id.categorySpinner)).getSelectedItem().toString();
                    unit = ((Spinner)view.findViewById(R.id.unitSpinner)).getSelectedItem().toString();
                    distance = ((EditText)view.findViewById(R.id.distanceInout)).getText().toString();
                    location = ((EditText)view.findViewById(R.id.locationInput)).getText().toString();
                    FormDataObject formData = new FormDataObject(keyword, category, distance, unit, Integer.toString(locationIndex), location, userLat, userLon);
                    formData.print();
                    String url = formData.url();
                    Intent intent = new Intent(getActivity(), EventListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        clearButton = (Button) view.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView)view.findViewById(R.id.keywordError)).setVisibility(View.GONE);
                ((TextView)view.findViewById(R.id.locationError)).setVisibility(View.GONE);
                ((EditText)view.findViewById(R.id.keywordInout)).setText("");
                ((EditText)view.findViewById(R.id.distanceInout)).setText("");
                ((Spinner)view.findViewById(R.id.categorySpinner)).setSelection(0);
                ((Spinner)view.findViewById(R.id.unitSpinner)).setSelection(0);
                ((RadioGroup)view.findViewById(R.id.radioGroup)).check(R.id.location1);
                ((EditText)view.findViewById(R.id.keywordInout)).requestFocus();
            }
        });

        // Inflate the layout for this fragment
        autoComplete(view);
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

    private void autoComplete(View view) {
        final AppCompatAutoCompleteTextView autoCompleteTextView =
                view.findViewById(R.id.keywordInout);
        final TextView selectedText = view.findViewById(R.id.selected_item);

        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        selectedText.setText(autoSuggestAdapter.getObject(position));
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });
    }

    private void makeApiCall(String text) {
        ApiCall.make(getActivity(), text, new Response.Listener<String>() {



//            @Override
//            public void onResponse(String response) {
//                //parsing logic, please change it as per your requirement
//                List<String> stringList = new ArrayList<>();
//                try {
//                    JSONObject responseObject = new JSONObject(response);
//                    JSONArray array = responseObject.getJSONArray("results");
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject row = array.getJSONObject(i);
//                        stringList.add(row.getString("trackName"));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                //IMPORTANT: set data here and notify
//                autoSuggestAdapter.setData(stringList);
//                autoSuggestAdapter.notifyDataSetChanged();
//            }

            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        stringList.add(array.getString(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


    private void location() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("")
                        .setMessage("Allow Event Search to access this device's location?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
            }
        } else {
            mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                Log.d("locationSource", "GPS");
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            } else {
                boolean isNETWORKEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (isNETWORKEnabled) {
                    Log.d("locationSource", "network");
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        userLat = String.valueOf(location.getLatitude());
        userLon = String.valueOf(location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}