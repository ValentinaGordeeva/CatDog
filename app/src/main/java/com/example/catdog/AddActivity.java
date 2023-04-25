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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

/*
public class AddActivity extends AppCompatActivity {



   private EditText etName, etType, etAge, etWeight;
   private Button btnAdd;
   private Button btnAddPhoto;
   private DatabaseReference mDatabase;
   FirebaseDatabase database = FirebaseDatabase.getInstance();

   static final int PICK_IMAGE_REQUEST = 1;
   private ImageView ivPreview;
   private Uri imageUri;
   //  private FirebaseStorage mFirebaseStorage;
   // private StorageReference mStorageReference;
   //  private DatabaseReference mDatabaseReference;
   // private ProgressDialog progressDialog;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);


       FirebaseApp.initializeApp(this);
       FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
       firebaseAppCheck.installAppCheckProviderFactory(
               PlayIntegrityAppCheckProviderFactory.getInstance());

       setContentView(R.layout.activity_add);
       // Log.d("UPLOAD", "Starting upload...");
// Получаем ссылку на базу данных Firebase
       mDatabase = FirebaseDatabase.getInstance().getReference();

       etName = findViewById(R.id.etName);
       etType = findViewById(R.id.etType);
       etAge = findViewById(R.id.etAge);
       etWeight = findViewById(R.id.etWeight);
       btnAdd = findViewById(R.id.btnAdd);
       btnAddPhoto = findViewById(R.id.btnAddPhoto);
       ivPreview = findViewById(R.id.imageView);


       mFirebaseStorage = FirebaseStorage.getInstance();
       mStorageReference = mFirebaseStorage.getReference("animal_photos");
       mDatabaseReference = FirebaseDatabase.getInstance().getReference("animals");


       btnAddPhoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // openFileChooser();
               Intent intent = new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
           }
       });

       btnAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               saveAnimal();
           }
       });
      // Log.d("UPLOAD", "Upload successful");
   }

protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
   super.onActivityResult(requestCode, resultCode, data);

   if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
       imageUri = data.getData();
       ivPreview.setImageURI(imageUri);
       Context context=this;
       Glide.with(context).load(imageUri) // указываем URL изображения или путь к локальному файлу
               .override(250, 250) // указываем размеры изображения в пикселях
               .centerCrop() // указываем тип масштабирования (центрирование с обрезкой)
               .into(ivPreview); // указываем ImageView, в который загружаем изображение
   }
}
   private void saveAnimal() {
       String name = etName.getText().toString();
       String type = etType.getText().toString();
       int age = Integer.parseInt(etAge.getText().toString());
       double weight = Double.parseDouble(etWeight.getText().toString());

       if (name.isEmpty() || type.isEmpty()) {
           Toast.makeText(this, "Заполните все пустые поля", Toast.LENGTH_SHORT).show();
           return;
       }

       if (imageUri == null) {
           Toast.makeText(this, "Выберите изображение", Toast.LENGTH_SHORT).show();
           return;
       }

       final ProgressDialog progressDialog = new ProgressDialog(this);
       progressDialog.setTitle("Загрузка...");
       progressDialog.show();

       StorageReference fileReference = mStorageReference.child(System.currentTimeMillis() + ".jpg");
       UploadTask uploadTask = fileReference.putFile(imageUri);

       uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       String photoUrl = uri.toString();
                       addAnimal(name, type, age, weight, photoUrl);

                   }
               });

               progressDialog.dismiss();

           }

       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(AddActivity.this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
               progressDialog.dismiss();
           }
       });



   }
    protected void onDestroy() {
       super.onDestroy();
       if (progressDialog != null && progressDialog.isShowing()) {
           progressDialog.dismiss();
       }
   }

  private void addAnimal(String name, String type, int age, double weight, String imageUrl) {
      if (imageUrl == null) {
          Toast.makeText(this, "Выберите изображение", Toast.LENGTH_SHORT).show();
          return;
      }

      Animal animal = new Animal( name, type, age, weight, imageUrl);
      //  DatabaseReference animalRef = mDatabase.child("animals").push();
      // mDatabaseReference.child(animalId).setValue(animal);

      animalRef.setValue(animal);
      Toast.makeText(this, "Животное добавлено успешно!", Toast.LENGTH_SHORT).show();
      String key = animalRef.push().getKey();
      animalRef.child(key).setValue(animal);

      Intent intent = new Intent( this, MainActivity.class);
      startActivity(intent);
      finish();
      Intent resultIntent = new Intent();
      resultIntent.putExtra("name", name);
      resultIntent.putExtra("type", type);
      resultIntent.putExtra("age", age);
      resultIntent.putExtra("weight", weight);
      resultIntent.putExtra("imageUrl", imageUrl);
      setResult(Activity.RESULT_OK, resultIntent);
      finish();
  }

}*/
 public class AddActivity extends AppCompatActivity {

    private EditText etName, etType, etAge, etWeight;
    private Button btnAdd;
    private Button btnAddPhoto;

    static final int PICK_IMAGE_REQUEST = 1;
    private ImageView ivPreview;
    private Uri imageUri;
    private Bitmap animalBitmap;

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
        ivPreview = findViewById(R.id.imageView);

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnimal();

            }
        });
    }
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivPreview.setImageURI(imageUri);
        }
    }



    private void saveAnimal() {
        String name = etName.getText().toString();
        String type = etType.getText().toString();
        int age = Integer.parseInt(etAge.getText().toString());
        double weight = Double.parseDouble(etWeight.getText().toString());
        Bitmap animalBitmap = null;
        if (imageUri != null) {
            try {
                animalBitmap  = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                Log.e("AddActivity", "Error getting image bitmap", e);
            }
        }
        Animal animal = new Animal(name, type, age, weight, animalBitmap);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", name);
        resultIntent.putExtra("type", type);
        resultIntent.putExtra("age", age);
        resultIntent.putExtra("weight", weight);
        resultIntent.putExtra("image", animalBitmap);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();




        Toast.makeText(this, "Животное добавлено успешно!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

        }
    }


