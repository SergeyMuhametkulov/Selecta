package com.example.selectasclad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class VidachaActivity extends AppCompatActivity {
    private String id, material,article,name,param,total;
    private Intent intent;
    DatabaseReference aluminDataBase,metDataBase;
    TextView textMaterial,textArticle,textName,textParam,textTotal;
    EditText editTextGetTotal;
    int newTotal;
    Button btnGet;
    Toolbar toolbar;
    String subject,emailText;
    String[] addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vidacha);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Выдача товара");
        toolbar.setBackgroundColor(getResources().getColor(R.color.vidacha));
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        aluminDataBase = FirebaseDatabase.getInstance().getReference("Alumin");
        metDataBase = FirebaseDatabase.getInstance().getReference("Met");

        addresses = new String[1];
        addresses[0] = "mahey10725@gmail.com";
        subject = "выдача";

        textMaterial = findViewById(R.id.textMaterial);
        textArticle = findViewById(R.id.textArticle);
        textName = findViewById(R.id.textName);
        textParam = findViewById(R.id.textParam);
        textTotal = findViewById(R.id.textTotal);
        editTextGetTotal = findViewById(R.id.editTextTotal);
        btnGet = findViewById(R.id.btn_get_new_total);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTotal();
            }
        });

        intent = getIntent();
        id = intent.getStringExtra("id");
        material = intent.getStringExtra("material");
        article = intent.getStringExtra("article");
        name = intent.getStringExtra("name");
        param = intent.getStringExtra("param");
        total = intent.getStringExtra("total");
        textMaterial.setText(material);
        textArticle.setText(article);
        textName.setText(name);
        textParam.setText(param);
        textTotal.setText("Кол-во: " + total);


    }

    private void getTotal() {
        if(TextUtils.isEmpty(editTextGetTotal.getText().toString())){
            Toast.makeText(this, "Введите кол-во", Toast.LENGTH_SHORT).show();
            return;
        }if(Integer.parseInt(editTextGetTotal.getText().toString())> Integer.parseInt(total)){
            Toast.makeText(this, "Нет такого кол-ва", Toast.LENGTH_SHORT).show();
            return;
        }
        newTotal = Integer.parseInt(total) - Integer.parseInt(editTextGetTotal.getText().toString());
        if(material.equals("алюминий")){
            aluminDataBase.child(id).child("total").setValue(String.valueOf(newTotal)).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(VidachaActivity.this, "Выдано " + editTextGetTotal.getText().toString(), Toast.LENGTH_SHORT).show();
                            sendToEmail(String.valueOf(newTotal));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VidachaActivity.this, "ОШИБКА", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

        }
        if(material.equals("железо")){
            metDataBase.child(id).child("total").setValue(String.valueOf(newTotal)).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(VidachaActivity.this, "Принято " + editTextGetTotal.getText().toString(), Toast.LENGTH_SHORT).show();
                            sendToEmail(String.valueOf(newTotal));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VidachaActivity.this, "ОШИБКА", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });

        }

    }

    private void sendToEmail(String newTotal) {
        emailText ="" + material + " " + name + " " + param +". "
                + "Выдано " + editTextGetTotal.getText().toString() +
                ".  Было " + total + ", Осталось " + newTotal;
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL,addresses);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT,emailText);
        if(emailIntent.resolveActivity(getPackageManager()) != null){
            startActivity(emailIntent);
        }

    }
}