package com.deltau.things.thingsinbox;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class ThingsItem {

    public abstract JSONObject getJSONObject();

    public String getUrlString() throws UnsupportedEncodingException {
        JSONObject titledToDo = getJSONObject();
        return MainActivity.URL_PREPEND
                + URLEncoder.encode(titledToDo.toString(), "utf-8").replace("+", "%20")
                + "%5D"; // "]";
    }
}
