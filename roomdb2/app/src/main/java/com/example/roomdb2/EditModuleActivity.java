package com.example.roomdb2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

public class EditModuleActivity extends AppCompatActivity {
    private EditText edImage, edName, edDesc, edOs;
    private Button btnSave;
    private int mModuleId;
    private Intent intent;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_module);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").fallbackToDestructiveMigration().build();

        intent = getIntent();
        if(intent != null && intent.hasExtra(Constants.UPDATE_Module_Id)){
            btnSave.setText("Update");
            mModuleId = intent.getIntExtra(Constants.UPDATE_Module_Id, -1);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Module module = mDb.moduleDAO().loadModuleById(mModuleId);
                    populateUI(module);
                }
            });
        }
    }
    private void populateUI(Module module){
        if(module == null){
            return;
        }
        edImage.setText(module.getImage());
        edName.setText(module.getName());
        edDesc.setText(module.getDescription());
        edOs.setText(module.getOs());
    }
    private void initViews(){
        edImage = findViewById(R.id.edImage);
        edName = findViewById(R.id.edName);
        edDesc = findViewById(R.id.edDesc);
        edOs = findViewById(R.id.edOs);

        btnSave = findViewById(R.id.button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick();
            }
        });
    }
    private void onSaveButtonClick(){
        final Module module = new Module(edImage.getText().toString(), edName.getText().toString(), edDesc.getText().toString(), edOs.getText().toString());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(!intent.hasExtra(Constants.UPDATE_Module_Id)){
                    mDb.moduleDAO().insert(module);
                }else{
                    module.setUid(mModuleId);
                    mDb.moduleDAO().update(module);
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