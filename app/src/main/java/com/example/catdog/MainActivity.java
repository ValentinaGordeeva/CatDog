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
    //  private FirebaseFirestore db;
    private DatabaseReference dbRef;
    private Uri imageUri;
    private ImageView animalImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получаем ссылку на базу данных Firebase
       // db = FirebaseFirestore.getInstance();
        //dbRef = FirebaseDatabase.getInstance().getReference("animals");


        FloatingActionButton addButton = findViewById(R.id.fab_add);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        animalList = new ArrayList<>();
        animalAdapter = new AnimalAdapter(animalList);
        recyclerView.setAdapter(animalAdapter);

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


        /*

        imageURLRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageUrl= dataSnapshot.getValue(String.class);
                if (imageUrl != null) {
                    // Используйте значение imageURL здесь
                    Log.d(TAG, "imageURL is " + imageUrl);
                } else {
                    Log.d(TAG, "imageURL is NULL");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибок чтения из базы данных
                Log.e(TAG, "Failed to read imageURL.", databaseError.toException());
            }
        });
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                animalList.clear();

                for (DataSnapshot animalSnapshot : snapshot.getChildren()) {
                    Animal animal = animalSnapshot.getValue(Animal.class);
                    animalList.add(animal);
                }
                animalAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Отображено ", Toast.LENGTH_SHORT).show();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_ANIMAL);
            }
        });
    }}


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

    */ /*
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
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
 }
         */



    /*
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


     */
/*
    private void saveAnimalToFirebase(Animal animal){

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
