package com.example.catdog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
    private static final int ADD_ANIMAL_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animalAdapter = new AnimalAdapter(animalList);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter(animalAdapter);
        /*
        Bitmap catBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        animalList.add(new Animal("Барсик", "Кошка", 3, 4.5, catBitmap));
        animalAdapter.notifyDataSetChanged();

      */

        FloatingActionButton addButton = findViewById(R.id.fab_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, ADD_ANIMAL_REQUEST);
            }
        });
    }
      public void addAnimal(Animal animal) {
        animalList.add(animal);
       animalAdapter.notifyDataSetChanged();
    }


@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == ADD_ANIMAL_REQUEST && resultCode == RESULT_OK) {
        String name = data.getStringExtra("name");
        String type = data.getStringExtra("type");
        int age = data.getIntExtra("age", 0);
        double weight = data.getDoubleExtra("weight", 0);
        Bitmap image = (Bitmap) data.getParcelableExtra("image");

        Animal animal = new Animal(name, type, age, weight, image);
        addAnimal(animal);

        Log.d("MainActivity", "Добавленное: " + animal.toString());
    } else {
        Log.d("MainActivity", "Резкльтат: " + resultCode);
    }

        }
    protected void onPause() {
        super.onPause();
        Log.d("AddActivity", "onPause() called");
    }

}

