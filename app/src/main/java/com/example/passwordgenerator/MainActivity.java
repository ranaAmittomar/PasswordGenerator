package com.example.passwordgenerator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView passwordTextView;
    private TextView checkPassword;
    private Button generateButton;
    private ImageView savePasswordIcon;
    private TextView clicktoSaveTextView;
    private SecureRandom random = new SecureRandom();
    private List<Character> shuffledCharacters;
    private int shuffleInterval = 100;
    private Handler shuffleHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        passwordTextView = findViewById(R.id.passwordTextView);
        generateButton = findViewById(R.id.generateButton);
        savePasswordIcon = findViewById(R.id.savePasswordIcon);
        clicktoSaveTextView = findViewById(R.id.clicktoSave);
        checkPassword = findViewById(R.id.checkPassword);
        shuffledCharacters = new ArrayList<>();

        checkPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DisplayPassword.class));
            }
        });
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Generate a random password
                String password = generatePassword();
                passwordTextView.setText(""); // Clear the TextView

                // Start shuffling animation
                updateClickToSaveText("It's Generating"); // Update text during generation
                startShuffleAnimation(password);
            }
        });

        savePasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract the generated password from the passwordTextView
                String generatedPassword = passwordTextView.getText().toString();

                // Save the generated password to the database
                savePasswordToDatabase(generatedPassword);

                // Show a toast message
                Toast.makeText(MainActivity.this, "Saved Password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, DisplayPassword.class));
            }
        });

    }

    private void savePasswordToDatabase(String password) {
        PasswordDbHelper dbHelper = new PasswordDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PasswordDbHelper.COLUMN_PASSWORD, password);

        // Insert the password into the database
        long newRowId = db.insert(PasswordDbHelper.TABLE_PASSWORDS, null, values);

        // Close the database
        db.close();
    }


    private String generatePassword() {
        int length = 12;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    private void startShuffleAnimation(final String password) {
        // Shuffle the characters of the password
        shuffledCharacters.clear();
        for (char c : generatePassword().toCharArray()) {
            shuffledCharacters.add(c);
        }

        // Create a handler for updating the passwordTextView with shuffled characters
        shuffleHandler = new Handler() {
            private long startTime = System.currentTimeMillis();

            @Override
            public void handleMessage(Message msg) {
                // Calculate the elapsed time
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;

                // If the elapsed time is less than the desired duration (e.g., 800 milliseconds),
                // continue the shuffle animation
                if (elapsedTime < 800) {
                    // Shuffle the characters randomly
                    Collections.shuffle(shuffledCharacters, random);

                    // Convert shuffled characters back to a string
                    StringBuilder shuffledPassword = new StringBuilder();
                    for (char c : shuffledCharacters) {
                        shuffledPassword.append(c);
                    }

                    // Update the passwordTextView with shuffled characters
                    passwordTextView.setText(shuffledPassword.toString());

                    // Update the clicktoSaveTextView text to "It's Generating"
                    updateClickToSaveText("It's Generating");

                    // Schedule the next update
                    sendEmptyMessageDelayed(0, shuffleInterval);
                } else {
                    // After the desired duration, reveal the generated password
                    passwordTextView.setText(generatePassword());

                    // Revert the clicktoSaveTextView text to its original state
                    updateClickToSaveText("Click To Save Your Password");
                }
            }
        };

        // Start the shuffle animation by sending a delayed message to the handler
        shuffleHandler.sendEmptyMessageDelayed(0, shuffleInterval);
    }


    private void updateClickToSaveText(String newText) {
        clicktoSaveTextView.setText(newText);
    }
}