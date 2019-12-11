package com.android.example.thingsinbox;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ToDo extends ThingsItem {

    private static final String TAG = ToDo.class.getSimpleName();

    private String mTitle;
    private String mNotes;
    private String mDate;
    private String mDeadline;
    private String[] mSubtasks;
    private String[] mTags;
    private String mList;

    public static class Builder {
        private String mTitle;
        private String mNotes;
        private String mDate;
        private String mDeadline;
        private String[] mSubtasks;
        private String[] mTags;
        private String mList;

        //Builder methods make use of nulls so that Gson will exclude from Json

        public Builder title(String title) {
            mTitle = TextUtils.isEmpty(title) ? null : title;
            return this;
        }

        public Builder notes(String notes) {
            mNotes = TextUtils.isEmpty(notes) ? null : notes;
            return this;
        }

        public Builder when(String date) {
            mDate = TextUtils.isEmpty(date) ? null : date;
            return this;
        }

        public Builder subtasks(String[] subtasks) {
            mSubtasks = (subtasks.length == 1 && subtasks[0].equals("")) ? null : subtasks;
            return this;
        }

        public Builder deadline(String deadline) {
            mDeadline = TextUtils.isEmpty(deadline) ? null : deadline;
            return this;
        }

        public Builder tags(String[] tags) {
            mTags = (tags.length == 1 && tags[0].equals("")) ? null : tags;
            return this;
        }

        public Builder list(String projectOrArea) {
            mList = TextUtils.isEmpty(projectOrArea) ? null : projectOrArea;
            return this;
        }

        public ToDo build() {
            return new ToDo(mTitle, mNotes, mDate, mDeadline, mSubtasks, mTags, mList);
        }
    }

    private ToDo(String title, String notes, String date, String deadline, String[] subtasks, String[] tags, String list) {
        mTitle = title;
        mNotes = notes;
        mDate = date;
        mDeadline = deadline;
        mSubtasks = subtasks;
        mTags = tags;
        mList = list;
    }

    private boolean isMessageComplex() {
            return (hasDate() ||
                    hasDeadline() ||
                    hasSubtasks() ||
                    hasTags() ||
                    hasList());
    }

    private boolean hasDate() { return mDate != null; }

    private boolean hasDeadline() { return mDeadline != null; }

    private boolean hasSubtasks() { return mSubtasks != null; }

    private boolean hasTags() { return mTags != null; }

    private boolean hasList() { return mList != null; }


    /**
     * Sends the To-Do as an email, either directly (for simple notes), or with URL to open inside Things
     * (for complex notes).
     */
    public void send(Context context) {
        Message message = new Message();
        message.setRecipients(new String[] {MainActivity.sEmailAddress});

        if(isMessageComplex()) {
            String urlString = getUrlString();
            message.setSubject(String.format(context.getString(R.string.email_todo_subject), mDate)); //TODO this should probably be formatted better
            message.setBody(urlString);
        } else {
            message.setSubject(mTitle);
            message.setBody(mNotes);
        }

        message.sendMessageAsEmail(context);

    }

    //This could probably be much simplified using Gson.
    public JSONObject getJSONObject() {
        Gson gson = new Gson();
        JSONObject toDoJson = new JSONObject();
        JSONObject attributes = new JSONObject();
        try {

            //Process title and notes. 2000 limit means JSONObject should be kept to bare minimum, no shortcuts.

            if (mTitle != null) {
                attributes.put("title", mTitle);
            }
            if (mNotes != null)
                attributes.put("notes", mNotes);

            if (hasDate())
                attributes.put("when", mDate);

            if (hasDeadline())
                attributes.put("deadline", mDeadline);

            if (hasList())
                attributes.put("list", mList);

            if (hasTags()) {
                JSONArray tagsJsonArray = new JSONArray(mTags);
                attributes.put("tags", tagsJsonArray);
            }

            if (hasSubtasks()) {
                JSONArray checklistItems = new JSONArray();

                for(String subtask : mSubtasks) {
                    JSONObject checkAttributes = new JSONObject();
                    checkAttributes.put("title",subtask);
                    JSONObject item = new JSONObject();

                    item.put("type", "checklist-item");
                    item.put("attributes", checkAttributes);
                    checklistItems.put(item);
                }

                attributes.put("checklist-items",checklistItems);
            }

            toDoJson
                    .put("type", "to-do")
                    .put("attributes", attributes);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return toDoJson;
    }
}
