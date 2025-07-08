package com.example.taskdeadlinetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private RadioGroup rgPriority;
    private TextView tvDeadline;
    private LinearLayout llDeadline;
    private Switch swCompleted;
    private Button btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1. Bật edge‑to‑edge
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_addtask);

        // 2. Tự động padding để né status & nav bars
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.addTask),
                (v, insets) -> {
                    Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
                    return insets;
                }
        );

        // 3. Ánh xạ tất cả View
        etTitle       = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        rgPriority    = findViewById(R.id.rgPriority);
        tvDeadline    = findViewById(R.id.tvDeadline);
        llDeadline    = findViewById(R.id.llDeadline);
        swCompleted   = findViewById(R.id.swCompleted);
        btnCancel     = findViewById(R.id.btnCancel);
        btnSave       = findViewById(R.id.btnSave);

        // 4. Mở DatePicker khi click vào cả vùng
        llDeadline.setOnClickListener(v -> showDatePicker());

        // 5. Hủy quay về Main
        btnCancel.setOnClickListener(v -> finish());

        // 6. Lưu Task (chờ bạn cắm phần lưu trữ vào đây)
        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc  = etDescription.getText().toString().trim();

            // Priority: 0=Low,1=Medium,2=High
            int checkedId = rgPriority.getCheckedRadioButtonId();
            int priorityIndex = 1; // default medium
            if (checkedId == R.id.rbLow)    priorityIndex = 0;
            else if (checkedId == R.id.rbHigh) priorityIndex = 2;

            String deadlineStr = tvDeadline.getText().toString();
            boolean isCompleted = swCompleted.isChecked();

            // TODO lưu trữ: tạo Entity_Task và insert/update DB hoặc SharedPreferences
            // Ví dụ:
            // Entity_Task task = new Entity_Task(...);
            // task.setTitle(title);
            // task.setDescription(desc);
            // task.setDeadline(parsedDateFrom(deadlineStr));
            // task.setPriority(Priority.values()[priorityIndex]);
            // task.setCompleted(isCompleted);
            // yourRepository.insert(task);

            // Sau khi lưu xong, finish() hoặc trả kết quả về MainActivity
            finish();
        });
    }

    private void showDatePicker() {
        // Lấy ngày hiện tại làm mặc định
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    // Cập nhật TextView với định dạng dd/MM/yyyy
                    String s = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    tvDeadline.setText(s);
                },
                y, m, d
        );
        dlg.show();
    }

    // Nếu cần parse String thành Date, bạn có thể thêm helper:
    // private Date parsedDateFrom(String s) { ... }
}
