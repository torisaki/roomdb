package com.example.roomdb2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class ModuleActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRView;
    private ModuleAdapter mAdapter;
    private AppDatabase mDb;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_module);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnAdd = findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(this);
        mRView = findViewById(R.id.moduleList);
        mRView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ModuleAdapter(this);
        mRView.setAdapter(mAdapter);
        mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        mDb.moduleDAO().insert(new Module("android", "ListView trong Android", "ListView trong Android là một thành phần dùng để nhóm nhiều mục (item) view với nhau.", "Android"));
        mDb.moduleDAO().insert(new Module("ios", "Xử lý sự kiện trong iOS", "Xử lý sự kiện trong iOS", "iOS"));
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Module> tasks = mAdapter.getTasks();
                        mDb.moduleDAO().delete(tasks.get(position));
                        retrieveTasks();
                    }
                });
            }
        }).attachToRecyclerView(mRView);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAdd) {
            addModule();
        }
    }
    private void addModule(){
        startActivity(new Intent(ModuleActivity.this, EditModuleActivity.class));
    }
    @Override
    protected void onResume(){
        super.onResume();
        retrieveTasks();
    }
    private void retrieveTasks(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Module> modules = mDb.moduleDAO().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setTasks(modules);
                    }
                });
            }
        });
    }
}