package com.example.unilib;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


import androidx.appcompat.app.AppCompatActivity;

public class signup extends AppCompatActivity {
//    private EditText usernameInput, emailInput, passwordInput, rePasswordInput;
    //private Spinner roleSpinner;
    //private DBHelper dbHelper;

    private EditText emailInput, passwordInput;
    private FirebaseAuth mAuth;
    private Button signupButton;
    private TextView loginRedirectText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        usernameInput = findViewById(R.id.userName);
//        emailInput = findViewById(R.id.email);
//        passwordInput = findViewById(R.id.password);
//        rePasswordInput = findViewById(R.id.repassword);
//        roleSpinner = findViewById(R.id.roles);
//        signupButton = findViewById(R.id.Register);
//        dbHelper = new DBHelper(this);

        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        signupButton = findViewById(R.id.Register);
        loginRedirectText = findViewById(R.id.existingUserLink);

        signupButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (!isValidEmail(email)) {
                Toast.makeText(signup.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isPasswordStrong(password)) {
                Toast.makeText(signup.this, "Password must be at least 8 characters long and include uppercase, lowercase, digit, and special character.", Toast.LENGTH_LONG).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new
                                    Intent(signup.this, MainActivity.class));
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // This means the email is already in use
                                Toast.makeText(signup.this, "This email is already registered. Please log in.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(signup.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }                        }
                    });
        });
        loginRedirectText.setOnClickListener(v -> startActivity(new
                Intent(signup.this, login.class)));
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordStrong(String password) {
        return password.length() >= 8
                && password.chars().anyMatch(Character::isUpperCase)
                && password.chars().anyMatch(Character::isLowerCase)
                && password.chars().anyMatch(Character::isDigit)
                && password.chars().anyMatch(c -> "!@#$%^&*()-_=+[]{}|;:,.<>?/".indexOf(c) >= 0);
    }
}
//
//
//        signupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = usernameInput.getText().toString().trim();
//                String email = emailInput.getText().toString().trim();
//                String password = passwordInput.getText().toString().trim();
//                String rePassword = rePasswordInput.getText().toString().trim();
//                String role = roleSpinner.getSelectedItem().toString();
//
//                // Validate input
//                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty() || role.isEmpty()) {
//                    Toast.makeText(signup.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (!password.equals(rePassword)) {
//                    Toast.makeText(signup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                // Check if the username is already taken
//                if (isUsernameTaken(username)) {
//                    Toast.makeText(signup.this, "Username is already taken", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                // Handle user roles
//                if (role.equals("Student") || role.equals("Faculty")) {
//                    registerUser(username, email, password, role);
//                } else if (role.equals("Club Authority")) {
//                    handleClubAuthorityRequest(username, email, password);
//                }
//            }
//        });
//    }
//
//    private void registerUser(String username, String email, String password, String role) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("username", username);
//        values.put("email", email);
//        values.put("password", password);
//        values.put("role", role);
//        long newRowId = db.insert("users", null, values);
//
//        if (newRowId != -1) {
//            // Save login credentials using SharedPreferences
//            SharedPreferences sharedPreferences = getSharedPreferences("UniCalPrefs", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("email", email);
//            editor.putString("role", role);
//            editor.apply();
//
//            // Redirect to the main/calendar page
//            Intent intent = new Intent(signup.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            Toast.makeText(signup.this, "Signup failed", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void handleClubAuthorityRequest(String username, String email, String password) {
//        // Store the request in the 'requests' table for admin approval
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("username", username);
//        values.put("email", email);
//        values.put("password", password);
//        values.put("role", "Club Authority");
//        long newRowId = db.insert("requests", null, values);
//
//        if (newRowId != -1) {
//            Toast.makeText(signup.this, "Request sent for admin approval", Toast.LENGTH_SHORT).show();
//            finish();  // Close signup activity
//        } else {
//            Toast.makeText(signup.this, "Failed to send request", Toast.LENGTH_SHORT).show();
//        }
//    }
//    private boolean isUsernameTaken(String username) {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String[] columns = {"username"};
//        String selection = "username = ?";
//        String[] selectionArgs = {username};
//
//        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
//
//        boolean isTaken = ((Cursor) cursor).getCount() > 0;
//        cursor.close();
//        return isTaken;
//    }
//
//}
