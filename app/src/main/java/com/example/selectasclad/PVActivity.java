package com.example.selectasclad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PVActivity extends AppCompatActivity {
    DatabaseReference aluminDataBase,metDataBase;
    PVAdapter pvAdapter;
    ListView listView;
    private Button btn_met,btn_al;
    private String message;
    private Intent intent;
    Toolbar toolbar;
    List<Product> met_sheet,met_pipe,met_box,al_profile,al_pipe,al_sheet,al_box;
    Product selectProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvactivity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.listview);

        aluminDataBase = FirebaseDatabase.getInstance().getReference("Alumin");
        metDataBase = FirebaseDatabase.getInstance().getReference("Met");

        intent = getIntent();
        message = intent.getStringExtra("message");
        setColorToolbar();

        btn_al = findViewById(R.id.btn_al);
        btn_met = findViewById(R.id.btn_met);

        btn_met.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu metPopupMenu = new PopupMenu(PVActivity.this,btn_met);
                metPopupMenu.getMenuInflater().inflate(R.menu.popup_met_menu,metPopupMenu.getMenu());
                metPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.met_sheet){
                            metSheetInitList();

                        } else if (menuItem.getItemId()==R.id.met_pipe) {
                            metPipeInitList();

                        } else if (menuItem.getItemId()==R.id.met_box) {
                            metBoxInitList();

                        } else if (menuItem.getItemId()==R.id.met_another) {

                        }

                        return true;
                    }
                });
                metPopupMenu.show();

            }
        });
        btn_al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu alPopupMenu = new PopupMenu(PVActivity.this,btn_al);
                alPopupMenu.getMenuInflater().inflate(R.menu.popup_al_menu,alPopupMenu.getMenu());
                alPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.al_profile){
                           alProfileInitList();
                        }else if (menuItem.getItemId()==R.id.al_sheet){
                            alSheetInitList();
                        } else if (menuItem.getItemId()==R.id.al_pipe) {
                           alPipeInitList();
                        } else if (menuItem.getItemId()==R.id.al_box) {
                            alBoxInitList();
                        }
                        return true;
                    }
                });
                alPopupMenu.show();

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
    private void alBoxInitList() {
        al_box = new ArrayList<>();
        pvAdapter = new PVAdapter(al_box,this);
        listView.setAdapter(pvAdapter);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                al_box.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getName().equals("бокс")){
                        al_box.add(product);
                    }

                }
                pvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        aluminDataBase.addValueEventListener(valueEventListener);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectProduct = al_box.get(i);
                goToNextActivity(selectProduct.getId(),selectProduct.getMaterial(),selectProduct.getArticle(),selectProduct.getName()
                        ,selectProduct.getParam(),selectProduct.getLocation(),selectProduct.getTotal());
            }
        });

    }
    private void alPipeInitList() {
        al_pipe = new ArrayList<>();
        pvAdapter = new PVAdapter(al_pipe,this);
        listView.setAdapter(pvAdapter);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                al_pipe.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getName().equals("труба")){
                        al_pipe.add(product);
                    }
                }
                pvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        aluminDataBase.addValueEventListener(valueEventListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectProduct = al_pipe.get(i);
                goToNextActivity(selectProduct.getId(),selectProduct.getMaterial(),selectProduct.getArticle(),selectProduct.getName()
                        ,selectProduct.getParam(),selectProduct.getLocation(),selectProduct.getTotal());
            }
        });
    }

    private void alSheetInitList() {
        al_sheet = new ArrayList<>();
        pvAdapter = new PVAdapter(al_sheet,this);
        listView.setAdapter(pvAdapter);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                al_sheet.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getName().equals("лист")){
                        al_sheet.add(product);
                    }
                }
                pvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        aluminDataBase.addValueEventListener(valueEventListener);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectProduct = al_sheet.get(i);
                goToNextActivity(selectProduct.getId(),selectProduct.getMaterial(),selectProduct.getArticle(),selectProduct.getName()
                        ,selectProduct.getParam(),selectProduct.getLocation(),selectProduct.getTotal());
            }
        });
    }

    private void alProfileInitList() {
        al_profile = new ArrayList<>();
        pvAdapter = new PVAdapter(al_profile,this);
        listView.setAdapter(pvAdapter);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                al_profile.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getName().equals("профиль")){
                        al_profile.add(product);
                    }
                }
                pvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        aluminDataBase.addValueEventListener(valueEventListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectProduct = al_profile.get(i);
                goToNextActivity(selectProduct.getId(),selectProduct.getMaterial(),selectProduct.getArticle(),selectProduct.getName()
                        ,selectProduct.getParam(),selectProduct.getLocation(),selectProduct.getTotal());
            }
        });
    }
    private void metBoxInitList() {
        met_box = new ArrayList<>();
        pvAdapter = new PVAdapter(met_box,this);
        listView.setAdapter(pvAdapter);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                met_box.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getName().equals("бокс")){
                        met_box.add(product);
                    }
                }
                pvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        metDataBase.addValueEventListener(valueEventListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectProduct = met_box.get(i);
                goToNextActivity(selectProduct.getId(),selectProduct.getMaterial(),selectProduct.getArticle(),selectProduct.getName()
                        ,selectProduct.getParam(),selectProduct.getLocation(),selectProduct.getTotal());
            }
        });
    }
    private void metPipeInitList() {
        met_pipe = new ArrayList<>();
        pvAdapter = new PVAdapter(met_pipe,this);
        listView.setAdapter(pvAdapter);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                met_pipe.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getName().equals("труба")){
                        met_pipe.add(product);
                    }
                }
                pvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        metDataBase.addValueEventListener(valueEventListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectProduct = met_pipe.get(i);
                goToNextActivity(selectProduct.getId(),selectProduct.getMaterial(),selectProduct.getArticle(),selectProduct.getName()
                        ,selectProduct.getParam(),selectProduct.getLocation(),selectProduct.getTotal());
            }
        });
    }
    private void metSheetInitList(){
        met_sheet = new ArrayList<>();
        pvAdapter = new PVAdapter(met_sheet,this);
        listView.setAdapter(pvAdapter);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                met_sheet.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getName().equals("лист")){
                        met_sheet.add(product);
                    }
                }
                pvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        metDataBase.addValueEventListener(valueEventListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectProduct = met_sheet.get(i);
                goToNextActivity(selectProduct.getId(),selectProduct.getMaterial(),selectProduct.getArticle(),selectProduct.getName()
                        ,selectProduct.getParam(),selectProduct.getLocation(),selectProduct.getTotal());
            }
        });

    }


    private void goToNextActivity(String id, String material, String article, String name
            , String param,String location, String total){
        switch (message){
            case "priem":{
                intent = new Intent(PVActivity.this,PriemActivity.class);
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
            case "vidacha":{
                intent = new Intent(PVActivity.this,VidachaActivity.class);
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