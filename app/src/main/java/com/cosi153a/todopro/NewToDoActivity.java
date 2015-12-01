package com.cosi153a.todopro;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
//import android.nfc.Tag;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.app.DialogFragment;
import android.app.Dialog;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.app.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewToDoActivity extends Activity {

    private String title;
    private String details;
    private static Calendar time;
    private boolean alarm;
    private static TextView dateTextView;
    private static TextView timeTextView;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");;
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private final static String TAG = "NewToDo";

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            time.set(year,month,day);
            NewToDoActivity.updateDate();
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            time.set(Calendar.HOUR_OF_DAY,hourOfDay);
            time.set(Calendar.MINUTE, minute);
            NewToDoActivity.updateTime();
        }
    }

    public static void updateDate() {
        dateTextView.setText(dateFormat.format(time.getTime()));
    }

    public static void updateTime() {
        timeTextView.setText(timeFormat.format(time.getTime()));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do);
        final EditText titleET = (EditText) findViewById(R.id.Title);
        final EditText detailsET = (EditText) findViewById(R.id.Details);
        dateTextView = (TextView) this.findViewById(R.id.dateString);
        timeTextView = (TextView) this.findViewById(R.id.timeString);

        title  = titleET.getText().toString();
        details  = detailsET.getText().toString();
        time = Calendar.getInstance();
        String dateString = dateFormat.format(time.getTime());
        String timeString = timeFormat.format(time.getTime());
        dateTextView.setText(dateString);
        timeTextView.setText(timeString);

        final Button submitBtn = (Button) findViewById(R.id.btnSubmit);
        final Button cancelBtn = (Button) findViewById(R.id.btnCancel);
        final Button alarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                title = titleET.getText().toString();
                details = detailsET.getText().toString();
                setTodo();
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            Log.d(TAG, "Alarm On");
            alarm = true;
        } else {
            alarm = false;
            Log.d(TAG, "Alarm Off");
        }
    }

    private void setTodo()
    {
        Intent data = new Intent();
        Bundle todo = new Bundle();

        Log.v(TAG,"in setToDo()");

        todo.putString("TITLE",title);
        todo.putString("DETAILS", details);
        todo.putBoolean("ALARM", alarm);
        todo.putSerializable("TIME", time.getTime());
        data.putExtras(todo);
        setResult(Activity.RESULT_OK,
                data);
        finish();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

}





