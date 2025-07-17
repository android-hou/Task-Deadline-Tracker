package com.example.taskdeadlinetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginUsername, etLoginPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // === GỢI Ý USERNAME/EMAIL ĐÃ ĐĂNG KÝ LÊN Ô NHẬP ===
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String lastUsername = prefs.getString("username", "");
        etLoginUsername.setText(lastUsername);

        btnLogin.setOnClickListener(v -> loginUser());

        TextView tvRegister = findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String input = etLoginUsername.getText().toString().trim();
        String password = etLoginPassword.getText().toString();

        if (input.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy thông tin đã lưu trong SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUsername = prefs.getString("username", null);
        String savedEmail = prefs.getString("email", null);
        String savedPassword = prefs.getString("password", null);

        // Kiểm tra đăng nhập bằng username hoặc email và mật khẩu
        if ((input.equals(savedUsername) || input.equals(savedEmail)) && password.equals(savedPassword)) {
            // Đăng nhập thành công: Lưu thông tin đăng nhập (nếu cần)
            SharedPreferences sessionPrefs = getSharedPreferences("user_session", MODE_PRIVATE);
            SharedPreferences.Editor editor = sessionPrefs.edit();
            editor.putString("username", savedUsername);
            editor.putLong("login_time", System.currentTimeMillis());
            editor.apply();

            // Chuyển sang MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Sai tên đăng nhập/email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
        }
    }
}
