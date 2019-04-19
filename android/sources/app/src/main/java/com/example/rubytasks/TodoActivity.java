package com.example.rubytasks;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.w3c.dom.Text;

import java.util.List;

public class TodoActivity extends AppCompatActivity {

    public int  switchedId;
    ImageView   checked;
    EditText    inputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout parentLinearLayout = new LinearLayout(this);
        parentLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parentLinearLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backColor));
        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout toolbar = new RelativeLayout(this);
        toolbar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 110));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.accentColor));
        toolbar.setElevation(9);

        TextView toolbarTitle = new TextView(this);
        RelativeLayout.LayoutParams toolbarTitleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        toolbarTitleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        toolbarTitle.setLayoutParams(toolbarTitleParams);
        toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Light.ttf"));
        toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.systemWhiteColor));
        toolbarTitle.setTextSize(27);
        toolbarTitle.setText("Новая задача");
        toolbar.addView(toolbarTitle);

        final TodoActivity link = this;
        switchedId = -1;
        checked = new ImageView(this);
        RelativeLayout.LayoutParams checkedParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        checkedParams.addRule(RelativeLayout.CENTER_VERTICAL);
        checkedParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        checkedParams.setMargins(0, 0, 40, 0);
        checked.setLayoutParams(checkedParams);
        checked.setImageResource(R.drawable.donegray);

        ImageView backButton = new ImageView(this);
        RelativeLayout.LayoutParams backButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        backButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        backButtonParams.setMargins(40, 0, 0, 0);
        backButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        backButton.setLayoutParams(backButtonParams);
        backButton.setImageResource(R.drawable.backicon);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link.finish();
            }
        });
        toolbar.addView(backButton);

        ImageView doneButton = new ImageView(this);
        RelativeLayout.LayoutParams doneButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        doneButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        doneButtonParams.setMargins(0, 0, 40, 0);
        doneButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        doneButton.setLayoutParams(doneButtonParams);
        doneButton.setImageResource(R.drawable.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent returnIntent = new Intent();
                if (switchedId != -1) {
                    returnIntent.putExtra("id", switchedId - 1);
                    returnIntent.putExtra("value", inputField.getText());

                    JsonObject json = new JsonObject();
                    json.addProperty("project_id", String.valueOf(switchedId));
                    json.addProperty("todo_text", inputField.getText().toString());

                    Ion.with(link)
                            .load("http://rubytasks.herokuapp.com/projects/index")
                            .setJsonObjectBody(json)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    link.setResult(TodoActivity.RESULT_OK, returnIntent);
                                }
                            });
                    link.setResult(TodoActivity.RESULT_OK, returnIntent);
                }
                else
                    link.setResult(TodoActivity.RESULT_CANCELED);
               link.finish();
            }
        });
        toolbar.addView(doneButton);

        ScrollView mainScrollView = new ScrollView(this);
        mainScrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mainScrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.backColor));

        int countProjects;

        Bundle args = getIntent().getExtras();
        countProjects = (int) args.get("size");



        LinearLayout listTodoNames = new LinearLayout(this);
        listTodoNames.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));
        listTodoNames.setBackgroundColor(ContextCompat.getColor(this, R.color.backColor));
        listTodoNames.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams titParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titParams.setMargins(30, 20, 30, 15);

        TextView todo = new TextView(this);

        todo.setLayoutParams(titParams);
        todo.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Light.ttf"));
        todo.setTextSize(13);
        todo.setText("ЗАДАЧА");
        todo.setTextColor(ContextCompat.getColor(this, R.color.systemShadow));
        listTodoNames.addView(todo);

        RelativeLayout inputBlock = new RelativeLayout(this);
        inputBlock.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        inputBlock.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));

        inputField = new EditText(this);
        RelativeLayout.LayoutParams inputFieldParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        inputFieldParams.setMargins(30, 20, 30, 20);
        inputField.setLayoutParams(inputFieldParams);
        inputBlock.addView(inputField);

        listTodoNames.addView(inputBlock);


        TextView category = new TextView(this);
        category.setLayoutParams(titParams);
        category.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Light.ttf"));
        category.setTextSize(13);
        category.setText("КАТЕГОРИЯ");
        category.setTextColor(ContextCompat.getColor(this, R.color.systemShadow));
        listTodoNames.addView(category);

        for (int i = 0; i < countProjects; ++i){
            RelativeLayout nameBlock = new RelativeLayout(this);
            nameBlock.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            nameBlock.setId(i + 1);
            nameBlock.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
            nameBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout nameBlock;
                    if (switchedId != -1) {
                        nameBlock = (RelativeLayout) link.findViewById(switchedId);
                        nameBlock.removeView(link.checked);
                    }
                    link.switchedId = v.getId();
                    nameBlock = (RelativeLayout) link.findViewById(switchedId);
                    nameBlock.addView(checked);
                }
            });
            nameBlock.setElevation(4);

            View hr = new View(this);
            RelativeLayout.LayoutParams hrParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
            hrParams.addRule(RelativeLayout.BELOW, i + 1);
            hr.setLayoutParams(hrParams);
            hr.setBackgroundColor(ContextCompat.getColor(this, R.color.systemShadow));

            TextView name = new TextView(this);
            RelativeLayout.LayoutParams nameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            nameParams.addRule(RelativeLayout.CENTER_VERTICAL);
            nameParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            nameParams.setMargins(40, 30, 40, 30);
            name.setLayoutParams(nameParams);
            name.setText(args.get("name" + i).toString());
            name.setTextSize(18);
            name.setTextColor(ContextCompat.getColor(this, R.color.systemBlackColor));
            name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Light.ttf"));
            nameBlock.addView(name);

            listTodoNames.addView(nameBlock);
            if (i < countProjects - 1)
                listTodoNames.addView(hr);
        }

        mainScrollView.addView(listTodoNames);

        parentLinearLayout.addView(toolbar);
        parentLinearLayout.addView(mainScrollView);
        setContentView(parentLinearLayout);
    }

}
