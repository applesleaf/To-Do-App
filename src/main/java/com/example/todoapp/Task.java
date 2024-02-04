package com.example.todoapp;
public class Task {
    private long id;
    private String taskName;
    private boolean completed; // Add completed property

    public Task() {
        // Default constructor
    }

    public Task(long id, String taskName) {
        this.id = id;
        this.taskName = taskName;
    }

    // Getter and setter for id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // Getter and setter for taskName
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    // Getter and setter for completed
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return taskName; // Override toString method for easy printing or displaying in UI
    }
}

