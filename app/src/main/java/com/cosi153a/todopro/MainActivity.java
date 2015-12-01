package com.cosi153a.todopro;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.LoginFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.app.ListActivity;

import com.cosi153a.todopro.db.TaskContract;
import com.cosi153a.todopro.db.TaskDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends ListActivity {

    private TaskDBHelper helper;
    public static final int REQUEST_CODE = 1111;
    public static final String TAG = "MainActivity";
    private static MainActivity inst;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;


    public static MainActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        updateUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent intent = new Intent(MainActivity.this,NewToDoActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                updateUI();
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
                final Date datetime = (Date) iData.getExtras().getSerializable("TIME");
                final boolean alarm = iData.getExtras().getBoolean("ALARM");

                Log.v(TAG,title+details);
                helper = new TaskDBHelper(MainActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                if (alarm) {
                    Calendar alarmTime = new GregorianCalendar();
                    alarmTime.setTime(datetime);
                    Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
                    alarmManager.set(AlarmManager.RTC, alarmTime.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.cancel(pendingIntent);
                    Log.d(TAG, "Alarm Off");
                }

                values.clear();
                values.put(TaskContract.Columns.TASK, title);
                values.put(TaskContract.Columns.DETAILS, details);
                values.put(TaskContract.Columns.DATE, time.format(datetime));
                db.insertWithOnConflict(TaskContract.TABLE, null, values,
                        SQLiteDatabase.CONFLICT_IGNORE);

                updateUI();

            }
        }

    }

    private void updateUI() {
        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK, TaskContract.Columns.DETAILS, TaskContract.Columns.DATE},
                null,null,null,null,null);

        ListAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.task_view,
                cursor,
                new String[] { TaskContract.Columns.TASK, TaskContract.Columns.DETAILS, TaskContract.Columns.DATE},
                new int[] { R.id.TitleView, R.id.DetailsView, R.id.DateView},
                0
        );
        this.setListAdapter(listAdapter);

    }

    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.TitleView);
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

