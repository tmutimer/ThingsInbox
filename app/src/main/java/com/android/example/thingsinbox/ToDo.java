package com.android.example.thingsinbox;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ToDo {

    private static final String TAG = ToDo.class.getSimpleName();

    private String mTitle;
    private String mNotes;
    private String mDate;
    private String mDeadline;
    private String[] mSubtasks;
    private String[] mTags;
    private String mList;

    public static class Builder {
        private String mTitle = "";
        private String mNotes = "";
        private String mDate = "";
        private String mDeadline = "";
        private String[] mSubtasks = {""};
        private String[] mTags = {""};
        private String mList = "";

        public Builder title(String title) { mTitle = title; return this; }

        public Builder notes(String notes) { mNotes = notes; return this; }

        public Builder whenString(String dateOption) { mDate = dateOption; return this;}

        public Builder subtasks(String[] subtasks) { mSubtasks = subtasks; return this;}

        public Builder deadline(String deadline) { mDeadline = deadline; return this; }

        public Builder tags(String[] tags) { mTags = tags; return this; }

        public Builder list(String projectOrArea) { mList = projectOrArea; return this;}

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

    private boolean hasDate() { return !(mDate.equals("inbox")); }

    private boolean hasDeadline() { return !(mDeadline.equals("")); }

    //TODO this may need changing after messing around with the views for this. Might not even be correct now.
    private boolean hasSubtasks() { return !(mSubtasks.length == 1 && (mSubtasks[0].equals(""))); }

    private boolean hasTags() { return !(mTags.length == 1 && (mTags[0].equals(""))); }

    private boolean hasList() { return !(mList.equals("")); }


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
    private String getUrlString() {
        JSONObject titledToDo = new JSONObject();
        JSONObject attributes = new JSONObject();
        try {

            //Process title and notes
            attributes.put("title", mTitle);
            attributes.put("notes", mNotes);

            if (hasDate())
                attributes.put("when", mDate);

            if (hasDeadline()) //todo determine if this if logic is necessary... if so, determine if cleaner code or shorter url is better...
                attributes.put("deadline", mDeadline);

            if (hasTags())
                attributes.put("tags", mTags); //fixme this will need to be processed like subtasks below, but for now hasTags is always false

            if(hasSubtasks()) {
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

            titledToDo
                    .put("type", "to-do")
                    .put("attributes", attributes);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return MainActivity.URL_PREPEND + titledToDo.toString()
                .replace(" ", "%20")
                .replace("{","%7B")
                .replace("}", "%7D")
                .replace("\"","%22")
                + "%5D"; // "]";
    }


}
