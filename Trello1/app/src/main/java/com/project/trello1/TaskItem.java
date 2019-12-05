package com.project.trello1;

public class TaskItem {

    String taskName;
    String dueTo;

    TaskItem(String _taskName, String _dueTo) {
        taskName = _taskName;
        dueTo = _dueTo;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDueTo() {
        return dueTo;
    }
}