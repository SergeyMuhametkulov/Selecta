package com.example.selectasclad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnLookLoc;
    Spinner spLocation;
    ListView listViewProductLoc;
    List<String> listLocation, listProductInLoc;
    DatabaseReference aluminDataBase,metDataBase,locationDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        toolbar = findViewById(R.id.toolbar_info);
        btnLookLoc = findViewById(R.id.btn_look_location);
        listViewProductLoc = findViewById(R.id.listview_product_location);
        spLocation = findViewById(R.id.spinner_loc);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.info);
        toolbar.setBackgroundColor(getResources().getColor(R.color.info));

        aluminDataBase = FirebaseDatabase.getInstance().getReference("Alumin");
        metDataBase = FirebaseDatabase.getInstance().getReference("Met");
        locationDataBase = FirebaseDatabase.getInstance().getReference("location");

        listLocation = new ArrayList<>();
        listProductInLoc = new ArrayList<>();
        locationDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLocation.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    listLocation.add(ds.getValue(String.class));
                }
                initList(listLocation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnLookLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initListView();
            }
        });


    }

    private void initListView() {
        String selectLocation = spLocation.getSelectedItem().toString();
        listProductInLoc.clear();
        ArrayAdapter adapterProduct = new ArrayAdapter<>(InfoActivity.this, R.layout.spinner_add_new,listProductInLoc);
        listViewProductLoc.setAdapter(adapterProduct);
        aluminDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    Product pr = ds.getValue(Product.class);
                    if(pr.getLocation().equals(selectLocation)){
                        listProductInLoc.add(pr.getMaterial() + ", " + pr.getName());
                    }
                }
                adapterProduct.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        metDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    Product pr = ds.getValue(Product.class);
                    if(pr.getLocation().equals(selectLocation)){
                        listProductInLoc.add(pr.getMaterial() + ", " + pr.getName());
                    }
                }
                adapterProduct.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initList(List<String> list) {
        ArrayAdapter adapterLocation = new ArrayAdapter(InfoActivity.this,R.layout.spinner_add_new,list);
        adapterLocation.setDropDownViewResource(R.layout.spinner_add_new_drop);
        spLocation.setAdapter(adapterLocation);

    }
}