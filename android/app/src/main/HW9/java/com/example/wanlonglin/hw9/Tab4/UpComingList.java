package com.example.wanlonglin.hw9.Tab4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class UpComingList {
    private JSONArray jsonArray;
    public UpComingList(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public UpComingList(UpComingList another) {
        this.jsonArray = another.jsonArray; // you can access
    }

    public String getTitle(int i) {
        String result = "";
        try {
            result = jsonArray.getJSONObject(i).getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getUrl(int i) {
        String result = "";
        try {
            result = jsonArray.getJSONObject(i).getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getArtist(int i) {
        String result = "";
        try {
            result = jsonArray.getJSONObject(i).getString("artist");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String getTime(int i) {
        String result = "";
        try {
            result = jsonArray.getJSONObject(i).getString("time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getDate(int i) {
        String result = "";
        try {
            result = jsonArray.getJSONObject(i).getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String getShowTime(int i) {
        String result = "";
        try {
            String localData = jsonArray.getJSONObject(i).getString("date");
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
            result += " " + jsonArray.getJSONObject(i).getString("time");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getType(int i) {
        String result = "";
        try {
            result = jsonArray.getJSONObject(i).getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getLength() {
        return jsonArray.length();
    }

    public JSONObject[] getJSONObjectArr() {
        JSONObject[] result = new JSONObject[jsonArray.length()];
        for (int i = 0; i < result.length; i++) {
            try {
                result[i] = new JSONObject(jsonArray.getJSONObject(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    //parameter: 0 => eventname, 1 => time, 2 => artist, 3 => type
    //mode: 0 => ascending, 1=> descending
    public UpComingList sort(final int parameter, final int mode) {
        JSONObject[] before = getJSONObjectArr();
        Arrays.sort(before, new Comparator<JSONObject>() {
            public int compare(JSONObject obj1, JSONObject obj2) {
                try {
                    int index;
                    switch(parameter) {
                        case 1:
                            index = obj1.getString("title").compareTo(obj2.getString("title"));
                            return mode == 0 ? index : -index;
                        case 2:
                            index = obj1.getString("date").compareTo(obj2.getString("date"));
                            if (index == 0) {
                                index = obj1.getString("time").compareTo(obj2.getString("time"));
                            }
                            return mode == 0 ? index : -index;
                        case 3:
                            index = obj1.getString("artist").compareTo(obj2.getString("artist"));
                            return mode == 0 ? index : -index;
                        case 4:
                            index = obj1.getString("type").compareTo(obj2.getString("type"));
                            return mode == 0 ? index : -index;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return -1;
            }
        });
        try {
            jsonArray = new JSONArray(Arrays.asList(before).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new UpComingList(jsonArray);
    }

    public String toString() {
        return jsonArray.toString();
    }

}
