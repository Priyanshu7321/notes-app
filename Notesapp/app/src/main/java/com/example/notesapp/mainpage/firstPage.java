package com.example.notesapp.mainpage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.app.DialogCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.MainActivity;
import com.example.notesapp.MyDbClass;
import com.example.notesapp.R;
import com.example.notesapp.RecyclerAdapter;
import com.example.notesapp.individualNoteItem;
import com.example.notesapp.notePage;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class firstPage extends AppCompatActivity {

    EditText search;
    ImageButton profile;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    List<individualNoteItem> arrayList;
    MyDbClass myDbClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        search = findViewById(R.id.search);
        profile = findViewById(R.id.profile);
        myDbClass = new MyDbClass(firstPage.this);
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerAdapter(firstPage.this, arrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        floatingActionButton = findViewById(R.id.flbt1);

        fetchData();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchResult(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        floatingActionButton.setOnClickListener(v -> showDialog());
    }

    private void fetchResult(String s) {
        List<individualNoteItem> tempList=new ArrayList<>();
        for (individualNoteItem item : arrayList) {
            if (item.title.toLowerCase().contains(s.toLowerCase()) ||
                    item.subtitle.toLowerCase().contains(s.toLowerCase()) ||
                    item.content.toLowerCase().contains(s.toLowerCase())) {
                tempList.add(item);
            }
        }
        adapter.updateList(tempList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAllNotes();
    }

    private void fetchData() {
        arrayList.clear();
        arrayList.addAll(myDbClass.getAll());
        adapter.notifyDataSetChanged();
        Log.d("firstPage", "Fetched data size: " + arrayList.size());
    }

    public void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);

        MaterialButton mtbtOk = dialog.findViewById(R.id.ok);
        MaterialButton mtbtCancel = dialog.findViewById(R.id.cancel);
        EditText title = dialog.findViewById(R.id.title);
        EditText subtitle = dialog.findViewById(R.id.subtitle);

        if (mtbtOk != null) {
            mtbtOk.setOnClickListener(v -> {
                if (!title.getText().toString().isEmpty()) {
                    Intent intent = new Intent(firstPage.this, notePage.class);
                    intent.putExtra("title", title.getText().toString());
                    intent.putExtra("subtitle", subtitle.getText().toString());
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }
            });
        }
        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (mtbtCancel != null) {
            mtbtCancel.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }

    public void showAllNotes() {
        fetchData();
    }
}