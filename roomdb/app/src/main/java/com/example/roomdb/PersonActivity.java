package com.example.roomdb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PersonActivity extends AppCompatActivity {
    private FloatingActionButton fbAdd;
    private RecyclerView mRView;
    private PersonAdapter mAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_person);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_person), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fbAdd = findViewById(R.id.floatingActionButton);
        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonActivity.this, EditPersonActivity.class));
            }
        });
        mRView = findViewById(R.id.rvPerson);
        mRView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PersonAdapter(this);
        mRView.setAdapter(mAdapter);
        mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").fallbackToDestructiveMigration().build();
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
                        List<Person> tasks = mAdapter.getTasks();
                        mDb.personDAO().delete(tasks.get(position));
                        retrieveTasks();
                    }
                });
            }
        }).attachToRecyclerView(mRView);
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
                final List<Person> persons = mDb.personDAO().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setTasks(persons);
                    }
                });
            }
        });
    }
}