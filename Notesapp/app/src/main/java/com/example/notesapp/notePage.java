package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notesapp.mainpage.firstPage;

public class notePage extends AppCompatActivity {

    AppCompatEditText editText;
    EditText heading;
    int clickType;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        MyDbClass obj=new MyDbClass(notePage.this);
        editText=findViewById(R.id.edittext);
        heading=findViewById(R.id.title);
        Intent intent=getIntent();
        heading.setText(intent.getStringExtra("title"));
        clickType=intent.getIntExtra("type",0);
        if(clickType!=1){
            count=intent.getIntExtra("recycler",1);
            individualNoteItem it=obj.showData(count+1);
            Log.d("recycler position",String.valueOf(count));

            editText.setText(it.content);
            heading.setText(it.title);
        }
        OnBackPressedDispatcher dispatcher=getOnBackPressedDispatcher();

        findViewById(R.id.notePageBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                dispatcher.onBackPressed();
            }
        });

        findViewById(R.id.materialButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals("")){
                    saveData();
                    startActivity(new Intent(notePage.this, firstPage.class));
                    finish();
                }
            }
        });

    }
    public void saveData(){
        MyDbClass objDB=new MyDbClass(this);

        if(clickType==1){
            objDB.addValue(heading.getText().toString(),"",editText.getText().toString());
        }else{
            objDB.updateData(count+1,heading.getText().toString(),"",editText.getText().toString());
        }
    }
}