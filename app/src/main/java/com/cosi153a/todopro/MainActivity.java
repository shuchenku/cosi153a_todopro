package com.cosi153a.todopro;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.app.ListActivity;

import com.cosi153a.todopro.db.TaskContract;
import com.cosi153a.todopro.db.TaskDBHelper;

public class MainActivity extends ListActivity {

    private TaskDBHelper helper;
    public static final int REQUEST_CODE = 1111;
    public static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Add a task");
//                builder.setMessage("What do you want to do?");
//                final EditText inputField = new EditText(this);
//                builder.setView(inputField);
//                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String task = inputField.getText().toString();
//                        Log.d("MainActivity", task);

                Intent intent = new Intent(MainActivity.this,NewToDoActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

                updateUI();
//                });
//                builder.setNegativeButton("Cancel",null);
//
//                builder.create().show();
                return true;

            default:
                return false;
        }
    }

    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent iData)
    {
        if ( requestCode == REQUEST_CODE )
        {
            if (resultCode == ListActivity.RESULT_OK )
            {
                final String title = iData.getExtras().getString("TITLE");
                final String details = iData.getExtras().getString("DETAILS");


                Log.v(TAG,title+details);
                helper = new TaskDBHelper(MainActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.clear();
                values.put(TaskContract.Columns.TASK, title);
                values.put(TaskContract.Columns.DETAILS, details);
                db.insertWithOnConflict(TaskContract.TABLE, null, values,
                        SQLiteDatabase.CONFLICT_IGNORE);
//                Log.v( TAG, "Retrieved Value zData is "+zData );
                //..logcats "Retrieved Value zData is returnValueAsString"

                updateUI();

            }
        }

    }

    private void updateUI() {
        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                null,null,null,null,null);



        ListAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.task_view,
                cursor,
                new String[] { TaskContract.Columns.TASK},
                new int[] { R.id.taskTextView},
                0
        );

        this.setListAdapter(listAdapter);

    }

    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.TASK,
                task);


        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }

}

