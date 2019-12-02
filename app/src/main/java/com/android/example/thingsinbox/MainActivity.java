package com.android.example.thingsinbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String URL_PREPEND = "things:///json?data=%5B";

    public static String sEmailAddress = "add-to-things-vkhkcwncbj2t3atem55@things.email";

    private EditText mToDoTitle;
    private EditText mToDoNotes;
    private EditText mToDoSubtasks;
    private EditText mToDoTags;
    private EditText mToDoProject;
    private Spinner mToDoDate;
    private String mSelectedWhenOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        mToDoTitle = (EditText) findViewById(R.id.et_todo_title);
        mToDoNotes = (EditText) findViewById(R.id.et_todo_notes);
        mToDoSubtasks = (EditText) findViewById(R.id.et_todo_subtasks);

        mToDoDate = (Spinner) findViewById(R.id.sp_date_picker);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.date_choice_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mToDoDate.setAdapter(adapter);
        mToDoDate.setOnItemSelectedListener(this);
    }


    //Date Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectedWhenOption = getResources().getStringArray(R.array.date_values_array)[(int) id];
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
                        .whenString(mSelectedWhenOption)
                        .subtasks(getSubtasks())
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


}

//TODO Create a Robust back end for projects
//TODO Decide on a UI design as this will save time wiring up views later
//  - The task entry screen should be available in one click from

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

//TODO Add project design screen:
//  adding a new project/area allows

//TODO Stretch allow saving of Project/task templates
//TODO Stretch update ToDo to work with Gson instead of JSONObject. Create inner class for Attributes potentially?
//TODO Stretch incorporate applescript functionality?? Perhaps as part of initial setup. Would be cool.