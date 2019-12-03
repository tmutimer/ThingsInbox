package com.android.example.thingsinbox;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//TODO Add project support later on, if needed, but for now, do not work further on implementing.
public class Project extends ThingsItem {
    private String mTitle;
    private String mNotes;
    private String mDate;
    private String mDeadline;
    private String mArea;
    private String[] mTags;
    private ArrayList<ThingsItem> mProjectItems;

    public static class Builder {
        private String mTitle = "";
        private String mNotes = "";
        private String mDate = "";
        private String mDeadline = "";
        private String[] mTags = {""};
        private ArrayList<ThingsItem> mProjectItems;
        private String mArea = "";

        public Builder title(String title) {
            mTitle = title;
            return this;
        }

        public Builder notes(String notes) {
            mNotes = notes;
            return this;
        }

        public Builder whenString(String dateOption) {
            mDate = dateOption;
            return this;
        }

        public Builder deadline(String deadline) {
            mDeadline = deadline;
            return this;
        }

        public Builder tags(String[] tags) {
            mTags = tags;
            return this;
        }

        public Builder area(String area) {
            mArea = area;
            return this;
        }

        public Builder addProjectItem(ThingsItem projectItem) {
            mProjectItems.add(projectItem);
            return this;
        }

        public Project build() {
            return new Project(mTitle, mNotes, mDate, mDeadline, mProjectItems, mTags, mArea);
        }

    }

    private Project(String title, String notes, String date, String deadline
            , ArrayList<ThingsItem> projectItems, String[] tags, String area) {
        mTitle = title;
        mNotes = notes;
        mDate = date;
        mDeadline = deadline;
        mProjectItems = projectItems;
        mTags = tags;
        mArea = area;
    }

    //TODO this can be massively simplified by using Gson.toJson(this). However, need to structure Project class to match properly.
    @Override
    public JSONObject getJSONObject() {
        Gson gson = new Gson();
        JSONObject projectJson = new JSONObject();
        JSONObject attributes = new JSONObject();

        try {
            attributes.put("title", mTitle)
                    .put("notes", mNotes);

            if (!(mDate.equals(""))) {
                attributes.put("when", mDate);
            }

            if (!(mDeadline.equals(""))) {
                attributes.put("deadline", mDeadline);
            }

            if (!(mTags.length == 1 && (mTags[0].equals("")))) {
                attributes.put("tags", gson.toJson(mTags));
            }

            if (!(mArea.equals(""))) {
                attributes.put("area", mArea);
            }

            if (!(mProjectItems.isEmpty())) {
                //Construct a JSONArray of Headings and To-Do JSONObjects
                JSONArray items = new JSONArray();
                for (ThingsItem item : mProjectItems) {
                    items.put(item.getJSONObject());
                }
                attributes.put("items", items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return projectJson;
    }

}
