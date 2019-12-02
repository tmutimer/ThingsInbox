package com.android.example.thingsinbox;

import org.json.JSONObject;

public abstract class ThingsItem {
    public abstract JSONObject getJSONObject();

    public String getUrlString() {
        JSONObject titledToDo = getJSONObject();
        return MainActivity.URL_PREPEND + titledToDo.toString()
                .replace(" ", "%20")
                .replace("{","%7B")
                .replace("}", "%7D")
                .replace("\"","%22")
                + "%5D"; // "]";
    }
}
