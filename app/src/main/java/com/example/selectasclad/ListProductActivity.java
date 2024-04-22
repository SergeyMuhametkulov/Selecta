package com.example.selectasclad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListProductActivity extends AppCompatActivity {
    private String message,article,material;
    Product selectProduct;
    private Intent intent;
    Toolbar toolbar;
    ListView listView;
    List<Product> listProduct;
    DatabaseReference aluminDataBase,metDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        toolbar = findViewById(R.id.toolbar_list_product);
        setSupportActionBar(toolbar);


        aluminDataBase = FirebaseDatabase.getInstance().getReference("Alumin");
        metDataBase = FirebaseDatabase.getInstance().getReference("Met");

        intent = getIntent();
        article = intent.getStringExtra("article");
        message = intent.getStringExtra("message");
        material = intent.getStringExtra("material");
        setColorToolbar();

        listView = findViewById(R.id.list_product_article);

        if(material.equals("алюминий")){
            initList(aluminDataBase);
        }
        if(material.equals("железо")){
            initList(metDataBase);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectProduct = listProduct.get(i);
                goToNextActivity(selectProduct.getId(),selectProduct.getMaterial(),selectProduct.getArticle(),selectProduct.getName()
                        ,selectProduct.getParam(),selectProduct.getLocation(),selectProduct.getTotal(),selectProduct.getUnit());
            }
        });
    }

    private void initList(DatabaseReference database) {
        listProduct = new ArrayList<>();
        PVAdapter adapter = new PVAdapter(listProduct,this);
        listView.setAdapter(adapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    Product pr = ds.getValue(Product.class);
                    if(pr.getArticle().equals(article)){
                        listProduct.add(pr);
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setColorToolbar() {
        switch (message){
            case "priem":{
                getSupportActionBar().setTitle(R.string.priem);
                toolbar.setBackgroundColor(getResources().getColor(R.color.priem));
                break;
            }
            case "vidacha":{
                getSupportActionBar().setTitle(R.string.vidacha);
                toolbar.setBackgroundColor(getResources().getColor(R.color.vidacha));
                break;
            }
        }
    }
    private void goToNextActivity(String id, String material, String article, String name
            , String param,String location, String total,String unit){
        switch (message){
            case "priem":{
                intent = new Intent(ListProductActivity.this,PriemActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("material",material);
                intent.putExtra("article",article);
                intent.putExtra("name",name);
                intent.putExtra("param",param);
                intent.putExtra("location",location);
                intent.putExtra("total",total);
                intent.putExtra("unit",unit);
                startActivity(intent);
                break;
            }
            case "vidacha":{
                intent = new Intent(ListProductActivity.this,VidachaActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("material",material);
                intent.putExtra("article",article);
                intent.putExtra("name",name);
                intent.putExtra("param",param);
                intent.putExtra("location",location);
                intent.putExtra("total",total);
                startActivity(intent);
                break;

            }
        }
    }
}