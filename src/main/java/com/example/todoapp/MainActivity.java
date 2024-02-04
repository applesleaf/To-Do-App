package com.example.todoapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TaskDataSource dataSource;
    private ArrayAdapter<Task> adapter;
    private List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new TaskDataSource(this);
        dataSource.open();

        tasks = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);

        ListView listView = findViewById(R.id.listViewTasks);
        listView.setAdapter(adapter);

        updateTaskList();

        EditText editTextTask = findViewById(R.id.editTextTask);
        findViewById(R.id.buttonAddTask).setOnClickListener(v -> {
            String taskName = editTextTask.getText().toString();
            if (!taskName.isEmpty()) {
                Task newTask = dataSource.createTask(taskName);
                tasks.add(newTask);
                adapter.notifyDataSetChanged();
                editTextTask.getText().clear();
            }
        });

        // Listener for marking a task as completed or editing a task
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Task selectedTask = tasks.get(position);
            // Toggle completion status or edit task name here
            // For demonstration purpose, let's consider editing the task name
            EditText editText = new EditText(MainActivity.this);
            editText.setText(selectedTask.getTaskName());
            editText.setSelection(selectedTask.getTaskName().length());
            editText.requestFocus();
            editText.postDelayed(() -> editText.setSelection(selectedTask.getTaskName().length()), 100);
            editText.setSingleLine(true);
            editText.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    selectedTask.setTaskName(editText.getText().toString());
                    dataSource.updateTask(selectedTask);
                    adapter.notifyDataSetChanged();
                }
            });
            listView.clearFocus();
            listView.requestFocus();
            listView.setSelection(position);
            listView.performItemClick(view, position, id);
            listView.setSelection(position);
            listView.addHeaderView(editText);
            editText.requestFocus();
        });

        // Listener for deleting a task
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Task selectedTask = tasks.get(position);
            dataSource.deleteTask(selectedTask.getId());
            tasks.remove(selectedTask);
            adapter.notifyDataSetChanged();
            return true;
        });
    }

    private void updateTaskList() {
        tasks.clear();
        Cursor cursor = dataSource.getAllTasksCursor();
        if (cursor.moveToFirst()) {
            do {
                Task task = dataSource.cursorToTask(cursor);
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}

