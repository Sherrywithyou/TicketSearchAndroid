package com.example.wanlonglin.hw9.Tab1;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDetail {

    private JSONArray eventDetaiArr;

    public EventDetail(JSONArray eventDetaiArr) {
        this.eventDetaiArr = eventDetaiArr;
    }

    public String getArtName() {
        String result = "";
        try {
            JSONArray artistArr = eventDetaiArr.getJSONObject(0).getJSONArray("artistTeam");
            for (int i = 0; i < artistArr.length(); i++) {
                result += artistArr.getJSONObject(i).getString("name") + " | ";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = result.substring(0, result.length() > 2 ? result.length() - 2 : result.length());
        return result;
    }

    public String getVenue() {
        String result = "";
        try {
            result = eventDetaiArr.getJSONObject(0).getString("venue");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String getTime() {
        String result = "";
        try {
            String localData = eventDetaiArr.getJSONObject(0).getString("localData");
            DateFormat originalFormat = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMMM dd, yyyy");
            try {
                Date date = originalFormat.parse(localData);
                String formattedDate = targetFormat.format(date);
                result += formattedDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            result += " " + eventDetaiArr.getJSONObject(0).getString("localTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

    }

    public String getCategory() {
        String result = "";
        try {
            result += eventDetaiArr.getJSONObject(0).getString("genre") + " | ";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            result += eventDetaiArr.getJSONObject(0).getString("segment") + " | ";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = result.length() > 0 ? result.substring(0, result.length() - 2) : "";
        return result;
    }

    public String getSegment() {
        String result = "";
        try {
            result += eventDetaiArr.getJSONObject(0).getString("segment");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getTicket() {
        String result = "";
        try {
            result += eventDetaiArr.getJSONObject(0).getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getBuy() {
        String result = "";
        try {
            result += eventDetaiArr.getJSONObject(0).getString("buyTicketAt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getMap() {
        String result = "";
        try {
            result += eventDetaiArr.getJSONObject(0).getString("seatMap");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getPriceRangeValue() {
        String result = "";
        try {
            String minString = eventDetaiArr.getJSONObject(0).getString("priceMin");
            float priceMin = Float.valueOf(minString);
            result += "$" + String.format("%.2f", priceMin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String maxString = eventDetaiArr.getJSONObject(0).getString("priceMax");
            float priceMax = Float.valueOf(maxString);
            result += "~ $" + String.format("%.2f", priceMax);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}