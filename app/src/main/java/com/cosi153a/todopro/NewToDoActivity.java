package com.cosi153a.todopro;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewToDoActivity extends Activity {

    private String title;
    private String details;
    private final static String TAG = "NewToDo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do);
        EditText titleET = (EditText) findViewById(R.id.Title);
        EditText detailsET = (EditText) findViewById(R.id.Details);

        title  = titleET.getText().toString();
        details  = detailsET.getText().toString();

        final Button submitBtn = (Button) findViewById(R.id.btnSubmit);
        final Button cancelBtn = (Button) findViewById(R.id.btnCancel);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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


    private void setTodo()
    {
        Intent data = new Intent();
        Bundle todo = new Bundle();

        Log.v(TAG,"in setToDo()");

        todo.putString("TITLE",title);
        todo.putString("DETAILS", details);
        data.putExtras(todo);

        setResult(Activity.RESULT_OK,
                data);
        finish();
    }
}
