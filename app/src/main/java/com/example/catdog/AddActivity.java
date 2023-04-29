package com.example.catdog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

         if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
             Uri imageUri = data.getData();
             Bitmap bitmap = null;
             try {
                 bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
             } catch (IOException e) {
                 e.printStackTrace();
             }

             // Устанавливаем выбранное изображение в ImageView
             imageView.setImageBitmap(bitmap);
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

         if (name.trim().isEmpty() || type.trim().isEmpty()) {
             Toast.makeText(this, "Please enter animal name and type", Toast.LENGTH_SHORT).show();
             return;
         }

         // Получаем Bitmap из ImageView
         Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

         // Преобразуем Bitmap в массив байтов
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
         byte[] imageData = baos.toByteArray();

         FirebaseStorage storage = FirebaseStorage.getInstance();
         StorageReference storageRef = storage.getReference();
         String filename = UUID.randomUUID().toString() + ".jpg";
         StorageReference imageRef = storageRef.child("images/" + filename);
         imageRef.putBytes(imageData).addOnSuccessListener(taskSnapshot -> {
             // Файл был успешно загружен
             Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

             // Получаем ссылку на загруженный файл в Firebase Storage
             imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                 String imageURL = uri.toString();
                 // Сохраняем данные о животном и ссылку на файл в Firebase Realtime Database
                 DatabaseReference animalsRef = FirebaseDatabase.getInstance().getReference("animals");
                 String animalId = animalsRef.push().getKey();
                 Animal animal = new Animal(animalId, name, type, age, weight, imageURL);
                 animalsRef.child(animalId).setValue(animal);

                 // Передаем данные о животном в MainActivity
                 Intent resultIntent = new Intent();
                 resultIntent.putExtra("animal", animal);
                 setResult(Activity.RESULT_OK, resultIntent);
                 finish();
             });}).addOnFailureListener(exception -> {
             // Обработка ошибки загрузки файла
             Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show();
         });
                 /*
         // передаю  в MainActivity
         Intent resultIntent = new Intent();
         resultIntent.putExtra("name", name);
         resultIntent.putExtra("type", type);
         resultIntent.putExtra("age", age);
         resultIntent.putExtra("weight", weight);
         resultIntent.putExtra("image", imageData);
         setResult(Activity.RESULT_OK, resultIntent);
         finish();

                  */
     }


 }

