package com.android.example.thingsinbox;

import java.util.ArrayList;

public class Project {
    private String mTitle;
    private String mNotes;
    private String mDate;
    private String mDeadline;
    private String mArea;
    private String[] mTags;
    private ArrayList<ToDo> mProjectItems; //TODO make To-Do extend something like "ProjectItem", as well as Heading, update type here to be ProjectItem

    public static class Builder {
        private String mTitle = "";
        private String mNotes = "";
        private String mDate = "";
        private String mDeadline = "";
        private String[] mTags = {""};
        private ArrayList<ToDo> mProjectItems;
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

        public Builder addProjectItem(ToDo projectItem) {
            mProjectItems.add(projectItem);
            return this;
        }

        public Project build() {
            return new Project(mTitle, mNotes, mDate, mDeadline, mProjectItems, mTags, mArea);
        }

    }

    private Project(String title, String notes, String date, String deadline
            , ArrayList<ToDo> projectItems, String[] tags, String area) {
        mTitle = title;
        mNotes = notes;
        mDate = date;
        mDeadline = deadline;
        mProjectItems = projectItems;
        mTags = tags;
        mArea = area;
    }

}
