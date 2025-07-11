package com.example.taskdeadlinetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.taskdeadlinetracker.module.DatabaseClient;
import com.example.taskdeadlinetracker.module.Entity_User;
import com.example.taskdeadlinetracker.module.UserDao;
import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginUsername, etLoginPassword;
    private Button btnLogin;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        userDao = DatabaseClient.getInstance(this).getAppDatabase().userDao();

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

        AsyncTask.execute(() -> {
            Entity_User user = userDao.findByUsernameOrEmail(input, input);
            if (user == null) {
                runOnUiThread(() -> Toast.makeText(this, "Sai tên đăng nhập/email hoặc mật khẩu!", Toast.LENGTH_SHORT).show());
            } else if (!BCrypt.checkpw(password, user.getPasswordHash())) {
                runOnUiThread(() -> Toast.makeText(this, "Sai tên đăng nhập/email hoặc mật khẩu!", Toast.LENGTH_SHORT).show());
            } else {
                // Lưu session vào SharedPreferences
                SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("user_id", user.getId());
                editor.putString("username", user.getUsername());
                editor.putLong("login_time", System.currentTimeMillis());
                editor.apply();

                runOnUiThread(() -> {
                    // Chuyển sang MainActivity nếu login thành công
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                });
            }
        });
    }
}
