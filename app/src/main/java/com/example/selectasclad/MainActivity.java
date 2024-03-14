package com.example.selectasclad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private Button in,reg;
    private EditText textLogin , textPass;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

        in = findViewById(R.id.btn_in);
        reg = findViewById(R.id.btn_reg);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterWindow();
            }
        });
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sigIn();
            }
        });

    }
    private void showRegisterWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.window_register,null);
        builder.setView(rl);
        builder.setMessage(R.string.input);
        builder.setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(R.string.reg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog alertDialog = (AlertDialog) dialogInterface;
                EditText regLogin = alertDialog.findViewById(R.id.regLogin);
                EditText regPass = alertDialog.findViewById(R.id.regPass);
                if(TextUtils.isEmpty(regLogin.getText().toString())){
                    Toast.makeText(MainActivity.this, R.string.error_login, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(regPass.getText().toString())){
                    Toast.makeText(MainActivity.this, R.string.error_pass, Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(regLogin.getText().toString(),regPass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User(regLogin.getText().toString(),regPass.getText().toString());
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user);
                                startActivity(new Intent(MainActivity.this,MenuActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.show();
    }
    private void sigIn() {
        textLogin = findViewById(R.id.edLogin);
        textPass = (EditText)findViewById(R.id.edPass);

        if(TextUtils.isEmpty(textLogin.getText().toString())){
            Toast.makeText(MainActivity.this, R.string.error_login, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(textPass.getText().toString())){
            Toast.makeText(MainActivity.this, R.string.error_pass, Toast.LENGTH_SHORT).show();
            return;
        }auth.signInWithEmailAndPassword(textLogin.getText().toString(),textPass.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(MainActivity.this,MenuActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ОШИБКА АВТОРИЗАЦИИ", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}