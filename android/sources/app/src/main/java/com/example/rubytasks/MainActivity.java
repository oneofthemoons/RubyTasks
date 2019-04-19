package com.example.rubytasks;

import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainActivity extends AppCompatActivity {

    public TextView[]                           labels;
    public LinearLayout[]                       tasksLayouts;
    private int                                 margin;
    public List<Pair<String, List<Pair<String, Boolean>>>>     projects;
    LinearLayout                                mainLinearLayout;
    CheckBox[][] checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        margin = 20;
        projects = new ArrayList<Pair<String, List<Pair<String, Boolean>>>>();
        final MainActivity mainLink = this;

        Ion.getDefault(this).getConscryptMiddleware().enable(false);


        Ion.with(this)
                .load("http://rubytasks.herokuapp.com/projects_json")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (result != null)
                        {
                            Iterator iter = result.iterator();
                            while (iter.hasNext())
                            {
                                JsonObject innerObj = (JsonObject) iter.next();
                                mainLink.addProject(innerObj.get("title").getAsString());
                            }
                            Ion.with(mainLink)
                                    .load("http://rubytasks.herokuapp.com/todos_json")
                                    .asJsonArray()
                                    .setCallback(new FutureCallback<JsonArray>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonArray result) {
                                            if (result != null)
                                            {
                                                Iterator iter = result.iterator();
                                                while (iter.hasNext())
                                                {
                                                    JsonObject innerObj = (JsonObject) iter.next();
                                                    mainLink.addTodo(innerObj.get("text").getAsString(), innerObj.get("isCompleted").getAsBoolean(),innerObj.get("project_id").getAsInt());
                                                }
                                                mainLink.renderProjects();
                                            }
                                        }
                                    });
                        }
                    }
                });

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
        toolbarTitle.setText("Задачи");
        toolbar.addView(toolbarTitle);

        ScrollView mainScrollView = new ScrollView(this);
        mainScrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        mainLinearLayout = new LinearLayout(this);
        mainLinearLayout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));
        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);



        // create '+' button in a toolbar
        TextView addTodoButton = new TextView(this);
        RelativeLayout.LayoutParams addTodoButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        addTodoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addTodoButtonParams.setMargins(0, 25, 40, 0);
        addTodoButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        addTodoButton.setLayoutParams(addTodoButtonParams);
        addTodoButton.setText("+");
        addTodoButton.setTextColor(ContextCompat.getColor(this, R.color.systemWhiteColor));
        addTodoButton.setTextSize(30);
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTodo = new Intent(mainLink, TodoActivity.class);
                addTodo.putExtra("size", projects.size());
                for (int i = 0; i < projects.size(); ++i)
                    addTodo.putExtra("name" + i, mainLink.labels[i].getText());
                startActivityForResult(addTodo, 1);
            }
        });
        toolbar.addView(addTodoButton);

        mainScrollView.addView(mainLinearLayout);
        parentLinearLayout.addView(toolbar);
        parentLinearLayout.addView(mainScrollView);
        setContentView(parentLinearLayout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == TodoActivity.RESULT_OK) {
                Bundle args = data.getExtras();
                int id = (int) args.get("id");
                LinearLayout oneTaskLayout = new LinearLayout(this);
                oneTaskLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                final MainActivity link = this;

                CheckBox checkBox = new CheckBox(this);
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                checkBox.setId(projects.size() * 3 * 10000000 + id * 10000 + this.checkBox[id].length);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        JsonObject json = new JsonObject();
                        int projId = (buttonView.getId() - projects.size() * 3 * 10000000) / 10000 + 1;
                        int todoId = (buttonView.getId()) % 10000 + 1;
                        json.addProperty("project_id", String.valueOf(projId));
                        json.addProperty("id", String.valueOf(todoId));

                        Ion.with(link)
                                .load("PATCH","http://rubytasks.herokuapp.com/projects/" + String.valueOf(projId) + "/todos/" + String.valueOf(todoId))
                                .setJsonObjectBody(json)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                    }
                                });
                    }
                });

                TextView task = new TextView(this);
                LinearLayout.LayoutParams taskParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                taskParams.setMargins(margin, 0, margin, margin);
                task.setLayoutParams(taskParams);
                task.setTextSize(15);
                task.setText(args.get("value").toString());
                task.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Light.ttf"));
                oneTaskLayout.addView(checkBox);
                oneTaskLayout.addView(task);

                projects.get(id).second.add(new Pair<String, Boolean>(args.get("value").toString(), false));

                tasksLayouts[id].addView(oneTaskLayout);
            }
            if (resultCode == TodoActivity.RESULT_CANCELED) { //Write your code if there's no result } } }//onActivityResult
            }
        }
    }

    public void addProject(String title)
    {
        this.projects.add(new Pair<String, List<Pair<String, Boolean>>>(new String(title), new ArrayList<Pair<String, Boolean>>()));
    }

    public void addTodo(String text, Boolean isCompleted, int id)
    {
        if (projects.size() > id - 1 && id > 0)
            projects.get(id - 1).second.add(new Pair<String, Boolean>(text, isCompleted));
    }

    public void renderProjects()
    {
        final MainActivity link = this;
        int countProjects = projects.size();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int screenWidth = size.x;
        int screenHeight = size.y;
        int columns = screenWidth / 450 == 0 ? 1 : screenWidth / 450;
        int rows = countProjects / columns + (countProjects % columns == 0 ? 0 : 1);
        int boxWidth = screenWidth / columns;

        LinearLayout[] rowLinearLayouts = new LinearLayout[rows];
        for (int i = 0; i < rows; ++i) {
            rowLinearLayouts[i] = new LinearLayout(this);
            rowLinearLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
        }

        RelativeLayout[] relativeLayouts = new RelativeLayout[countProjects];
        LinearLayout.LayoutParams[] relativeLayoutParams = new LinearLayout.LayoutParams[countProjects];
        labels = new TextView[countProjects];
        RelativeLayout.LayoutParams[] labelParams = new RelativeLayout.LayoutParams[countProjects];
        View[] hr = new View[countProjects];
        RelativeLayout.LayoutParams[] hrParams = new RelativeLayout.LayoutParams[countProjects];
        tasksLayouts = new LinearLayout[countProjects];
        RelativeLayout.LayoutParams[] tasksLayoutsParams = new RelativeLayout.LayoutParams[countProjects];
        TextView[][] task = new TextView[countProjects][];
        LinearLayout.LayoutParams[][] taskParams = new LinearLayout.LayoutParams[countProjects][];
        checkBox = new CheckBox[countProjects][];
        LinearLayout[][] oneTaskLayouts = new LinearLayout[countProjects][];
        for (int i = 0; i < projects.size(); ++i) {
            relativeLayouts[i] = new RelativeLayout(this);
            relativeLayoutParams[i] = new LinearLayout.LayoutParams(boxWidth - (margin << 1), LinearLayout.LayoutParams.WRAP_CONTENT);
            relativeLayoutParams[i].setMargins((i % columns == 0) ? margin : (margin >> 1), i / columns == 0 ? margin << 1 : margin, margin, margin);
            relativeLayouts[i].setLayoutParams(relativeLayoutParams[i]);
            relativeLayouts[i].setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
            relativeLayouts[i].setElevation(8);

            //label init
            labels[i] = new TextView(this);
            labelParams[i] = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            labelParams[i].addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            labelParams[i].addRule(RelativeLayout.ALIGN_PARENT_TOP);
            labelParams[i].setMargins(margin, margin, margin, margin);
            labels[i].setLayoutParams(labelParams[i]);
            labels[i].setText(projects.get(i).first);
            labels[i].setTextColor(ContextCompat.getColor(this, R.color.systemBlackColor));
            labels[i].setTextSize(20);
            labels[i].setId(i + 1); //unique id with cntPrg * 0
            labels[i].setTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Light.ttf"));

            //create HR-line
            hr[i] = new View(this);
            hrParams[i] = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 2);
            hrParams[i].addRule(RelativeLayout.BELOW, i + 1);
            hrParams[i].setMargins(margin >> 1, 0, margin >> 1, 0);
            hr[i].setLayoutParams(hrParams[i]);
            hr[i].setBackgroundColor(ContextCompat.getColor(this, R.color.placeholderColor));
            hr[i].setId(countProjects + i + 1); //unique id with cntPrg * 1

            //create task list
            tasksLayouts[i] = new LinearLayout(this);
            tasksLayoutsParams[i] = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tasksLayoutsParams[i].addRule(RelativeLayout.BELOW, countProjects + i + 1);
            tasksLayoutsParams[i].setMargins(margin, margin, margin, margin);
            tasksLayouts[i].setLayoutParams(tasksLayoutsParams[i]);
            tasksLayouts[i].setOrientation(LinearLayout.VERTICAL);

            int countTasks = 5;

            oneTaskLayouts[i] = new LinearLayout[countTasks];
            checkBox[i] = new CheckBox[countTasks];
            task[i] = new TextView[countTasks];
            taskParams[i] = new LinearLayout.LayoutParams[countTasks];
            for (int j = 0; j < projects.get(i).second.size(); ++j) {
                oneTaskLayouts[i][j] = new LinearLayout(this);
                oneTaskLayouts[i][j].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                checkBox[i][j] = new CheckBox(this);
                checkBox[i][j].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                checkBox[i][j].setChecked(projects.get(i).second.get(j).second);
                checkBox[i][j].setId(projects.size() * 3 * 10000000 + i * 10000 + j);
                checkBox[i][j].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        JsonObject json = new JsonObject();
                        int projId = (buttonView.getId() - projects.size() * 3 * 10000000) / 10000 + 1;
                        int todoId = (buttonView.getId()) % 10000 + 1;
                        json.addProperty("project_id", String.valueOf(projId));
                        json.addProperty("id", String.valueOf(todoId));

                        Ion.with(link)
                                .load("PATCH","http://rubytasks.herokuapp.com/projects/" + String.valueOf(projId) + "/todos/" + String.valueOf(todoId))
                                .setJsonObjectBody(json)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                    }
                                });
                    }
                });

                task[i][j] = new TextView(this);
                taskParams[i][j] = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                taskParams[i][j].setMargins(margin, 0, margin, margin);
                task[i][j].setLayoutParams(taskParams[i][j]);
                task[i][j].setTextSize(15);
                task[i][j].setText(projects.get(i).second.get(j).first);
                task[i][j].setTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Light.ttf"));
                oneTaskLayouts[i][j].addView(checkBox[i][j]);
                oneTaskLayouts[i][j].addView(task[i][j]);

                tasksLayouts[i].addView(oneTaskLayouts[i][j]);
            }

            //adding views to relative
            relativeLayouts[i].addView(labels[i], labelParams[i]);
            relativeLayouts[i].addView(hr[i], hrParams[i]);
            relativeLayouts[i].addView(tasksLayouts[i], tasksLayoutsParams[i]);

            //adding views to mainframe part
            rowLinearLayouts[i / columns].addView(relativeLayouts[i], relativeLayoutParams[i]);
        }
        for (int i = 0; i < rows; ++i)
            mainLinearLayout.addView(rowLinearLayouts[i]);
    }
}