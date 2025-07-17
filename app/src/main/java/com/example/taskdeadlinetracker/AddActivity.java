package com.example.taskdeadlinetracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskdeadlinetracker.module.Entity_Task;
import com.example.taskdeadlinetracker.module.Entity_User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private TextInputLayout tilTitle;
    private EditText etTitle, etDescription;
    private RadioGroup rgPriority;
    private TextView tvDeadline, tvDeadlineError;
    private LinearLayout llDeadline;
    private Switch swCompleted;
    private Button btnCancel, btnSave;
    private Entity_User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        // lấy ScrollView để scroll khi lỗi
        scrollView      = findViewById(R.id.addTask);
        tilTitle        = findViewById(R.id.tilTitle);
        etTitle         = findViewById(R.id.etTitle);
        etDescription   = findViewById(R.id.etDescription);
        rgPriority      = findViewById(R.id.rgPriority);
        tvDeadline      = findViewById(R.id.tvDeadline);
        tvDeadlineError = findViewById(R.id.tvDeadlineError);
        llDeadline      = findViewById(R.id.llDeadline);
        swCompleted     = findViewById(R.id.swCompleted);
        btnCancel       = findViewById(R.id.btnCancel);
        btnSave         = findViewById(R.id.btnSave);

        // Nhận user demo
        user = (Entity_User) getIntent().getSerializableExtra("user");


        llDeadline.setOnClickListener(v -> showDatePicker());
        btnCancel .setOnClickListener(v -> exitTask());
        btnSave   .setOnClickListener(v -> saveTask());
    }

    private void exitTask() {
        boolean isTitleEmpty    = etTitle.getText().toString().trim().isEmpty();
        boolean isDescEmpty     = etDescription.getText().toString().trim().isEmpty();
        String  deadlineStr     = tvDeadline.getText().toString().trim();
        boolean isDeadlineDefault = deadlineStr.isEmpty() || deadlineStr.equals("Chọn ngày");

        if (isTitleEmpty && isDescEmpty && isDeadlineDefault) {
            finish();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận thoát")
                .setMessage("Bạn có chắc chắn muốn thoát khi task này chưa được lưu không?")
                .setPositiveButton("Thoát", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("Ở lại", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void saveTask() {
        // reset lỗi cũ
        tilTitle.setError(null);
        tvDeadlineError.setVisibility(View.GONE);

        String title       = etTitle.getText().toString().trim();
        String desc        = etDescription.getText().toString().trim();
        int    checkedId   = rgPriority.getCheckedRadioButtonId();
        String priority    = (checkedId == R.id.rbLow)  ? "LOW"
                : (checkedId == R.id.rbHigh) ? "HIGH"
                : "MEDIUM";
        String deadlineStr = tvDeadline.getText().toString().trim();
        boolean isCompleted = swCompleted.isChecked();

        boolean hasError = false;

        // 1) Kiểm tra Title
        if (title.isEmpty()) {
            tilTitle.setError("Tiêu đề không được bỏ trống!");
            hasError = true;
        }

        // 2) Kiểm tra Deadline
        if (deadlineStr.isEmpty() || deadlineStr.equals("Chọn ngày")) {
            tvDeadlineError.setText("Ngày hoàn thành không được bỏ trống!");
            tvDeadlineError.setVisibility(View.VISIBLE);
            hasError = true;
        }

        if (hasError) {
            // scroll lên để người dùng thấy lỗi
            scrollView.post(() -> scrollView.smoothScrollTo(0, tilTitle.getTop()));
            return;
        }

        // Tạo task mới
        Entity_Task task = new Entity_Task();
        task.setId((int) (System.currentTimeMillis() & 0xffffffff));
        task.setUserId(user.getId());
        task.setTitle(title);
        task.setDescription(desc);
        task.setPriority(priority);
        task.setIsComplete(isCompleted);
        task.setCreatedAt(new Date());
        task.setUpdatedAt(new Date());

        // Parse deadlineStr thành Date (dd/MM/yyyy)
        try {
            String[] parts = deadlineStr.split("/");
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[1]) - 1,
                    Integer.parseInt(parts[0]));
            task.setDeadline(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent result = new Intent();
        result.putExtra("task", task);
        setResult(RESULT_OK, result);
        finish();
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (DatePicker view, int y, int m, int d) ->
                        tvDeadline.setText(String.format("%02d/%02d/%04d", d, m + 1, y)),
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
