package com.android.example.thingsinbox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Message {
    protected String[] mRecipients;
    protected String mSubject;
    protected String mBody;
    public void setRecipients(String[] recipients) {
        mRecipients = recipients;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public void sendMessageAsEmail(Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, mRecipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
        intent.putExtra(Intent.EXTRA_TEXT, mBody);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }


//    public class complexToDo extends Message {
//        private String mNotes;
//        public void send(String[] recipients) {
//            String todoUrlString = parseThingsParametersToUrlString(mTitle);

//        }
}

