// MainActivity.java
package com.example.taskdeadlinetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskdeadlinetracker.module.Entity_Task;
import com.example.taskdeadlinetracker.module.Entity_User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Entity_Task> tasks = new ArrayList<>();
    private Entity_User user;
    private ActivityResultLauncher<Intent> addTaskLauncher;
    private RecyclerView rvTasks;
    private TaskAdapter adapter;

    private TextView tvCompletedTasks;
    private TextView tvPendingTasks;
    private TextView tvTotalTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Điều chỉnh padding theo system bars
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
                    return insets;
                }
        );

        // 1. Demo user
        user = new Entity_User();
        user.setId(1);
        user.setUsername("demo");
        user.setEmail("demo@example.com");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // 2. Views thống kê
        tvTotalTasks     = findViewById(R.id.tvTotalTasks);
        tvPendingTasks   = findViewById(R.id.tvPendingTasks);
        tvCompletedTasks = findViewById(R.id.tvCompletedTasks);

        // 3. RecyclerView + Adapter
        rvTasks = findViewById(R.id.recyclerViewTasks);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter();
        rvTasks.setAdapter(adapter);

        // 4. Launcher nhận task mới
        addTaskLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Entity_Task newTask = result.getData().getParcelableExtra("task");
                        if (newTask != null) {
                            tasks.add(0, newTask);
                            adapter.notifyItemInserted(0);
                            rvTasks.scrollToPosition(0);
                            updateTaskStats();
                        }
                    }
                }
        );

        // 5. Nút thêm mới
        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddActivity.class);
            intent.putExtra("user", user);
            addTaskLauncher.launch(intent);
        });

        // 6. Tính thống kê lần đầu
        updateTaskStats();
    }

    /** Tính và hiển thị lại Total / Pending / Completed **/
    private void updateTaskStats() {
        int total     = tasks.size();
        int pending   = 0;
        int complete  = 0;
        for (Entity_Task t : tasks) {
            if (t.isComplete()) complete++;
            else                pending++;
        }
        tvTotalTasks.setText("Tổng: " + total + " task");
        tvPendingTasks.setText("Đang thực hiện: " + pending);
        tvCompletedTasks.setText("Hoàn thành: " + complete);
    }

    /** Adapter hiển thị Task với CheckBox và click item **/
    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
        private final SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy");

        @NonNull @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_task, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
            Entity_Task t = tasks.get(pos);
            h.tvTitle.setText(t.getTitle());
            h.tvDeadline.setText("Hạn: " + dateFmt.format(t.getDeadline()));
            h.tvPriority.setText("Ưu tiên: " + t.getPriority());

            h.cbDone.setOnCheckedChangeListener(null);
            h.cbDone.setChecked(t.isComplete());
            h.tvStatus.setText(t.isComplete() ? "Hoàn thành" : "Đang làm");

            h.cbDone.setOnCheckedChangeListener((buttonView, checked) -> {
                t.setIsComplete(checked);
                h.tvStatus.setText(checked ? "Hoàn thành" : "Đang làm");
                updateTaskStats();
            });

            // Click cả item → DetailActivity
            h.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("task", t);
                startActivity(intent);
            });
        }

        @Override public int getItemCount() { return tasks.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDeadline, tvPriority, tvStatus;
            CheckBox cbDone;
            ViewHolder(@NonNull View v) {
                super(v);
                cbDone     = v.findViewById(R.id.cbDone);
                tvTitle    = v.findViewById(R.id.item_tvTitle);
                tvDeadline = v.findViewById(R.id.item_tvDeadline);
                tvPriority = v.findViewById(R.id.item_tvPriority);
                tvStatus   = v.findViewById(R.id.item_tvStatus);
            }
        }
    }
}
