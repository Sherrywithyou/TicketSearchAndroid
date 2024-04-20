package com.example.wanlonglin.hw9.EventList;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EventList {
    private JSONArray eventlist;
    private Map<String, String> map;

    public EventList (JSONArray eventlist) {
        this.eventlist = eventlist;
        map = new HashMap<>();
        map.put("Music","http://csci571.com/hw/hw9/images/android/music_icon.png");
        map.put("Sports","http://csci571.com/hw/hw9/images/android/sport_icon.png");
        map.put("Arts & Theatre","http://csci571.com/hw/hw9/images/android/art_icon.png");
        map.put("Miscellaneous","http://csci571.com/hw/hw9/images/android/miscellaneous_icon.png");
        map.put("Film","http://csci571.com/hw/hw9/images/android/film_icon.png");
    }

    public void print () {
        for (int i = 0; i < eventlist.length(); i++) {
            try {
                JSONObject singleobject = eventlist.getJSONObject(i);
                Log.d(Integer.toString(i), singleobject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getEventId(int i) {
        String result= "";
        try {
            result = eventlist.getJSONObject(i).getString("eventId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

//    public String getChoosenJsonObjectString (int i) throws JSONException {
//        return eventlist.getJSONObject(i).toString();
//    }

    public JSONObject getChoosenJsonObject (int i) throws JSONException {
        return eventlist.getJSONObject(i);
    }


    public int length() {
        return eventlist.length();
    }

    public String getEventName(int i) {
        try {
            return eventlist.getJSONObject(i).getString("eventName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "undefined";
    }

    public String getVenue(int i) {
        try {
            return eventlist.getJSONObject(i).getString("venue");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "undefined";
    }

    public String getDate(int i) {
        String result = "";
        try {
            result += eventlist.getJSONObject(i).getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            result += " " + eventlist.getJSONObject(i).getString("localTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getSegmentPic(int i) {
        try {
            return map.get(eventlist.getJSONObject(i).getString("segment"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "undefined";
    }

    public String getSegment(int i) {
        try {
            return eventlist.getJSONObject(i).getString("segment");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "undefined";
    }



    public JSONArray getArtistsArr(int i) {
        try {
            return eventlist.getJSONObject(i).getJSONArray("artistArr");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] mStringArray = {};
        return new JSONArray(Arrays.asList(mStringArray));
    }

    public String toString() {
        return eventlist.toString();
    }

    public String getChoosenJsonObjectString(int i) {
        String result = "";
        try {
            result = eventlist.getJSONObject(i).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void remove(int i) {
        Log.d("rrrr1", eventlist.toString());
        eventlist.remove(i);
        Log.d("rrrr2", eventlist.toString());
    }

    public String getUrl(int i) {
        String result = "";
        try {
            result = eventlist.getJSONObject(i).getString("buyTicketAt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
