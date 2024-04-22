package com.example.selectasclad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    List<ProductInList> listAluminPipe,listMetBox;
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
                           alPipeInitList2();
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
    private void alPipeInitList2(){
        initProductList();
        List<String> allPipeAl = new ArrayList<>();
        for(ProductInList pr : listAluminPipe){
            allPipeAl.add(pr.getName());

        }
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),R.layout.spinner_add_new,allPipeAl);
        listView.setAdapter(adapter);
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
                intent = new Intent(PVActivity.this,ListProductActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("material",material);
                intent.putExtra("article",article);
                intent.putExtra("name",name);
                intent.putExtra("param",param);
                intent.putExtra("location",location);
                intent.putExtra("total",total);
                intent.putExtra("message",message);
                startActivity(intent);
                break;
            }
            case "vidacha":{
                intent = new Intent(PVActivity.this,ListProductActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("material",material);
                intent.putExtra("article",article);
                intent.putExtra("name",name);
                intent.putExtra("param",param);
                intent.putExtra("location",location);
                intent.putExtra("total",total);
                intent.putExtra("message",message);
                startActivity(intent);
                break;

            }
        }
    }
    private void initProductList(){
        listAluminPipe = new ArrayList<>();
        listAluminPipe.add(new ProductInList("труба 8 Х 1 Х 6000 мм","алюминий","00-0008045"));
        listAluminPipe.add(new ProductInList("труба 10 Х 1,5 Х 6000 мм","алюминий","10156000"));
        listAluminPipe.add(new ProductInList("труба 10 Х 2 Х 6000 мм","алюминий","1026000"));
        listAluminPipe.add(new ProductInList("труба 12 Х 1 Х 6000 мм","алюминий","1216000"));
        listAluminPipe.add(new ProductInList("труба 12 Х 1,5 Х 3000 мм","алюминий","12*1,5*3000"));
        listAluminPipe.add(new ProductInList("труба 12 Х 3 Х 6000 мм","алюминий","12x3PTT"));
        listAluminPipe.add(new ProductInList("труба 14 Х 1,2 Х 6000 мм","алюминий","71098"));
        listAluminPipe.add(new ProductInList("труба 14 Х 2 Х 6000 мм","алюминий","1216001"));
        listAluminPipe.add(new ProductInList("труба 16 Х 1,5 Х 6000 мм","алюминий","16156000АД31Т1"));
        listAluminPipe.add(new ProductInList("труба 16 Х 2 Х 6000 мм","алюминий","1626000"));
        listAluminPipe.add(new ProductInList("труба 16 Х 3,8 Х 6000 мм","алюминий","16386000"));
        listAluminPipe.add(new ProductInList("труба 20 Х 1 Х 3000 мм","алюминий","20x1x3000"));
        listAluminPipe.add(new ProductInList("труба 20 Х 1,5 Х 6000 мм","алюминий","АД20х1,5"));
        listAluminPipe.add(new ProductInList("труба 20 Х 2 Х 6000 мм","алюминий","2026000"));
        listAluminPipe.add(new ProductInList("труба 20 Х 3 Х 6000 мм","алюминий","20Х3Х6000"));
        listAluminPipe.add(new ProductInList("труба 20 Х 4 Х 6000 мм","алюминий","2046000"));
        listAluminPipe.add(new ProductInList("труба 22 Х 1,5 Х 6000 мм","алюминий","22x1,5x6000"));
        listAluminPipe.add(new ProductInList("труба 25 Х 2 Х 6000 мм","алюминий","654654654"));
        listAluminPipe.add(new ProductInList("труба 28 Х 1,5 Х 6000 мм","алюминий","28156000"));
        listAluminPipe.add(new ProductInList("труба 30 Х 1 Х 6000 мм","алюминий","30X1X6000"));
        listAluminPipe.add(new ProductInList("труба 30 Х 2 Х 6000 мм","алюминий","30X2X6000"));
        listAluminPipe.add(new ProductInList("труба 30 Х 3 Х 6000 мм","алюминий","3036000"));
        listAluminPipe.add(new ProductInList("труба 35 Х 2 Х 6000 мм","алюминий","3526000"));
        listAluminPipe.add(new ProductInList("труба 40 Х 2 Х 6000 мм","алюминий","АД31Т140Х2Х6000"));
        listAluminPipe.add(new ProductInList("труба 40 Х 3 Х 6000 мм","алюминий","АД31Т140Х3Х6000"));
        listAluminPipe.add(new ProductInList("труба 42 Х 2 Х 6000 мм","алюминий","4226000"));
        listAluminPipe.add(new ProductInList("труба 45 Х 2 Х 6000 мм","алюминий","4526000 тр"));
        listAluminPipe.add(new ProductInList("труба 50 Х 3 Х 6000 мм","алюминий","5036000"));
        listAluminPipe.add(new ProductInList("труба 50 Х 4 Х 3000 мм","алюминий","5043000"));
        listAluminPipe.add(new ProductInList("труба 52 Х 4 Х 5000 мм","алюминий","52x4x5000"));
        listAluminPipe.add(new ProductInList("труба 54 Х 4 Х 5000 мм","алюминий","54x4x5000"));
        listAluminPipe.add(new ProductInList("труба 60 Х 1,5 Х 6000 мм","алюминий","60X1,5X6000"));
        listAluminPipe.add(new ProductInList("труба 60 Х 2 Х 6000 мм","алюминий","6026000"));
        listAluminPipe.add(new ProductInList("труба 60 Х 3 Х 6000 мм","алюминий","6036000"));
        listAluminPipe.add(new ProductInList("труба 70 Х 2 Х 6000 мм","алюминий","7026000"));
        listAluminPipe.add(new ProductInList("труба 70 Х 3 Х 6000 мм","алюминий","7036000"));
        listAluminPipe.add(new ProductInList("труба 80 Х 2 Х 6000 мм","алюминий","8026000"));
        listAluminPipe.add(new ProductInList("труба 80 Х 2 Х 6000 мм","алюминий","8036000"));
        listAluminPipe.add(new ProductInList("труба 80 Х 15 Х 6000 мм","алюминий","80156000"));
        listAluminPipe.add(new ProductInList("труба 85 Х 5 Х 6000 мм","алюминий","8556000"));
        listAluminPipe.add(new ProductInList("труба 90 Х 3 Х 6000 мм","алюминий","9036000"));
        listAluminPipe.add(new ProductInList("труба 90 Х 4 Х 6000 мм","алюминий","9046000"));
        listAluminPipe.add(new ProductInList("труба 90 Х 5 Х 6000 мм","алюминий","9056000АД31Т1"));
        listAluminPipe.add(new ProductInList("труба 90 Х 10 Х 6000 мм","алюминий","90X10X6000"));
        listAluminPipe.add(new ProductInList("труба 100 Х 2 Х 6000 мм","алюминий","10026000"));
        listAluminPipe.add(new ProductInList("труба 100 Х 3 Х 6000 мм","алюминий","10036000"));
        listAluminPipe.add(new ProductInList("труба 100 Х 5 Х 6000 мм","алюминий","10056000"));
        listAluminPipe.add(new ProductInList("труба 100 Х 15 Х 6000 мм","алюминий","100156000"));
        listAluminPipe.add(new ProductInList("труба 110 Х 5 Х 6000 мм","алюминий","11056000"));
        listAluminPipe.add(new ProductInList("труба 120 Х 3,5 Х 6000 мм","алюминий","120x3,5"));
        listAluminPipe.add(new ProductInList("труба 120 Х 5 Х 6000 мм","алюминий","00-00119938"));
        listAluminPipe.add(new ProductInList("труба 125 Х 4 Х 6000 мм","алюминий","125X4X6000"));
        listAluminPipe.add(new ProductInList("труба 125 Х 5 Х 6000 мм","алюминий","АМГ5м 125х5х6000"));
        listAluminPipe.add(new ProductInList("труба 125 Х 15 Х 6000 мм","алюминий","125156000"));
        listAluminPipe.add(new ProductInList("труба 150 Х 3 Х 6000 мм","алюминий","15036000"));

        listMetBox = new ArrayList<>();
        listMetBox.add(new ProductInList("бокс 10 X 10 X 1 X 6000 мм","железо","101016000"));
        listMetBox.add(new ProductInList("бокс 15 X 15 X 1 X 6000 мм","железо","15x15x1"));
        listMetBox.add(new ProductInList("бокс 15 X 15 X 1.5 X 6000 мм","железо","15*15*1,5"));
        listMetBox.add(new ProductInList("бокс 15 X 15 X 2 X 6000 мм","железо","15x15x2"));
        listMetBox.add(new ProductInList("бокс 20 X 10 X 2 X 6000 мм","железо","20x10x2"));
        listMetBox.add(new ProductInList("бокс 20 X 20 X 1 X 6000 мм","железо","20x20x1"));
        listMetBox.add(new ProductInList("бокс 20 X 20 X 1.5 X 6000 мм","железо","20*20*1,5 6м"));
        listMetBox.add(new ProductInList("бокс 20 X 20 X 2 X 6000 мм","железо","20*20*2 6м"));
        listMetBox.add(new ProductInList("бокс 25 X 25 X 2 X 6000 мм","железо","25*25*2 6м"));
        listMetBox.add(new ProductInList("бокс 30 X 15 X 1.5 X 6000 мм","железо","30*15*1,5"));
        listMetBox.add(new ProductInList("бокс 30 X 20 X 2 X 6000 мм","железо","30202"));
        listMetBox.add(new ProductInList("бокс 30 X 20 X 2,5 X 6000 мм","железо","30x20x2,5"));
        listMetBox.add(new ProductInList("бокс 30 X 30 X 2 X 6000 мм","железо","15x15x2"));
        listMetBox.add(new ProductInList("бокс 30 X 30 X 3 X 6000 мм","железо","30*30*3"));
        listMetBox.add(new ProductInList("бокс 40 X 20 X 1.5 X 6000 мм","железо","40x20x1,5"));
        listMetBox.add(new ProductInList("бокс 40 X 20 X 2 X 6000 мм","железо","40x20x2"));
        listMetBox.add(new ProductInList("бокс 40 X 40 X 1.5 X 6000 мм","железо","40*40*1,5"));
        listMetBox.add(new ProductInList("бокс 40 X 40 X 2 X 6000 мм","железо","40*20*2"));
        listMetBox.add(new ProductInList("бокс 50 X 25 X 2 X 6000 мм","железо","8645тм"));
        listMetBox.add(new ProductInList("бокс 50 X 50 X 2 X 6000 мм","железо","50*50*2 6м"));



    }
}