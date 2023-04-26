package com.example.catdog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private ArrayList<Animal> animalList = new ArrayList<>();
    private static final int REQUEST_CODE_ADD_ANIMAL  = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animalAdapter = new AnimalAdapter(animalList);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(animalAdapter);
        FloatingActionButton addButton = findViewById(R.id.fab_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_ANIMAL );
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ANIMAL && resultCode == RESULT_OK) {
            // Получаем данные из Intent
            String name = data.getStringExtra("name");
            String type = data.getStringExtra("type");
            int age = data.getIntExtra("age", 0);
            float weight = data.getFloatExtra("weight", 0.0f);
            byte[] imageData = data.getByteArrayExtra("image");

            // Преобразуем массив байтов в Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            // Создаем новый объект Animal
            Animal animal = new Animal(name, type, age, weight, bitmap);

            // Добавляем объект Animal в список
            animalList.add(animal);

            // Обновляем адаптер
            animalAdapter.notifyDataSetChanged();
                }
            }
        }
