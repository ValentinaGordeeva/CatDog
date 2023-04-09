package com.example.catdog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button logout_btn;
    private Button btn_add;
    TextView textViewname;
    TextView textViewtype;
    TextView textViewage;
    TextView textViewweight;
   ListView listview;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout_btn=findViewById(R.id.logout_btn);
        mAuth=FirebaseAuth.getInstance();
        btn_add=findViewById(R.id.btn_add);

        listview=findViewById(R.id.listview);


        textViewname=findViewById(R.id.textViewname);
        textViewtype=findViewById(R.id.textViewtype);
        textViewage=findViewById(R.id.textViewage);
        textViewweight=findViewById(R.id.textViewweight);



        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });


        Intent intent=getIntent();

            String name = intent.getStringExtra("name");
            String type = intent.getStringExtra("type");
            String age = intent.getStringExtra("age");
            String weight = intent.getStringExtra("weight");

        List list = new ArrayList<>();
        list.add(name);
        list.add(type);
        list.add(age);
        list.add(weight);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,list);
        listview.setAdapter(adapter);

        /*
        textViewname.setText(name);
        textViewtype.setText(type);
        textViewage.setText(age);
        textViewweight.setText(weight);
            */


    }

}