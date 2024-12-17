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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.mrahmed.todo.Model.TodoModel;
import com.mrahmed.todo.R;

import java.util.Calendar;

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
        String selectedPriority = "";

        if (title.isEmpty() || description.isEmpty() || selectedTodoType.equals("Select Todo Type") || selectedPriority.isEmpty()) {
            showError("Please fill in all required fields and select a valid todo type and priority.");
            return;
        }

//        if (selectedTodoType.equals("Select Todo Type")) {
//            Toast.makeText(this, "Please select a valid todo type", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (title.isEmpty() || description.isEmpty()) {
//            Toast.makeText(this, "Title and Description are required", Toast.LENGTH_SHORT).show();
//            return;
//        }

        int selectedPriorityId = radioGroupPriority.getCheckedRadioButtonId();

        if (selectedPriorityId != -1) {
            RadioButton selectedPriorityButton = findViewById(selectedPriorityId);
            selectedPriority = selectedPriorityButton.getText().toString();
        } else {
            Toast.makeText(this, "Please select your priority", Toast.LENGTH_SHORT).show();
            return;
        }

        TodoModel todo = new TodoModel(0, title, description, date.getText().toString(), selectedTodoType, selectedPriority);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("todos");
        String key = databaseReference.push().getKey();
        todo.setFirebaseKey(key);

        databaseReference.child(key).setValue(todo)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(PostActivity.this, "Todo created successfully", Toast.LENGTH_SHORT).show();
                    navigateToGetActivity(); // Navigate to GetActivity after successful creation
                })
                .addOnFailureListener(e -> {
                    showError("Failed to create Todo. Please try again later.");
                });

    }

    private void navigateToGetActivity() {
        Intent intent = new Intent(this, GetActivity.class);
        startActivity(intent);
        finish();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
