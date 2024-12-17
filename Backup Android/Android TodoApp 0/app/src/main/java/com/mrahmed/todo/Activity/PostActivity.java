package com.mrahmed.todo.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mrahmed.todo.ApiClient.RetrofitInstance;
import com.mrahmed.todo.ApiSocket.TodoApi;
import com.mrahmed.todo.Model.TodoModel;
import com.mrahmed.todo.R;

/**
 * @Project: Todo
 * @Author: Md. Raju Ahmed
 * @Created at: 12/13/2024
 */

public class PostActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, date;
    private Spinner spinnerTodoType;
    private RadioGroup radioGroupPriority;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        editTextTitle = findViewById(R.id.titleInput);
        editTextDescription = findViewById(R.id.descriptionInput);
        date = findViewById(R.id.date);
        spinnerTodoType = findViewById(R.id.spinnerTodoType);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        btnSubmit = findViewById(R.id.btnSubmit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.todo_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTodoType.setAdapter(adapter);

        date.setOnClickListener(v -> showDatePickerDialog());

        btnSubmit.setOnClickListener(v -> submitTodo());
    }

    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String dateString = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            date.setText(dateString);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void submitTodo() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        String selectedTodoType = spinnerTodoType.getSelectedItem().toString();

        if (selectedTodoType.equals("Select Todo Type")) {
            Toast.makeText(this, "Please select a valid todo type", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPriorityId = radioGroupPriority.getCheckedRadioButtonId();
        String selectedPriority = "";
        if (selectedPriorityId != -1) {
            RadioButton selectedPriorityButton = findViewById(selectedPriorityId);
            selectedPriority = selectedPriorityButton.getText().toString();
        } else {
            Toast.makeText(this, "Please select a priority", Toast.LENGTH_SHORT).show();
            return;
        }

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Title and Description are required", Toast.LENGTH_SHORT).show();
            return;
        }

        TodoModel todo = new TodoModel(0, title, description, date.getText().toString(), selectedTodoType, selectedPriority);

        TodoApi api = RetrofitInstance.getRetrofitInstance().create(TodoApi.class);
        Call<Void> call = api.createTodo(todo);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PostActivity.this, "Todo created successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostActivity.this, GetActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PostActivity.this, "Failed to create Todo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
