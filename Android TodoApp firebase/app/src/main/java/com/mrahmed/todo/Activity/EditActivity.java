package com.mrahmed.todo.Activity;

import android.app.DatePickerDialog;
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

public class EditActivity extends AppCompatActivity {

    private EditText editTitleInput, editDescriptionInput, editDate;
    private Spinner editSpinnerTodoType;
    private RadioGroup editRadioGroupPriority;
    private Button btnSave;
    private TodoModel todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize views using the IDs from activity_edit.xml
        editTitleInput = findViewById(R.id.editTitleInput);
        editDescriptionInput = findViewById(R.id.editDescriptionInput);
        editDate = findViewById(R.id.editDate);
        editSpinnerTodoType = findViewById(R.id.editSpinnerTodoType);
        editRadioGroupPriority = findViewById(R.id.editRadioGroupPriority);
        btnSave = findViewById(R.id.btnSave);

        // Get the todo item data from the Intent
        todo = (TodoModel) getIntent().getSerializableExtra("todo");

        // Populate the input fields with the todo item data
        editTitleInput.setText(todo.getTitle());
        editDescriptionInput.setText(todo.getDescription());
        editDate.setText(todo.getDate());

        // Set the spinner selection based on the todo type
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.todo_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editSpinnerTodoType.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(todo.getTodoType());
        editSpinnerTodoType.setSelection(spinnerPosition);

        editDate.setOnClickListener(v -> showDatePickerDialog());

        btnSave.setOnClickListener(v -> updateTodo());

        // Set the radio button selection based on the priority
        switch (todo.getPriority()) {
            case "Priority High":
                editRadioGroupPriority.check(R.id.radioHigh);
                break;
            case "Priority Medium":
                editRadioGroupPriority.check(R.id.radioMedium);
                break;
            case "Priority Low":
                editRadioGroupPriority.check(R.id.radioLow);
                break;
        }
    }
    private void showDatePickerDialog() {
        // Get the current date from the existing date
        String[] dateParts = editDate.getText().toString().split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-indexed
        int day = Integer.parseInt(dateParts[2]);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String dateString = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            editDate.setText(dateString);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateTodo() {
        String title = editTitleInput.getText().toString().trim();
        String description = editDescriptionInput.getText().toString().trim();
        String selectedTodoType = editSpinnerTodoType.getSelectedItem().toString();
        String selectedPriority = "";

        int selectedPriorityId = editRadioGroupPriority.getCheckedRadioButtonId();
        if (selectedPriorityId != -1) {
            RadioButton selectedPriorityButton = findViewById(selectedPriorityId);
            selectedPriority = selectedPriorityButton.getText().toString();
        } else {
            Toast.makeText(this, "Please select a priority", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the todo item in Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("todos");
        databaseReference.child(todo.getFirebaseKey()).setValue(new TodoModel(todo.getId(), title, description, editDate.getText().toString(), selectedTodoType, selectedPriority))
                .addOnSuccessListener(aVoid -> {
                    // Todo updated successfully
                    Toast.makeText(EditActivity.this, "Todo updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish EditActivity
                })
                .addOnFailureListener(e -> {
                    // Handle error
                    showError("Failed to update todo: " + e.getMessage());
                });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}