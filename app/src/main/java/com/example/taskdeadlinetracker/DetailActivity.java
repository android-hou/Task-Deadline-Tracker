package com.example.taskdeadlinetracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.taskdeadlinetracker.module.Entity_Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DetailActivity extends AppCompatActivity {
    private Entity_Task task;
    private TextView detailTitle, detailDesc, detailDeadline, detailPriority, detailStatus;
    private ImageView statusIcon;
    private Button btnToggleStatus, btnEdit, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết nhiệm vụ");
        }

        // Get task from intent
        task = getIntent().getParcelableExtra("task");
        if (task == null) {
            finish();
            return;
        }

        initViews();
        setupEventListeners();
        displayTaskInfo();
    }

    private void initViews() {
        detailTitle = findViewById(R.id.detailTitle);
        detailDesc = findViewById(R.id.detailDesc);
        detailDeadline = findViewById(R.id.detailDeadline);
        detailPriority = findViewById(R.id.detailPriority);
        detailStatus = findViewById(R.id.detailStatus);
        statusIcon = findViewById(R.id.statusIcon);

        btnToggleStatus = findViewById(R.id.btnToggleStatus);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void setupEventListeners() {
        btnToggleStatus.setOnClickListener(v -> toggleTaskStatus());
        btnEdit.setOnClickListener(v -> editTask());
        btnDelete.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void displayTaskInfo() {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

        detailTitle.setText(task.getTitle());
        detailDesc.setText(task.getDescription());

        String deadlineText = "Hạn: " + fmt.format(task.getDeadline());
        detailDeadline.setText(deadlineText);
        long daysUntil = getDaysUntilDeadline();
        if (daysUntil < 0) {
            detailDeadline.setTextColor(Color.RED);
        } else if (daysUntil <= 3) {
            detailDeadline.setTextColor(Color.parseColor("#FF9800"));
        } else {
            detailDeadline.setTextColor(Color.parseColor("#4CAF50"));
        }

        String priorityText = "Ưu tiên: " + task.getPriority();
        detailPriority.setText(priorityText);
        switch (task.getPriority().toLowerCase()) {
            case "cao":
                detailPriority.setTextColor(Color.RED);
                break;
            case "trung bình":
                detailPriority.setTextColor(Color.parseColor("#FF9800"));
                break;
            case "thấp":
                detailPriority.setTextColor(Color.parseColor("#4CAF50"));
                break;
        }

        updateStatusDisplay();
    }

    private void updateStatusDisplay() {
        if (task.isComplete()) {
            detailStatus.setText("Hoàn thành");
            detailStatus.setTextColor(Color.parseColor("#4CAF50"));
            statusIcon.setImageResource(R.drawable.ic_check_circle);
            btnToggleStatus.setText("Đánh dấu chưa hoàn thành");
        } else {
            detailStatus.setText("Đang làm");
            detailStatus.setTextColor(Color.parseColor("#FF9800"));
            statusIcon.setImageResource(R.drawable.ic_radio_button_unchecked);
            btnToggleStatus.setText("Đánh dấu hoàn thành");
        }
    }

    private long getDaysUntilDeadline() {
        Date now = new Date();
        long diff = task.getDeadline().getTime() - now.getTime();
        return TimeUnit.MILLISECONDS.toDays(diff);
    }

    private void toggleTaskStatus() {
        task.setComplete(!task.isComplete());
        updateStatusDisplay();

        // TODO: update to database
        Toast.makeText(this,
                task.isComplete() ? "Đã đánh dấu hoàn thành!" : "Đã đánh dấu chưa hoàn thành!",
                Toast.LENGTH_SHORT).show();

        Intent result = new Intent();
        result.putExtra("task", task);
        setResult(RESULT_OK, result);
    }

    private void editTask() {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra("task", task);
        startActivityForResult(intent, 100);
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa nhiệm vụ '" + task.getTitle() + "'?" )
                .setPositiveButton("Xóa", (d, w) -> deleteTask())
                .setNegativeButton("Hủy", null)
                .setIcon(R.drawable.ic_warning)
                .show();
    }

    private void deleteTask() {
        // TODO: delete from database
        Toast.makeText(this, "Đã xóa nhiệm vụ", Toast.LENGTH_SHORT).show();

        Intent result = new Intent();
        result.putExtra("task", task);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (req == 100 && res == RESULT_OK) {
            task = data.getParcelableExtra("task");
            displayTaskInfo();
            Intent result = new Intent();
            result.putExtra("task", task);
            setResult(RESULT_OK, result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareTask() {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        String text = "Nhiệm vụ: " + task.getTitle() + "\n" +
                "Mô tả: " + task.getDescription() + "\n" +
                "Hạn: " + fmt.format(task.getDeadline()) + "\n" +
                "Ưu tiên: " + task.getPriority() + "\n" +
                "Trạng thái: " + (task.isComplete()?"Hoàn thành":"Đang làm");
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, text);
        share.putExtra(Intent.EXTRA_SUBJECT, "Nhiệm vụ: " + task.getTitle());
        startActivity(Intent.createChooser(share, "Chia sẻ nhiệm vụ"));
    }

    private void duplicateTask() {
        Entity_Task copy = new Entity_Task(
                task.getId() + 1000,
                1,
                task.getTitle() + " (Copy)",
                task.getDescription(),
                task.getDeadline(),
                task.getPriority(),
                false
        );
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra("task", copy);
        intent.putExtra("is_duplicate", true);
        startActivityForResult(intent, 100);
        Toast.makeText(this, "Đã sao chép nhiệm vụ", Toast.LENGTH_SHORT).show();
    }
}