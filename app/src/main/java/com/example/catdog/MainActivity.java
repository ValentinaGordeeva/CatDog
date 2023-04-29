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

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private static final int REQUEST_CODE_ADD_ANIMAL = 1;
    private FirebaseFirestore db;
    private DatabaseReference dbRef;
    private Uri imageUri;
    private ImageView animalImageView;


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
        animalImageView =findViewById(R.id.animal_image_view);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("animals");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                animalList.clear(); // очищаем список животных

                // Проходим по всем дочерним узлам "animals"
                for (DataSnapshot animalSnapshot : snapshot.getChildren()) {
                    // Получаем объект Animal из снепшота
                    Animal animal = animalSnapshot.getValue(Animal.class);

                    // Получаем URL-адрес изображения из объекта Animal
                    String imageUrl= animal.getImageUrl();

                    // Загружаем изображение из Firebase Storage с использованием URL-адреса
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference imagesRef = storageRef.child("images");

                    storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                        // Создаем Bitmap из массива байтов
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        // Устанавливаем изображение в объект Animal
                        animal.setImage(bitmap);

                        // Добавляем объект Animal в список
                        animalList.add(animal);

                        // Обновляем адаптер
                        animalAdapter.notifyDataSetChanged();
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to load image.", e);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to load animals.", error.toException());
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_ANIMAL);
            }
        });


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            /*
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

             */
                animalList.clear();

                // Получение списка животных из Firebase Realtime Database
                for (DataSnapshot animalSnapshot : snapshot.getChildren()) {
                    String animalId = animalSnapshot.getKey();
                    String name = animalSnapshot.child("name").getValue(String.class);
                    String type = animalSnapshot.child("type").getValue(String.class);
                    int age = animalSnapshot.child("age").getValue(Integer.class);
                    float weight = animalSnapshot.child("weight").getValue(Float.class);

                    // Получение URL-адреса изображения животного
                    String imageUrl = animalSnapshot.child("imageUrl").getValue(String.class);

                    // Создание объекта Animal
                    Animal animal = new Animal(animalId, name, type, age, weight, imageUrl);
                    Glide.with(MainActivity.this)
                            .load(animal.getImageUrl())
                            .into(animalImageView);
                    // Добавление животного в список
                    animalList.add(animal);
                }

                // Обновление адаптера
                animalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
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

            List<String> images = new ArrayList<>();
            // Создаем новый объект Animal
            Animal animal = new Animal(animalId,name, type, age, weight, images);

            // Сохраняем данные животного в Firebase
            saveAnimalToFirebase(animal);

            // Добавляем объект Animal в список
            animalList.add(animal);

            // Обновляем адаптер
            animalAdapter.notifyDataSetChanged();
        }

         */
        if (requestCode == REQUEST_CODE_ADD_ANIMAL && resultCode == RESULT_OK) {
            // Получаем данные из Intent
            String name = data.getStringExtra("name");
            String type = data.getStringExtra("type");
            int age = data.getIntExtra("age", 0);
            float weight = data.getFloatExtra("weight", 0.0f);
            byte[] imageData = data.getByteArrayExtra("image");
            String animalId = data.getStringExtra("animalId");


            // Сохраняем изображение в Firebase Storage
            saveImageToFirebaseStorage(imageData, animalId, name, type, age, weight);

            // Создаем новый объект Animal
            Animal animal = new Animal(animalId, name, type, age, weight, null);

            // Сохраняем данные животного в Firebase Realtime Database
            saveAnimalToFirebase(animal);

            // Добавляем объект Animal в список
            animalList.add(animal);

            // Обновляем адаптер
            animalAdapter.notifyDataSetChanged();
        }

    }

    private void saveImageToFirebaseStorage(byte[] imageData, String animalId, String name, String type, int age, float weight) {
        if (imageData != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference imagesRef = storage.getReference().child("images/" + animalId + ".jpg");

            UploadTask uploadTask = imagesRef.putBytes(imageData);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    // Создаем новый объект Animal с URL-адресом изображения
                    Animal animal = new Animal(animalId, name, type, age, weight, imageUrl);

                    // Сохраняем данные животного в Firebase Realtime Database
                    saveAnimalToFirebase(animal);

                    // Добавляем объект Animal в список
                    animalList.add(animal);

                    // Обновляем адаптер
                    animalAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get download URL.", e);
                });
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Failed to upload image.", e);
            });
        }
    }


    private void saveAnimalToFirebase(Animal animal) {
        /*
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

         */
        // Получаем ссылку на базу данных Firebase Realtime Database
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("animals");
        dbRef.child(animal.getId()).setValue(animal.toMap());
    }

    private void loadAnimalsFromFirebase() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("animals");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                animalList.clear();
                for (DataSnapshot animalSnapshot : snapshot.getChildren()) {
                    Animal animal = animalSnapshot.getValue(Animal.class);
                    String imageUri = animal.getImageUrl();


                    // Загружаем изображение из Firebase Storage с использованием URL-адреса
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference imagesRef = storageRef.child("images");

                    Glide.with(MainActivity.this)
                            .load(animal.getImageUrl())
                            .into(animalImageView);

                    storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                        // Создаем Bitmap из массива байтов
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        // Устанавливаем изображение в объект Animal
                        animal.setImage(bitmap);

                        // Добавляем объект Animal в список
                        animalList.add(animal);

                        // Обновляем адаптер
                        animalAdapter.notifyDataSetChanged();
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to load image.", e);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to load animals.", error.toException());
            }
        });
    }}