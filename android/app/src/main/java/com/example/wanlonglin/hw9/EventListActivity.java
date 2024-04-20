package com.example.wanlonglin.hw9;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.example.wanlonglin.hw9.EventList.EventList;
import com.example.wanlonglin.hw9.EventList.EventListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EventListActivity extends AppCompatActivity {

    private RecyclerView eventListView;
    private String choosenJsonObjectString;
    private EventList eventList;
    private ProgressDialog nDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ModeHolder.getInstance().setData(0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        setTitle("Search Results");

        if (bundle != null) {
            String url = bundle.getString("url");
            getJson(url);
        }
    }

    @Override
    protected void onRestart() {
        ModeHolder.getInstance().setData(0);
        super.onRestart();
        setAdapter(eventList);
    }

    private void setAdapter(final EventList eventListArr) {
        eventListView = (RecyclerView) findViewById(R.id.eventListView);
        eventListView.setLayoutManager(new LinearLayoutManager(EventListActivity.this));
        eventListView.addItemDecoration(new DividerItemDecoration(eventListView.getContext(), DividerItemDecoration.VERTICAL));
        eventListView.setAdapter(new EventListAdapter(EventListActivity.this, eventListArr, new EventListAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int pos) {
                try {
                    JSONObject choosenJsonObj = eventListArr.getChoosenJsonObject(pos);
                    choosenJsonObjectString = eventListArr.getChoosenJsonObjectString(pos);
                    final JSONArray artistsArr = eventListArr.getArtistsArr(pos);
                    Log.d("kkkk", artistsArr.toString());
                    final String venue = eventListArr.getVenue(pos);
                    Intent intent2 = new Intent(EventListActivity.this, EventDetailActivity.class);
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
        },0));
    }

    private void getJson(String url) {
        HttpRequest httpRequest = new HttpRequest(url, this, new HttpRequest.getJson() {
            @Override
            public void getJsonArr(JSONArray response) {
                eventList = new EventList(response);
                findViewById(R.id.linlaHeaderProgress).setVisibility(View.GONE);
                Log.d("ppppp", response.toString());
                if (eventList.length() > 0) {
                    setAdapter(eventList);
                } else {
                    ((TextView)findViewById(R.id.noResult)).setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onError(VolleyError error) {
                findViewById(R.id.linlaHeaderProgress).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.noResult)).setText("Error");
                ((TextView)findViewById(R.id.noResult)).setVisibility(View.VISIBLE);
                Toast.makeText(EventListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.d("httpRequestError", error.toString());
            }
        });
        httpRequest.getJson();
    }
}