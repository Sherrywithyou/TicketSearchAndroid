package com.example.wanlonglin.hw9.Form;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FormDataObject {
    private Map<String, String> map;

    public FormDataObject(String keyword, String category, String distance, String unit, String locationIndex, String location, String userLat, String userLon) {
        map = new HashMap();
        map.put("keyword", keyword);
        map.put("category", category);
        map.put("distance", distance.equals("") ? "10" : Long.valueOf(distance) > 19999 ? "19999" : distance);
        map.put("unit", unit.toLowerCase());
        map.put("locationIndex", locationIndex);
        map.put("location", location);
        map.put("userLat", userLat);
        map.put("userLon", userLon);
    }

    public void print () {
        Log.d("keyword",map.get("keyword"));
        Log.d("category",map.get("category"));
        Log.d("distance",map.get("distance"));
        Log.d("unit",map.get("unit"));
        Log.d("locationIndex", map.get("locationIndex"));
        Log.d("location",map.get("location"));
        Log.d("userLat",map.get("userLat"));
        Log.d("userLon",map.get("userLon"));
    }

    public String url() {
        String prefix = "http://hw9ticketmaster.us-east-2.elasticbeanstalk.com/eventSearchApi?";

        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                prefix += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return prefix.substring(0, prefix.length() - 1);
    }

}