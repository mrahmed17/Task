package com.mrahmed.todo.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mrahmed.todo.Adapter.TodoAdapter;
import com.mrahmed.todo.Model.TodoModel;
import com.mrahmed.todo.R;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.Toast;

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
    private List<TodoModel> todoDataList;

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

        todoList = findViewById(R.id.noticeList);
        todoList.setLayoutManager(new LinearLayoutManager(this));
        todoDataList = new ArrayList<>();
        todoAdapter = new TodoAdapter(todoDataList, this);
        todoList.setAdapter(todoAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("todos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                todoDataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TodoModel todo = snapshot.getValue(TodoModel.class);
                    todoDataList.add(todo);
                }
                todoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                showError("Failed to retrieve data: " + databaseError.getMessage());
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
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}