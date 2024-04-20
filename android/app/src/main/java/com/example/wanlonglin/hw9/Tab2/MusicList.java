package com.example.wanlonglin.hw9.Tab2;

import org.json.JSONArray;
import org.json.JSONException;
import java.text.DecimalFormat;

public class MusicList {
    private JSONArray jsonArray;
    public MusicList (JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String toString () {
        return jsonArray.toString();
    }

    public String getNmae (int i) {
        String result = "";
        try {
            result = jsonArray.getJSONObject(i).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getFollowers (int i) {
        String result = "";
        try {
            int number = Integer.valueOf(jsonArray.getJSONObject(i).getString("followers"));
            result = new DecimalFormat(",###").format(number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getPopularity (int i) {
        String result = "";
        try {
            result = jsonArray.getJSONObject(i).getString("popularity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getCheckAt (int i) {
        String result = "";
        try {
            result = jsonArray.getJSONObject(i).getString("checkAt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
