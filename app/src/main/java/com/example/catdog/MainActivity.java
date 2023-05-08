package com.example.catdog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private ArrayList<Animal> animalList = new ArrayList<>();
    private static final int REQUEST_CODE_ADD_ANIMAL = 1;
    private DatabaseReference dbRef;
    private Uri imageUri;
    private ImageView animalImageView;
    //private Button addButton,addnotif;
    private ImageButton addButton,addnotif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        animalList = new ArrayList<>();
        animalAdapter = new AnimalAdapter(animalList);
        recyclerView.setAdapter(animalAdapter);
      // FloatingActionButton addButton = findViewById(R.id.fab_add);
        addButton=findViewById(R.id.fab_add);
        addnotif=findViewById(R.id.btn_notif);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animalList.size() >= 10) {
                    Toast.makeText(MainActivity.this, "Количество животных превышено :)", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    startActivity(intent);
                }
            }
        });
        addnotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Notification.class);
                startActivity(intent);
            }
        });


        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("animals");
        ChildEventListener childEventListener= new ChildEventListener() { public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
            Animal animal = dataSnapshot.getValue(Animal.class);
            animalList.add(animal);
            animalAdapter.notifyDataSetChanged();
        }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot,  String s) {
                Animal animal = dataSnapshot.getValue(Animal.class);
                for (int i = 0; i < animalList.size(); i++) {
                    if (animalList.get(i).getKey().equals(dataSnapshot.getKey())) {
                        animalList.set(i, animal);
                        animalAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }


            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < animalList.size(); i++) {
                    if (animalList.get(i).getKey().equals(dataSnapshot.getKey())) {
                        animalList.remove(i);
                        animalAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }


            public void onChildMoved(@NonNull DataSnapshot dataSnapshot,  String s) {
                // Обработка перемещения данных
            }


            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок чтения из базы данных
                Log.e("MainActivity", "Failed to read animals.", databaseError.toException());
            }
        };

            databaseRef.addChildEventListener(childEventListener);

    }
}


