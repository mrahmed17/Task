package com.mrahmed.todo.Model;

/**
 * @Project: Todo
 * @Author: Md. Raju Ahmed
 * @Created at: 12/13/2024
 */

public class TodoModel {

    private int id;
    private String title;
    private String description;
    private String date;
    private String todoType;
    private String priority;

    public TodoModel(int id, String title, String description, String date, String type, String priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.todoType = type;
        this.priority = priority;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String  date) { this.date = date; }

    public String getTodoType() { return todoType; }
    public void setTodoType(String todoType) { this.todoType = todoType; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }


}