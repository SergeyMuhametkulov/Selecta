package com.example.selectasclad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

public class PriemActivity extends AppCompatActivity {
    private String material,article,name,param,total;
    private Intent intent;
    DatabaseReference aluminDataBase,metDataBase;
    TextView textMaterial,textArticle,textName,textParam,textTotal;
    EditText editTextAddTotal;
    Button btnAdd;
    int newTotal ;
    Toolbar toolbar;

    String productDatabaseKey,subject,emailText;
    String[] addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priem);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Прием товара");
        toolbar.setBackgroundColor(getResources().getColor(R.color.priem));


        aluminDataBase = FirebaseDatabase.getInstance().getReference("Alumin");
        metDataBase = FirebaseDatabase.getInstance().getReference("Met");

        addresses = new String[1];
        addresses[0] = "mahey10725@gmail.com";
        subject = "прием";

        textMaterial = findViewById(R.id.textMaterial);
        textArticle = findViewById(R.id.textArticle);
        textName = findViewById(R.id.textName);
        textParam = findViewById(R.id.textParam);
        textTotal = findViewById(R.id.textTotal);
        editTextAddTotal = findViewById(R.id.editTextTotal);
        btnAdd = findViewById(R.id.btn_add_new_total);

        intent = getIntent();
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
        getKeySelectProduct();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTotal();
            }
        });


    }

    private void addNewTotal() {

        if(TextUtils.isEmpty(editTextAddTotal.getText().toString())){
            Toast.makeText(this, "Введите кол-во", Toast.LENGTH_SHORT).show();
            return;
        }
        newTotal = Integer.parseInt(total) + Integer.parseInt(editTextAddTotal.getText().toString());
        if(material.equals("алюминий")){
            aluminDataBase.child(productDatabaseKey).child("total").setValue(String.valueOf(newTotal)).
            addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(PriemActivity.this, "Принято " + editTextAddTotal.getText().toString(), Toast.LENGTH_SHORT).show();
                    sendToEmail(String.valueOf(newTotal));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PriemActivity.this, "ОШИБКА", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

        }if(material.equals("железо")){
            metDataBase.child(productDatabaseKey).child("total").setValue(String.valueOf(newTotal)).
            addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(PriemActivity.this, "Принято " + editTextAddTotal.getText().toString(), Toast.LENGTH_SHORT).show();
                    sendToEmail(String.valueOf(newTotal));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PriemActivity.this, "ОШИБКА", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });

        }


    }

    private void sendToEmail(String newTotal) {
        emailText ="" + material + " " + name + " " + param +". "
                + "Принято " + editTextAddTotal.getText().toString() +
                  ".  Было " + total + ", Стало " + newTotal;
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL,addresses);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT,emailText);
        if(emailIntent.resolveActivity(getPackageManager()) != null){
            startActivity(emailIntent);
        }

    }

    public void getKeySelectProduct(){

        if(material.equals("алюминий")){
            Query query = aluminDataBase.orderByChild("name").equalTo(name);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren()){
                        Product product = ds.getValue(Product.class);
                        if(product.getParam().equals(param)){
                            productDatabaseKey = ds.getKey();
                            break;

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }if(material.equals("железо")){
            Query query = metDataBase.orderByChild("name").equalTo(name);
            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Product product = ds.getValue(Product.class);
                        assert product != null;
                        if(product.getParam().equals(param)){
                            productDatabaseKey = ds.getKey();
                            break;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}