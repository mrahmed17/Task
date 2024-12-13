package com.mrahmed.todo.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.mrahmed.todo.Adapter.TodoAdapter;
import com.mrahmed.todo.ApiClient.RetrofitInstance;
import com.mrahmed.todo.ApiSocket.TodoApi;
import com.mrahmed.todo.Model.TodoModel;
import com.mrahmed.todo.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * @Project: Todo
 * @Author: Md. Raju Ahmed
 * @Created at: 12/13/2024
 */

public class GetActivity extends AppCompatActivity {
    private RecyclerView todoList;
    private TodoAdapter todoAdapter;
    private FloatingActionButton addTodoFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addTodoFab = findViewById(R.id.addTodoFab);
        addTodoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        MaterialButtonToggleGroup dayNightToggleGroup = findViewById(R.id.dayNightToggleGroup);

        dayNightToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.dayModeButton) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    } else if (checkedId == R.id.nightModeButton) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                }
            }
        });

        todoList = findViewById(R.id.noticeList);
        todoList.setLayoutManager(new LinearLayoutManager(this));
        TodoApi todoApi = RetrofitInstance.getRetrofitInstance().create(TodoApi.class);
        Call<List<TodoModel>> call = todoApi.getTodos();
        call.enqueue(new Callback<List<TodoModel>>() {
            @Override
            public void onResponse(Call<List<TodoModel>> call, Response<List<TodoModel>> response) {
                if (response.isSuccessful()) {
                    List<TodoModel> todoList = response.body();

                    Collections.sort(todoList, new Comparator<TodoModel>() {
                        @Override
                        public int compare(TodoModel todo1, TodoModel todo2) {
                            return Integer.compare(todo2.getId(), todo1.getId());
                        }
                    });

                    // Set up RecyclerView with the adapter
                    todoAdapter = new TodoAdapter(todoList, getApplicationContext());
                    GetActivity.this.todoList.setAdapter(todoAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<TodoModel>> call, Throwable t) {
            }
        });
    }
}