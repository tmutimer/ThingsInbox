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


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String URL_PREPEND = "things:///json?data=%5B";

    public static String sEmailAddress = "add-to-things-vkhkcwncbj2t3atem55@things.email";

    private EditText mToDoTitle;
    private EditText mToDoNotes;
    private EditText mToDoSubtasks;
    private Spinner mToDoDate;
    private TextView mDeadline;
    private EditText mToDoTags;
    private EditText mToDoList;
    private String mDate;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //------------------------BEGIN WIRING-----------------------//

        //wire up input input fields
        mToDoTitle = (EditText) findViewById(R.id.et_todo_title);
        mToDoNotes = (EditText) findViewById(R.id.et_todo_notes);
        mToDoSubtasks = (EditText) findViewById(R.id.et_todo_subtasks);
        mToDoDate = (Spinner) findViewById(R.id.sp_date_picker);
        mDeadline = (EditText) findViewById(R.id.et_deadline);
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

        //wire up meta-information bar (wish I had used data binding already...)
        mSubtasksButton = (ImageButton) findViewById(R.id.button_checklist_meta_bar);
        mDateButton = (ImageButton) findViewById(R.id.button_date_meta_bar);
        mDeadlineButton = (ImageButton) findViewById(R.id.button_deadline_meta_bar);
        mListButton = (ImageButton) findViewById(R.id.button_list_meta_bar);
        mTagsButton = (ImageButton) findViewById(R.id.button_tags_meta_bar);


        //------------------------END WIRING-----------------------//


        //set up Date option spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.date_choice_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mToDoDate.setAdapter(adapter);
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
        mSubtasksCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubtasksLayout.setVisibility(View.GONE);
                mSubtasksButton.setVisibility(View.VISIBLE);
            }
        });

        mDateCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateLayout.setVisibility(View.GONE);
                mDateButton.setVisibility(View.VISIBLE);
            }
        });

        mDeadlineCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeadlineLayout.setVisibility(View.GONE);
                mDeadlineButton.setVisibility(View.VISIBLE);
            }
        });

        mListCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListLayout.setVisibility(View.GONE);
                mListButton.setVisibility(View.VISIBLE);
            }
        });

        mTagsCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagsLayout.setVisibility(View.GONE);
                mTagsButton.setVisibility(View.VISIBLE);
            }
        });
    }



    //Date Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mDate = getResources().getStringArray(R.array.date_values_array)[(int) id];
        if(id == 6) { //Choose Date
            //TODO bring up DatePickerFragment - may need to change this activity to fragment
//            FragmentManager manager = getSupportFragmentManager();
//            DatePickerFragment dialog = DatePickerFragment.newInstance();
//            dialog.setTargetFragment(MainActivity.this, REQUEST_DATE);
//            dialog.show(manager, DIALOG_DATE);
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
                ToDo toDo = new ToDo.Builder()
                        .title(mToDoTitle.getText().toString())
                        .notes(mToDoNotes.getText().toString())
                        .subtasks(getSubtasks())
                        .when(mDate)
//                        .deadline(mToDoDeadline)
                        .list(mToDoList.getText().toString())
                        .tags(getTags())
                        .build();

                toDo.send(this);
                return true;
        }
        return false;
    }

    private String[] getSubtasks() {
        return mToDoSubtasks.getText().toString()
                .split("\\r?\\n"); //Splits EditText population by linebreak
    }

    private String[] getTags() {
        return mToDoTags.getText().toString()
                .split("\\r?\\n"); //Splits EditText population by linebreak
    }


}

//TODO Decide on a UI design as this will save time wiring up views later
//  - The task entry screen should be available in one click
//  - History should be on screen, showing some recent notes.

//TODO Add further metadata support:
//  - Date
//      - Further DatePickerFragment may be chosen for custom date
//  - Deadline
//      - DatePickerFragment
//  - Tags
//      - Multiple choice Powerlist - use Room to store this

//TODO support configuration of list of Tags
//  - allow own choice of configured tags
//      - allow choice of highlight color, reflected in the multi-powerlist

//TODO Add history screen for keeping note of recently sent notes
//  - Keep database of To-Do objects (use Room probably?)
//  - Bring them back in a RecyclerView
//  - Can expand notes somehow, to see more detailed info

//TODO Add Project/Area list configuration
//  this allows choosing of an Area or project to assign a task to


//TODO Stretch Add project design screen:
//  adding a new project/area allows


//TODO Stretch add startup configuration splash
//TODO Stretch allow saving of Project/task templates
//TODO Stretch update ToDo to work with Gson instead of JSONObject. Create inner class for Attributes potentially?
//TODO Stretch incorporate applescript functionality?? Perhaps as part of initial setup. Would be cool.