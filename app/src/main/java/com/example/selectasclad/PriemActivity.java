package com.example.selectasclad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PriemActivity extends AppCompatActivity {
    private String material,article,name,param,location,total;
    private Intent intent;
    DatabaseReference aluminDataBase,metDataBase;
    TextView textMaterial,textArticle,textName,textParam,textLocation,textTotal,textKey;
    EditText editTextAddTotal;
    Button btnAdd;
    String productDatabaseKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priem);

        aluminDataBase = FirebaseDatabase.getInstance().getReference("Alumin");
        metDataBase = FirebaseDatabase.getInstance().getReference("Met");
        textMaterial = findViewById(R.id.textMaterial);
        textArticle = findViewById(R.id.textArticle);
        textName = findViewById(R.id.textName);
        textParam = findViewById(R.id.textParam);
        textLocation = findViewById(R.id.textLocation);
        textTotal = findViewById(R.id.textTotal);
        textKey = findViewById(R.id.textKey);
        editTextAddTotal = findViewById(R.id.editTextTotal);
        btnAdd = findViewById(R.id.btn_add_new_total);

        intent = getIntent();
        material = intent.getStringExtra("material");
        article = intent.getStringExtra("article");
        name = intent.getStringExtra("name");
        param = intent.getStringExtra("param");
        location = intent.getStringExtra("location");
        total = intent.getStringExtra("total");
        textMaterial.setText(material);
        textArticle.setText(article);
        textName.setText(name);
        textParam.setText(param);
        textLocation.setText(location);
        textTotal.setText(total);
        getKeySelectProduct();


    }
    public void getKeySelectProduct(){

        if(material.equals("алюминий")){
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            Query query = aluminDataBase.orderByChild("name").equalTo(name);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren()){
                        Product product = ds.getValue(Product.class);
                        if(product.getParam().equals(param)){
                            productDatabaseKey = ds.getKey();
                            textKey.setText(productDatabaseKey);
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
                            textKey.setText(productDatabaseKey);
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