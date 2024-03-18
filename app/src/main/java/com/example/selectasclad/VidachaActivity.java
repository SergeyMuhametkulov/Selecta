package com.example.selectasclad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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
    private String material,article,name,param,total;
    private Intent intent;
    DatabaseReference aluminDataBase,metDataBase;
    TextView textMaterial,textArticle,textName,textParam,textTotal;
    EditText editTextGetTotal;
    Button btnGet;
    Toolbar toolbar;
    String productDatabaseKey;

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

    }

    private void getTotal() {
        int newTotal = 0;
        if(TextUtils.isEmpty(editTextGetTotal.getText().toString())){
            Toast.makeText(this, "Введите кол-во", Toast.LENGTH_SHORT).show();
            return;
        }if(Integer.parseInt(editTextGetTotal.getText().toString())> Integer.parseInt(total)){
            Toast.makeText(this, "Нет такого кол-ва", Toast.LENGTH_SHORT).show();
            return;
        }
        newTotal = Integer.parseInt(total) - Integer.parseInt(editTextGetTotal.getText().toString());
        if(material.equals("алюминий")){
            aluminDataBase.child(productDatabaseKey).child("total").setValue(String.valueOf(newTotal))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(VidachaActivity.this, "Выдано " + editTextGetTotal.getText().toString(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VidachaActivity.this, "ОШИБКА", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

        }if(material.equals("железо")){
            metDataBase.child(productDatabaseKey).child("total").setValue(String.valueOf(newTotal))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(VidachaActivity.this, "Выдано " + editTextGetTotal.getText().toString(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

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