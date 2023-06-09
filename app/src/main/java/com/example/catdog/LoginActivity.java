package com.example.catdog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.String;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText email_login;
    private EditText password_login;
    private Button btn_login;
    private TextView regist_txt;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);
        btn_login=findViewById(R.id.btn_login);
        regist_txt=findViewById(R.id.regist_txt);
        mAuth=FirebaseAuth.getInstance();
        regist_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this, RegistrerActivity.class);
                startActivity(intent);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email =  email_login.getText().toString().trim();
                String Password = password_login.getText().toString().trim();
                if (Email.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Заполните все пустые поля", Toast.LENGTH_SHORT).show();
                } else {
                   mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override                          public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                               startActivity(intent);
                           }else{
                               Toast.makeText(LoginActivity.this, "Ошибка, не правильно введен логин или пароль", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
                }
            }
        });


    }
    public void showMyToast() {
        Toast.makeText(this, R.string.my_toast_text, Toast.LENGTH_SHORT).show();
    };
}
