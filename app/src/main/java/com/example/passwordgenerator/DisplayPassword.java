package com.example.passwordgenerator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DisplayPassword extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PasswordDbHelper dbHelper;
    private Button clearAllPassword;
    private PasswordsAdapter adapter;
    private View emptyView; // Change to View for empty view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_password);
        clearAllPassword = findViewById(R.id.clearAllPassword);
        emptyView = findViewById(R.id.emptyView); // Initialize the emptyView

        dbHelper = new PasswordDbHelper(this);

        clearAllPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to clear all passwords from the database
                clearAllPasswords();
                // Refresh the RecyclerView to reflect the changes
                adapter.clearData(); // Custom method to clear RecyclerView data
                adapter.notifyDataSetChanged();
                Toast.makeText(DisplayPassword.this, "Password Cleared", Toast.LENGTH_SHORT).show();

                // Show the empty view since there are no passwords now
                emptyView.setVisibility(View.VISIBLE);
            }
        });

        setupRecyclerView();
    }

    private void retrieveAndShowPasswords() {
        List<String> savedPasswords = retrieveSavedPasswords();

        if (savedPasswords.isEmpty()) {
            // Show the empty view when there are no passwords
            emptyView.setVisibility(View.VISIBLE);
        } else {
            // Hide the empty view when there are passwords to display
            emptyView.setVisibility(View.GONE);
        }

        // Update the RecyclerView with the saved passwords
        adapter = new PasswordsAdapter(savedPasswords);
        recyclerView.setAdapter(adapter);
    }

    private List<String> retrieveSavedPasswords() {
        List<String> savedPasswords = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                PasswordDbHelper.COLUMN_PASSWORD
        };

        Cursor cursor = db.query(
                PasswordDbHelper.TABLE_PASSWORDS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String password = cursor.getString(cursor.getColumnIndexOrThrow(PasswordDbHelper.COLUMN_PASSWORD));
                savedPasswords.add(password);
            }
            cursor.close();
        }

        db.close();

        Log.d("SavedPasswords", savedPasswords.toString()); // Add this line for debugging

        return savedPasswords;
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.passwordsRecyclerView);
        // Call the retrieveAndShowPasswords method to set up the RecyclerView
        retrieveAndShowPasswords();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void clearAllPasswords() {
        // Get a writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Delete all records from the passwords table
        db.delete(PasswordDbHelper.TABLE_PASSWORDS, null, null);

        // Close the database
        db.close();
    }


}