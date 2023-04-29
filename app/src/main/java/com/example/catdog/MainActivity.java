package com.example.catdog;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    /*
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
*/
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private ArrayList<Animal> animalList = new ArrayList<>();
    private static final int REQUEST_CODE_ADD_ANIMAL  = 1;
    private FirebaseFirestore db;
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получаем ссылку на базу данных Firebase
        db = FirebaseFirestore.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("animals");

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


 dbRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            // Очистка списка животных
            animalList.clear();

            // Получение списка животных из Firebase Realtime Database
            for (DataSnapshot animalSnapshot : snapshot.getChildren()) {
                String animalId = animalSnapshot.getKey();
                String name = animalSnapshot.child("name").getValue(String.class);
                String type = animalSnapshot.child("type").getValue(String.class);
                int age = animalSnapshot.child("age").getValue(Integer.class);
                float weight = animalSnapshot.child("weight").getValue(Float.class);

                // Создание объекта Animal
                Animal animal = new Animal(animalId, name, type, age, weight, null);

                // Добавление животного в список
                animalList.add(animal);
            }

            // Обновление адаптера
            animalAdapter.notifyDataSetChanged();
        }


        public void onCancelled(DatabaseError error) {
            Log.e(TAG, "Failed to read value.", error.toException());
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
            String animalId = data.getStringExtra("animalId");
            // Создаем новый объект Animal
            Animal animal = new Animal(animalId,name, type, age, weight, bitmap);

            // Сохраняем данные животного в Firebase
            saveAnimalToFirebase(animal);

            // Добавляем объект Animal в список
            animalList.add(animal);

            // Обновляем адаптер
            animalAdapter.notifyDataSetChanged();
        }
    }

    private void saveAnimalToFirebase(Animal animal) {
        // Получаем ссылку на коллекцию животных
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        // Генерируем уникальный идентификатор для нового животного
        String animalId = dbRef.child("animals").push().getKey();

        // Создаем объект для сохранения данных животного в базе данных
        Map<String, Object> animalData = new HashMap<>();
        animalData.put("name", animal.getName());
        animalData.put("type", animal.getType());
        animalData.put("age", animal.getAge());
        animalData.put("weight", animal.getWeight());

        // Сохраняем данные животного в базе данных Firebase
        //dbRef.child("animals").child(animalId).setValue(animalData);
        dbRef.push().setValue(animal);

}
}