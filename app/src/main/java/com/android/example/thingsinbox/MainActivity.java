package com.android.example.thingsinbox;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String URL_PREPEND = "things:///json?data=%5B";

    //TODO implement SharedPreferences and make this recipient email address one of them. Other preferences:
    // - Integrated or external email sending. (integrated might be from an email I own, or one of theirs.
    //     - Privacy less of an issue if they configure their own email/use external. If integrated, config becomes simpler
    //       but delay time and/or privacy/security could become an issue.
    // - If integrated sending, email credentials
    // - Batch or individual sending (however would need to work around the 2000 limit; could quite happily send multiple messages.)

    public static String sEmailAddress = "add-to-things-vkhkcwncbj2t3atem55@things.email";

    private EditText mToDoTitle;
    private EditText mToDoNotes;
    private EditText mToDoSubtasks;
    private Spinner mToDoDate;
    private TextView mToDoDeadline;
    private EditText mToDoTags;
    private EditText mToDoList;
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

    private LinearLayout mSubtasksLayout;
    private LinearLayout mDateLayout;
    private LinearLayout mDeadlineLayout;
    private LinearLayout mListLayout;
    private LinearLayout mTagsLayout;
    private FloatingActionButton mSendActionButton;

    private ArrayAdapter<CharSequence> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //------------------------BEGIN VIEW WIRING-----------------------//

        //wire up input input fields
        mToDoTitle = (EditText) findViewById(R.id.et_todo_title);
        mToDoNotes = (EditText) findViewById(R.id.et_todo_notes);
        mToDoSubtasks = (EditText) findViewById(R.id.et_todo_subtasks);
        mToDoDate = (Spinner) findViewById(R.id.sp_date_picker);
        mToDoDeadline = (TextView) findViewById(R.id.et_deadline);
        mToDoList = (EditText) findViewById(R.id.et_list);
        mToDoTags = (EditText) findViewById(R.id.et_tags);

        //wire up cancel buttons
        mSubtasksCancelButton = (ImageButton) findViewById(R.id.button_cancel_subtasks);
        mDateCancelButton = (ImageButton) findViewById(R.id.button_cancel_date_picker);
        mDeadlineCancelButton = (ImageButton) findViewById(R.id.button_cancel_deadline);
        mListCancelButton = (ImageButton) findViewById(R.id.button_cancel_list);
        mTagsCancelButton = (ImageButton) findViewById(R.id.button_cancel_tags);

        //wire up linear layouts
        mSubtasksLayout = (LinearLayout) findViewById(R.id.ll_subtasks);
        mDateLayout = (LinearLayout) findViewById(R.id.ll_date_picker);
        mDeadlineLayout = (LinearLayout) findViewById(R.id.ll_deadline);
        mListLayout = (LinearLayout) findViewById(R.id.ll_list);
        mTagsLayout = (LinearLayout) findViewById(R.id.ll_tags);

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
        mAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_choice_array, android.R.layout.simple_spinner_item);

        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mToDoDate.setAdapter(mAdapter);
        mToDoDate.setOnItemSelectedListener(this);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.compose_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_send:
                sendToDo();
                return true;
        }
        return false;
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

        toDo.send(this);
    }

    private String[] getSubtasks() {
        return mToDoSubtasks.getText().toString()
                .split("\\r?\\n"); //Splits EditText population by linebreak
    }


    //this should probably be done away with after implementing Chips.
    private String[] getTags() {
        return mToDoTags.getText().toString()
                .split("\\r?\\n"); //Splits EditText population by linebreak
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

        if(view.getId() ==  R.id.deadline_picker_button) {
            mToDoDeadline.setText(datePickerResult);
        } else {
            //TODO sort out spinner display of custom date if possible
            mDateString = datePickerResult;
        }
    }
}

//TODO Add further metadata support:
//  - Date
//      - Further DatePickerFragment may be chosen for custom date
//  - Deadline
//      - DatePickerFragment
//  - Tags
//      - Multiple choice Powerlist
//      - Make use of Chips!!!
//      - Should be persisted simply in file storage.

//TODO support configuration of list of Tags
//  - allow own choice of configured tags


//TODO Add history screen for keeping note of recently sent notes
//  - Keep database of To-Do objects (use Room probably?)
//  - Bring them back in a RecyclerView
//  - Can expand notes somehow, to see more detailed info

//TODO Add Project/Area list configuration
//  this allows choosing of an Area or project to assign a task to


//TODO Stretch Add project design screen:
//  adding a new project/area allows

//TODO Stretch add startup configuration splash
//TODO Stretch allow customisation of UI colors
//TODO Stretch allow customisation of individual Tag colors.
//TODO Stretch allow saving of Project/task templates
//TODO Stretch update ToDo to work with Gson instead of JSONObject. Create inner class for Attributes potentially?
//TODO Stretch incorporate applescript functionality?? Perhaps as part of initial setup. Would be cool.