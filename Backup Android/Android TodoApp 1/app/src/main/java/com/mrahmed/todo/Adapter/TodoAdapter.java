package com.mrahmed.todo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrahmed.todo.Model.TodoModel;
import com.mrahmed.todo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @Project: Todo
 * @Author: Md. Raju Ahmed
 * @Created at: 12/13/2024
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{
    private List<TodoModel> todoList;
    private Context context;

    public TodoAdapter(List<TodoModel> todoList, Context context) {
        this.todoList = todoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(com.mrahmed.todo.R.layout.todo_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoModel todo = todoList.get(position);
        holder.textTitle.setText(todo.getTitle());
        holder.textDescription.setText(todo.getDescription());
        holder.textTodoType.setText("Type: " + todo.getTodoType());
        holder.textPriority.setText("Priority: " + todo.getPriority().replace("Priority ", ""));
//        holder.textPriority.setText("Priority: " + todo.getPriority());
//        holder.textDate.setText("Date: " + todo.getDate());

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(todo.getDate());

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String formattedDate = outputFormat.format(date);

            holder.textDate.setText("Date: " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.textDate.setText("Date: Invalid Date Format");
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textDescription;
        TextView textTodoType;
        TextView textPriority;
        TextView textDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle=itemView.findViewById(R.id.todoTitle);
            textDescription=itemView.findViewById(R.id.todoDescription);
            textTodoType =itemView.findViewById(R.id.todoType);
            textPriority=itemView.findViewById(R.id.todoPriority);
            textDate=itemView.findViewById(R.id.todoDate);
        }
    }
}
