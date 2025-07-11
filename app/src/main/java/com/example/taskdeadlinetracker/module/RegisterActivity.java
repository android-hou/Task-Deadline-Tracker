package com.example.taskdeadlinetracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.taskdeadlinetracker.module.DatabaseClient;
import com.example.taskdeadlinetracker.module.Entity_User;
import com.example.taskdeadlinetracker.module.UserDao;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        userDao = DatabaseClient.getInstance(this).getAppDatabase().userDao();

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (username.isEmpty() || username.length() < 3 || username.length() > 20 || !username.matches("^[a-zA-Z0-9_]+$")) {
            etUsername.setError("Tên đăng nhập 3-20 ký tự, không ký tự đặc biệt!");
            etUsername.requestFocus();
            return;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            etEmail.setError("Email không hợp lệ!");
            etEmail.requestFocus();
            return;
        }
        if (password.length() < 8 || !password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*[0-9].*")) {
            etPassword.setError("Mật khẩu tối thiểu 8 ký tự, gồm chữ hoa, thường, số!");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu không khớp!");
            etConfirmPassword.requestFocus();
            return;
        }

        AsyncTask.execute(() -> {
            Entity_User existedUser = userDao.findByUsernameOrEmail(username, email);
            if (existedUser != null) {
                runOnUiThread(() -> Toast.makeText(this, "Tên đăng nhập hoặc email đã tồn tại!", Toast.LENGTH_SHORT).show());
                return;
            }
            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
            Entity_User newUser = new Entity_User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPasswordHash(passwordHash);
            newUser.setCreatedAt(new Date());
            newUser.setUpdatedAt(new Date());

            userDao.insertUser(newUser);

            runOnUiThread(() -> {
                Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
                finish(); // Quay về LoginActivity
            });
        });
    }
}
