package com.example.taskdeadlinetracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

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

        // Kiểm tra tài khoản đã tồn tại chưa (nếu dùng SharedPreferences chỉ đăng ký 1 user)
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUsername = prefs.getString("username", null);
        String savedEmail = prefs.getString("email", null);

        if ((username.equals(savedUsername)) || (email.equals(savedEmail))) {
            Toast.makeText(this, "Tên đăng nhập hoặc email đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu thông tin đăng ký vào SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.putString("email", email);
        editor.putString("password", password); // Nếu muốn bảo mật hơn, bạn có thể mã hóa ở đây
        editor.apply();

        Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
        finish(); // Quay về LoginActivity
    }
}
