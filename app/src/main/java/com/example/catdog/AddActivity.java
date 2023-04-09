package com.example.catdog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collection;

public class AddActivity extends AppCompatActivity {
    Button btn_add_Animal;
    EditText name_add;
    EditText type_add;
    EditText age_add;
    EditText weight_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        name_add=findViewById(R.id.name_add);
        type_add=findViewById(R.id.type_add);
        age_add=findViewById(R.id.age_add);
        weight_add=findViewById(R.id.weight_add);
        btn_add_Animal=findViewById(R.id.btn_add_Animal);


        btn_add_Animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AddActivity.this, MainActivity.class);

                intent.putExtra("name",name_add.getText().toString());
                intent.putExtra("type",type_add.getText().toString());
                intent.putExtra("age",age_add.getText().toString());
                intent.putExtra("weight",weight_add.getText().toString());
                startActivity(intent);




            }
        });
    }


}