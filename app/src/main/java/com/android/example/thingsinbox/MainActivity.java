package com.android.example.thingsinbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String URL_PREPEND = "things:///json?data=%5B";

    private EditText mToDoTitle;
    private EditText mToDoNotes;
    private EditText mToDoSubtasks;
    private Spinner mToDoDate;
    private TextView mToDoDeadline;
    private MultiAutoCompleteTextView mToDoTags;
    private AutoCompleteTextView mToDoList;
    private String mDateString;

    private ImageButton mSubtasksCancelButton;
    private ImageButton mDateCancelButton;
    private ImageButton mDeadlineCancelButton;
    private ImageButton mTagsCancelButton;
    private ImageButton mListCancelButton;

    private ImageButton mSubtasksButton;
    private ImageButton mDateButton;
    private ImageButton mDeadlineButton;
    private ImageButton mListButton;
    private ImageButton mTagsButton;

    private RelativeLayout mSubtasksLayout;
    private RelativeLayout mDateLayout;
    private RelativeLayout mDeadlineLayout;
    private RelativeLayout mListLayout;
    private RelativeLayout mTagsLayout;
    private FloatingActionButton mSendActionButton;

    private SharedPreferences mPreferences;
    private String mEmailAddress;
    private String[] mTagArray;
    private String[] mListArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        updateFromSharedPreferences();

        boolean isFirstRun = mPreferences.getBoolean("first_run", true);
        if(isFirstRun) {
            onFirstRun();
            mPreferences.edit().putBoolean("first_run", false).apply();
        }


        //------------------------BEGIN VIEW WIRING-----------------------//

        //wire up input input fields
        mToDoTitle = (EditText) findViewById(R.id.et_todo_title);
        mToDoNotes = (EditText) findViewById(R.id.et_todo_notes);
        mToDoSubtasks = (EditText) findViewById(R.id.et_todo_subtasks);
        mToDoDate = (Spinner) findViewById(R.id.sp_date_picker);
        mToDoDeadline = (TextView) findViewById(R.id.et_deadline);
        mToDoList = (AutoCompleteTextView) findViewById(R.id.actv_list);
        mToDoTags = (MultiAutoCompleteTextView) findViewById(R.id.mactv_tags);

        //wire up cancel buttons
        mSubtasksCancelButton = (ImageButton) findViewById(R.id.button_cancel_subtasks);
        mDateCancelButton = (ImageButton) findViewById(R.id.button_cancel_date_picker);
        mDeadlineCancelButton = (ImageButton) findViewById(R.id.button_cancel_deadline);
        mListCancelButton = (ImageButton) findViewById(R.id.button_cancel_list);
        mTagsCancelButton = (ImageButton) findViewById(R.id.button_cancel_tags);

        //wire up linear layouts
        mSubtasksLayout = (RelativeLayout) findViewById(R.id.ll_subtasks);
        mDateLayout = (RelativeLayout) findViewById(R.id.ll_date_picker);
        mDeadlineLayout = (RelativeLayout) findViewById(R.id.ll_deadline);
        mListLayout = (RelativeLayout) findViewById(R.id.ll_list);
        mTagsLayout = (RelativeLayout) findViewById(R.id.ll_tags);

        //wire up meta-information bar (by now I wish I had used data binding...)
        mSubtasksButton = (ImageButton) findViewById(R.id.button_checklist_meta_bar);
        mDateButton = (ImageButton) findViewById(R.id.button_date_meta_bar);
        mDeadlineButton = (ImageButton) findViewById(R.id.button_deadline_meta_bar);
        mListButton = (ImageButton) findViewById(R.id.button_list_meta_bar);
        mTagsButton = (ImageButton) findViewById(R.id.button_tags_meta_bar);

        //wire up floating send button
        mSendActionButton = (FloatingActionButton) findViewById(R.id.send_button);


        //------------------------END VIEW WIRING-----------------------//


        //set up Date option spinner

        ArrayList<String> dateChoiceArrayList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.date_choice_array)));
        ArrayAdapter<CharSequence> spinneradapter = ArrayAdapter.createFromResource(this,
                R.array.date_choice_array, android.R.layout.simple_spinner_item);

        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mToDoDate.setAdapter(spinneradapter);
        mToDoDate.setOnItemSelectedListener(this);

        //set up List AutoCompleteTextView

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mListArray);

        mToDoList.setAdapter(listAdapter);

        //set up Tags MultiAutoCompleteTextView

        ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mTagArray);

        mToDoTags.setAdapter(tagAdapter);
        mToDoTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        //set up meta-bar listeners
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateLayout.setVisibility(View.VISIBLE);
                mDateButton.setVisibility(View.GONE);
            }
        });

        mDeadlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeadlineLayout.setVisibility(View.VISIBLE);
                mDeadlineButton.setVisibility(View.GONE);
            }
        });

        mSubtasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubtasksLayout.setVisibility(View.VISIBLE);
                mSubtasksButton.setVisibility(View.GONE);
            }
        });

        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListLayout.setVisibility(View.VISIBLE);
                mListButton.setVisibility(View.GONE);
            }
        });

        mTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagsLayout.setVisibility(View.VISIBLE);
                mTagsButton.setVisibility(View.GONE);

            }
        });

        //set up cancel buttons
        //FIXME The fields visibly become empty before animation finishes
        mSubtasksCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubtasksLayout.setVisibility(View.GONE);
                mToDoSubtasks.setText("");
                mSubtasksButton.setVisibility(View.VISIBLE);
            }
        });

        mDateCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateLayout.setVisibility(View.GONE);
                mToDoDate.setSelection(0);
                mDateButton.setVisibility(View.VISIBLE);
            }
        });

        mDeadlineCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeadlineLayout.setVisibility(View.GONE);
                mToDoDeadline.setText("");
                mDeadlineButton.setVisibility(View.VISIBLE);
            }
        });

        mListCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListLayout.setVisibility(View.GONE);
                mToDoList.setText("");
                mListButton.setVisibility(View.VISIBLE);
            }
        });

        mTagsCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagsLayout.setVisibility(View.GONE);
                mToDoTags.setText("");
                mTagsButton.setVisibility(View.VISIBLE);
            }
        });

        mSendActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToDo();
            }
        });

        Intent intent = getIntent();
        if(intent != null) {
            String receivedIntentText = intent.getStringExtra(Intent.EXTRA_TEXT);
            mToDoNotes.setText(receivedIntentText);
        }
    }

    /**Runs the first time the app is run, as an introduction/setup */
    private void onFirstRun() {
//            mPreferences.edit().putBoolean("first_run",false).apply();
        Intent welcomeIntent = new Intent(this, WelcomeActivity.class);
        startActivity(welcomeIntent);
    }

    public void openSettings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    //Date Spinner. Potentially this should go in its own class.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mDateString = getResources().getStringArray(R.array.date_values_array)[(int) id];
        if(id == 6) { //Choose Date
            showDatePicker(view);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


//    //Options menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.compose_menu, menu);
//        return true;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO Could improve performance by implementing onPreferenceChangeListener
        //as this is called in onCreate and then again in onResume()
        updateFromSharedPreferences();
    }

    private void updateFromSharedPreferences() {
        mEmailAddress = mPreferences.getString("email", "");
        mTagArray = splitByLineBreak(mPreferences.getString("tags", "Xmas\nErrand"));
        mListArray = splitByLineBreak(mPreferences.getString("lists", "Money\nSocial"));
    }


    private void sendToDo() {
        ToDo toDo = new ToDo.Builder()
                .title(mToDoTitle.getText().toString())
                .notes(mToDoNotes.getText().toString())
                .subtasks(getSubtasks())
                .when(mDateString)
                .deadline(mToDoDeadline.getText().toString())
                .list(mToDoList.getText().toString())
                .tags(getTags())
                .build();

        toDo.send(this, mEmailAddress);
    }

    private String[] getSubtasks() {
        return splitByLineBreak(mToDoSubtasks.getText().toString());
    }


    //this should probably be done away with after implementing Chips.
    private String[] getTags() {
        return mToDoTags.getText().toString()
                .trim()
                .split("\\s*,\\s*");
    }

    public String[] splitByLineBreak(String string) {
        return string.split("\\r?\\n");
    }

    public void showDatePicker(View view) {
        DialogFragment fragment = new DatePickerFragment(view);
        fragment.show(getSupportFragmentManager(),"datePicker");
    }

    public String processDatePickerResult(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    public void setViewDate(View view, int year, int month, int dayOfMonth) {
        String datePickerResult = processDatePickerResult(year, month, dayOfMonth);

        int id = view.getId();

        if(id ==  R.id.deadline_picker_button || id == R.id.et_deadline) {
            mToDoDeadline.setText(datePickerResult);
        } else {
            //TODO sort out spinner display of custom date if possible
            mDateString = datePickerResult;
        }
    }
}