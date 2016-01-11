package com.cosi153a.todopro;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cosi153a.todopro.db.TaskContract;

/**
 * Created by ShuChen on 1/6/2016.
 */
public class CursorAdapterWithToggle extends ResourceCursorAdapter {

    public CursorAdapterWithToggle(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //        new String[] { TaskContract.Columns.TASK, TaskContract.Columns.DETAILS, TaskContract.Columns.DATE},
        //        new int[] { R.id.TitleView, R.id.DetailsView, R.id.DateView}

        TextView title = (TextView) view.findViewById(R.id.TitleView);
        title.setText(cursor.getString(cursor.getColumnIndex(TaskContract.Columns.TASK)));

        TextView details = (TextView) view.findViewById(R.id.DetailsView);
        details.setText(cursor.getString(cursor.getColumnIndex(TaskContract.Columns.DETAILS)));

        TextView date = (TextView) view.findViewById(R.id.DateView);
        date.setText(cursor.getString(cursor.getColumnIndex(TaskContract.Columns.DATE)));

        // button state
        ToggleButton tb = (ToggleButton) view.findViewById(R.id.mainAlarmToggle);
        String task = title.getText().toString();
        int piid = task.hashCode();
        String key = String.valueOf(piid);
        Log.d("IN CUSTOM ADAPTER",task+"; "+key);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),context.MODE_PRIVATE);
        boolean set = sharedPreferences.getBoolean(key, false);

        Log.d("IN CUSTOM ADAPTER",String.valueOf(sharedPreferences==null)+ "; "+sharedPreferences.contains(key));


        tb.setChecked(set);
    }



}
