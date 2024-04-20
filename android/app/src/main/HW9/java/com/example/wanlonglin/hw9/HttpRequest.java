package com.example.wanlonglin.hw9;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;

public class HttpRequest extends AppCompatActivity {

    String url;
    Activity activity;
    getJson listener;

    public HttpRequest (String url, Activity activity, getJson listener) {
        this.url = url;
        this.activity = activity;
        this.listener = listener;
    }

    public void getJson() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity);
        Log.d("URL", url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        listener.getJsonArr(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error);
                        // TODO: Handle error
                        Log.d("Error: ", error.toString());
                    }
                });
        queue.add(jsonArrayRequest);
    }

    public interface getJson {
        void getJsonArr(JSONArray response);
        void onError(VolleyError error);
    }
}
