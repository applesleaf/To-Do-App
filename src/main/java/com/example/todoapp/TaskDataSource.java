package com.example.todoapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskDataSource {
    private static final String TAG = "TaskDataSource";

    private SQLiteDatabase database;
    private TaskDBHelper dbHelper;

    public TaskDataSource(Context context) {
        dbHelper = new TaskDBHelper(context);
    }

    public void open() throws SQLException {
        Log.d(TAG, "Opening database");
        database = dbHelper.getWritableDatabase();
    }

    public void updateTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskDBHelper.COLUMN_TASK_NAME, task.getTaskName());

        // Update the task in the database
        database.update(TaskDBHelper.TABLE_TASKS, values, TaskDBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    public void close() {
        Log.d(TAG, "Closing database");
        dbHelper.close();
    }

    public Task createTask(String taskName) {
        Log.d(TAG, "Creating new task: " + taskName);
        ContentValues values = new ContentValues();
        values.put(TaskDBHelper.COLUMN_TASK_NAME, taskName);

        long insertId = database.insert(TaskDBHelper.TABLE_TASKS, null, values);

        Cursor cursor = database.query(TaskDBHelper.TABLE_TASKS,
                null, TaskDBHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Task newTask = cursorToTask(cursor);
        cursor.close();
        return newTask;
    }

    public void updateTaskCompletionStatus(long taskId, boolean completed) {
        // Update the completion status of the task in the database
        ContentValues values = new ContentValues();
        values.put(TaskDBHelper.COLUMN_TASK_COMPLETED, completed ? 1 : 0);
        database.update(TaskDBHelper.TABLE_TASKS, values, TaskDBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)});
    }

    public void deleteTask(long taskId) {
        // Delete the task from the database using its ID
        database.delete(TaskDBHelper.TABLE_TASKS, TaskDBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)});
    }

    public Cursor getAllTasksCursor() {
        return database.query(TaskDBHelper.TABLE_TASKS, null, null, null, null, null, null);
    }

    @SuppressLint("Range")
    Task cursorToTask(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getLong(cursor.getColumnIndex(TaskDBHelper.COLUMN_ID)));
        task.setTaskName(cursor.getString(cursor.getColumnIndex(TaskDBHelper.COLUMN_TASK_NAME)));
        return task;
    }
}

