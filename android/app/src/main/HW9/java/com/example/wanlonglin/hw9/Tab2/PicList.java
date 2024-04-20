package com.example.wanlonglin.hw9.Tab2;

import org.json.JSONArray;
import org.json.JSONException;

public class PicList {
    private JSONArray jsonArray;
    public PicList(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String toString() {
        return jsonArray.toString();
    }

    public String getName(int i) {
        String result = "";
        try {
            result = jsonArray.getJSONObject(i).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String[] getUrlArr(int index) {
        String[] result = {};
        try {
            int len = jsonArray.getJSONObject(index).getJSONArray("url").length();
            result = new String[len];
            for (int i = 0; i < len; i++) {
                result[i] = jsonArray.getJSONObject(index).getJSONArray("url").getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getLength () {
        return jsonArray.length();
    }
}
