package com.example.catdog;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import android.net.Uri;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AddActivity extends AppCompatActivity {

     private EditText etName, etType, etAge, etWeight;
     private Button btnAdd;
     private Button btnAddPhoto;
     private ImageView imageView;
    private Uri imageUri;
    private Intent resultIntent;

    private static final int MAX_IMAGE_SIZE = 14000;
    private static final int MIN_IMAGE_SIZE = 200;
    private Uri selectedImageUri;
    private Intent data;
    private DatabaseReference mDatabase;
     private static final int REQUEST_CODE_PICK_IMAGE = 1;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_add);

         etName = findViewById(R.id.etName);
         etType = findViewById(R.id.etType);
         etAge = findViewById(R.id.etAge);
         etWeight = findViewById(R.id.etWeight);
         btnAdd = findViewById(R.id.btnAdd);
         btnAddPhoto = findViewById(R.id.btnAddPhoto);
         imageView = findViewById(R.id.imageView);
         mDatabase = FirebaseDatabase.getInstance().getReference();

         btnAddPhoto.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);



             }
         });

         btnAdd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 onSaveClick(v);

             }
         });
     }

     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
             // Получаем URI выбранного изображения
             selectedImageUri = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.SIZE};
            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int sizeIndex = cursor.getColumnIndex(filePathColumn[0]);
            long imageSize = cursor.getLong(sizeIndex);
            cursor.close();
            if (imageSize > MAX_IMAGE_SIZE) {
                Toast.makeText(this, "Размер изображения слишком большой", Toast.LENGTH_SHORT).show();
                return;
            }if (imageSize < MIN_IMAGE_SIZE) {
                Toast.makeText(this, "Размер изображения слишком маленький", Toast.LENGTH_SHORT).show();
                return;
            }
            // Отображаем выбранное изображение в ImageView
             imageView.setImageURI(selectedImageUri);

             // Сохраняем данные Intent для передачи в onSaveClick
             this.data = data;
         }
     }
     public void onBackPressed() {

         setResult(Activity.RESULT_CANCELED);
         super.onBackPressed();
     }

     public void onSaveClick(View view) {
         String name = etName.getText().toString();
         String type = etType.getText().toString();
         int age = Integer.parseInt(etAge.getText().toString());
         float weight = Float.parseFloat(etWeight.getText().toString());
         String Age= etAge.getText().toString();
         String Weight= etWeight.getText().toString();
         if (name.trim().isEmpty() || type.trim().isEmpty()||Age.trim().isEmpty()||Weight.trim().isEmpty() ) {
             Toast.makeText(this, "Заполните пустые поля", Toast.LENGTH_SHORT).show();
             return;
         }
         // Получаем ссылку на Firebase Storage
         FirebaseStorage storage = FirebaseStorage.getInstance();
         StorageReference storageRef = storage.getReference();
         StorageReference imageRef = storageRef.child("images/" + selectedImageUri.getLastPathSegment());
         UploadTask uploadTask = imageRef.putFile(selectedImageUri);
         // Загружаем изображение животного в Firebase Storage


         uploadTask.addOnSuccessListener(taskSnapshot -> {
             // Получаем URL-адрес загруженного файла
             imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                 String imageURL = uri.toString();



                 // Создаем новый объект Animal
                 Animal animal = new Animal(null, name, type, age, weight, imageURL);
                 saveAnimalToFirebase(animal);

                 // Передаем данные о животном в MainActivity
                 Intent resultIntent = new Intent();
                 resultIntent.putExtra("animal", animal);
                 setResult(Activity.RESULT_OK, resultIntent);
                 finish();
             });
         }).addOnFailureListener(exception -> {
             // Обработка ошибки загрузки файла
             Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show();
         });


     }
    private void uploadImage() {
        // Проверяем, было ли выбрано изображение
        if (imageUri != null) {
            // Определяем путь в Firebase Storage, где будет храниться изображение
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images" + System.currentTimeMillis() + ".jpg");

            // Загружаем изображение в Firebase Storage
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Если загрузка успешна, получаем URL изображения
                        Task<Uri> downloadUrl = storageRef.getDownloadUrl();
                        downloadUrl.addOnSuccessListener(uri -> {
                            // Сохраняем URL изображения в базу данных
                            String imageUrl= uri.toString();
                            saveImageToFirebaseDatabase(imageUrl);
                        });
                    })
                    .addOnFailureListener(exception -> {
                        // Если загрузка не удалась, выводим сообщение об ошибке
                        Toast.makeText(getApplicationContext(), "Загрузка изображения не удалась", Toast.LENGTH_SHORT).show();
                    });
        }
    }
    private void saveImageToFirebaseDatabase(String imageUrl) {
        // Получаем уникальный идентификатор для нового элемента в базе данных
        String key = mDatabase.push().getKey();
        String name = data.getStringExtra("name");
        String type = data.getStringExtra("type");
        int age = data.getIntExtra("age", 0);
        float weight = data.getFloatExtra("weight", 0.0f);
        byte[] imageData = data.getByteArrayExtra("image");
        String animalId = data.getStringExtra("animalId");
        // Создаем новый объект CatDog с данными о животном
        Animal animal = new Animal(animalId, name, type, age, weight,imageUrl);



        // Сохраняем новый объект CatDog в базе данных
        mDatabase.child(key).setValue(animal);

        // Выводим сообщение об успешном сохранении
        Toast.makeText(getApplicationContext(), "Данные о животном успешно сохранены", Toast.LENGTH_SHORT).show();
    }
     private void saveAnimalToFirebase(Animal animal) {
         FirebaseDatabase database = FirebaseDatabase.getInstance();
         DatabaseReference databaseRef = database.getReference("animals");

         // Сохраняем данные животного в Firebase Database
         String key = databaseRef.push().getKey();
         Animal newAnimal = new Animal(key, animal.getName(), animal.getType(), animal.getAge(), animal.getWeight(), animal.getImageUrl());
         databaseRef.child(key).setValue(newAnimal);
     }


 }

