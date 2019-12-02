package com.android.example.thingsinbox;

import org.json.JSONException;
import org.json.JSONObject;

public class Heading extends ThingsItem {

    private String mTitle;


    @Override
    public JSONObject getJSONObject() {
        JSONObject headingJson = new JSONObject();
        JSONObject attributes = new JSONObject();

        try {
            attributes.put("title", mTitle);
            headingJson.put("type", "heading")
                    .put("attributes", attributes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return headingJson;
    }
}
