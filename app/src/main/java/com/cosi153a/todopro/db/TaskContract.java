package com.cosi153a.todopro.db;

/**
 * Created by apple on 10/21/15.
 */
import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.cosi153.todoapp.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tasks";

    public class Columns {
        public static final String TASK = "task";
        public static final String DETAILS = "details";
        public static final String _ID = BaseColumns._ID;
    }
}