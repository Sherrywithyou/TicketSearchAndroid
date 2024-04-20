package com.example.wanlonglin.hw9.Tab3;

import org.json.JSONArray;
import org.json.JSONException;

public class VenueDetail {
    private JSONArray jsonArray;

    public VenueDetail(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getName () {
        String result = "";
        try {
            result = jsonArray.getJSONObject(0).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getAddress () {
        String result = "";
        try {
            result = jsonArray.getJSONObject(0).getString("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getCity () {
        String result = "";
        try {
            result = jsonArray.getJSONObject(0).getString("city") + ", ";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            result += jsonArray.getJSONObject(0).getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getGeneralRule () {
        String result = "";
        try {
            result = jsonArray.getJSONObject(0).getString("generalRule");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getChildRule () {
        String result = "";
        try {
            result = jsonArray.getJSONObject(0).getString("childRule");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getOpenHours() {
        String result = "";
        try {
            result = jsonArray.getJSONObject(0).getString("openHours");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getPhoneNumber() {
        String result = "";
        try {
            result = jsonArray.getJSONObject(0).getString("phoneNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getLon() {
        String result = "";
        try {
            result = jsonArray.getJSONObject(0).getString("lon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getLat() {
        String result = "";
        try {
            result = jsonArray.getJSONObject(0).getString("lat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
