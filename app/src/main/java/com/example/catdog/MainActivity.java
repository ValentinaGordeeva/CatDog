package com.example.catdog;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private ArrayList<Animal> animalList = new ArrayList<>();
    private static final int REQUEST_CODE_ADD_ANIMAL = 1;
    private DatabaseReference dbRef;
    private Uri imageUri;
    private ImageView animalImageView;


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
        FloatingActionButton addButton = findViewById(R.id.fab_add);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddActivity.class);
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


