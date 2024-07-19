package com.example.roomdb;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.Objects;

public class EditPersonActivity extends AppCompatActivity {
    private EditText edFirstName, edLastName;
    private Button btnSave;
    private int mPersonId;
    private Intent intent;
    private AppDatabase mDb;

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_person);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database")
                .fallbackToDestructiveMigration()
                .build();
        mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").build();

        intent = getIntent();
        if(intent != null && intent.hasExtra(Constants.UPDATE_Person_Id)){
            btnSave.setText("Update");
            mPersonId = intent.getIntExtra(Constants.UPDATE_Person_Id, -1);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Person person = mDb.personDAO().loadPersonById(mPersonId);
                    populateUI(person);
                }
            });
        }
    }
    private void populateUI(Person person){
        if(person == null){
            return;
        }
        edFirstName.setText(person.getFirstName());
        edLastName.setText(person.getLastName());
    }
    private void initViews(){
        edFirstName = findViewById(R.id.editTextText);
        edLastName = findViewById(R.id.editTextText2);

        btnSave = findViewById(R.id.button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick();
            }
        });
    }
    private void onSaveButtonClick(){
        final Person person = new Person(edFirstName.getText().toString(), edLastName.getText().toString());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(!intent.hasExtra(Constants.UPDATE_Person_Id)){
                    mDb.personDAO().insert(person);
                }else{
                    person.setUid(mPersonId);
                    mDb.personDAO().update(person);
                }
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
