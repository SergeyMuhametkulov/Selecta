package com.example.selectasclad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChangeLocActivity extends AppCompatActivity {

    DatabaseReference aluminDataBase,metDataBase,locationDataBase;
    TextView textLocation,textSelectProduct,textCangeTotal,textNewLocation;
    Toolbar toolbar;
    List<String> listLoc,listProduct;
    List<Product> productsInLocation,productsInNewLocation;
    Product selectProduct,productInLoc;
    ListView listView;
    String location,changeTotal,newLocation,keyProductInLoc;
    int newTotal,t;
    Button btnChangeLoc;
    Boolean bool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_loc);

        aluminDataBase = FirebaseDatabase.getInstance().getReference("Alumin");
        metDataBase = FirebaseDatabase.getInstance().getReference("Met");
        locationDataBase = FirebaseDatabase.getInstance().getReference("location");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ПЕРЕМЕЩЕНИЕ");
        toolbar.setBackgroundColor(getResources().getColor(R.color.change));

        listLoc = new ArrayList<>();
        listProduct = new ArrayList<>();
        productsInLocation = new ArrayList<>();
        productsInNewLocation = new ArrayList<>();
        initList();


        bool = true;
        textLocation = findViewById(R.id.text_location);
        textLocation.setText("ПЕРЕМЕСТИТЬ ИЗ ");
        textSelectProduct = findViewById(R.id.text_select_product);
        textSelectProduct.setText("ЧТО ПЕРЕМЕСТИТЬ");
        textSelectProduct.setVisibility(View.INVISIBLE);
        textNewLocation = findViewById(R.id.text_new_location);
        textNewLocation.setText("КУДА ПЕРЕМЕСТИТЬ");
        textNewLocation.setVisibility(View.INVISIBLE);
        textCangeTotal = findViewById(R.id.text_change_total);
        textCangeTotal.setText("СКОЛЬКО ПЕРЕМЕСТИТЬ");
        textCangeTotal.setVisibility(View.INVISIBLE);
        btnChangeLoc = findViewById(R.id.btn_change_loc);
        btnChangeLoc.setVisibility(View.INVISIBLE);



        textSelectProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangeProduct();
            }
        });

        textLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangeLoc();
            }
        });
        textNewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangeNewLoc();
            }
        });
        textCangeTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputChangeTotal();
            }
        });
        btnChangeLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectProduct.getMaterial().equals("алюминий")){
                    addNewTotal(aluminDataBase);
                }
                if (selectProduct.getMaterial().equals("железо")){
                    addNewTotal(metDataBase);
                }else return;
            }
        });


    }

    private void changeLocation(DatabaseReference db) {

        if(Integer.parseInt(changeTotal)>Integer.parseInt(selectProduct.getTotal())){
            Toast.makeText(this, "нет такого кол-ва", Toast.LENGTH_SHORT).show();
            return;
        }

        if(newLocation.equals(selectProduct.getLocation())){
            Toast.makeText(this, "То же самое место", Toast.LENGTH_SHORT).show();
            return;
        }

        db.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   for(DataSnapshot ds : snapshot.getChildren()){
                       Product pr = ds.getValue(Product.class);
                       if(pr.getArticle().equals(selectProduct.getArticle()) && pr.getLocation().equals(newLocation)){
                           productInLoc = pr;
                           bool = false;
                           break;
                       }
                   }
               }


               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }

        });

        if(bool){
            String newId = db.push().getKey();
            int n = Integer.parseInt(selectProduct.getTotal()) - Integer.parseInt(changeTotal);
            db.child(newId).setValue(new Product(newId,selectProduct.getMaterial(),selectProduct.getArticle(),
                    selectProduct.getName(),selectProduct.getParam(),newLocation,changeTotal,selectProduct.getUnit()));
            if (n>0){
                db.child(selectProduct.getId()).child("total").setValue(String.valueOf(n));
            }else {
                db.child(selectProduct.getId()).removeValue();
            }

        }
        finish();


    }

    private void addNewTotal(DatabaseReference database) {
        int m = Integer.parseInt(changeTotal) + Integer.parseInt(productInLoc.getTotal());
        int k = Integer.parseInt(selectProduct.getTotal()) - Integer.parseInt(changeTotal);
        String l = String.valueOf(m);
        String s = String.valueOf(k);
        database.child(productInLoc.getId()).child("total").setValue(l).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ChangeLocActivity.this, "ПЕРЕМЕЩЕНИЕ ВЫПОЛНЕНО", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangeLocActivity.this, "ОШИБКА", Toast.LENGTH_SHORT).show();
            }
        });
        if (k>0){
            database.child(selectProduct.getId()).child("total").setValue(s);
        }else {
            database.child(selectProduct.getId()).removeValue();
        }
        finish();

    }


    private void showDialogChangeNewLoc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeLocActivity.this);
        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_change_location,null);
        listView = rl.findViewById(R.id.listview);
        locationDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLoc.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    listLoc.add(ds.getValue(String.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayAdapter newLocAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listLoc);
        listView.setAdapter(newLocAdapter);

        builder.setView(rl);
        AlertDialog dialog = builder.create();
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textNewLocation.setText("ПЕРЕМЕСТИТЬ В : " + listLoc.get(i));
                newLocation = listLoc.get(i);
                btnChangeLoc.setVisibility(View.VISIBLE);

                dialog.dismiss();
                if(selectProduct.getMaterial().equals("алюминий")){
                    changeLocation(aluminDataBase);
                }
                if(selectProduct.getMaterial().equals("железо")){
                    changeLocation(metDataBase);
                }
            }
        });


    }

    private void showDialogChangeProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeLocActivity.this);
        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_change_location,null);
        listView = rl.findViewById(R.id.listview);
        ArrayAdapter productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listProduct);
        aluminDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLoc.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getLocation().equals(location)){
                        listProduct.add(product.getMaterial()+" "+product.getName()+" "+product.getParam()+
                                ", кол-во "+product.getTotal());
                        productsInLocation.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        metDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLoc.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getLocation().equals(location)){
                        listProduct.add(product.getMaterial()+" "+product.getName()+" "+product.getParam()+
                                ", кол-во "+product.getTotal());
                        productsInLocation.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setAdapter(productAdapter);

        builder.setView(rl);
        AlertDialog dialog = builder.create();
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textSelectProduct.setText("ПЕРЕМЕСТИТЬ: " + listProduct.get(i));
                selectProduct = productsInLocation.get(i);
                textCangeTotal.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

    }

    private void inputChangeTotal() {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(ChangeLocActivity.this);
        View lView = getLayoutInflater().inflate(R.layout.dialog_add_new_loc,null);
        EditText edtTxtChangeTotal = lView.findViewById(R.id.edt_txt_new_location);
        edtTxtChangeTotal.setHint("Введите кол-во");
        lBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(edtTxtChangeTotal.getText().toString())){
                    Toast.makeText(ChangeLocActivity.this, "введите кол-во", Toast.LENGTH_SHORT).show();
                    return;
                }changeTotal = edtTxtChangeTotal.getText().toString();
                textCangeTotal.setText("ПЕРЕМЕСТИТЬ КОЛ-ВО: "+ changeTotal);
                textNewLocation.setVisibility(View.VISIBLE);


            }
        });


        lBuilder.setView(lView);
        AlertDialog dialog = lBuilder.create();
        dialog.show();
    }

    private void initList() {
        listLoc.clear();
        locationDataBase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    listLoc.add(ds.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDialogChangeLoc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeLocActivity.this);
        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_change_location,null);
        listView = rl.findViewById(R.id.listview);
        locationDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLoc.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    listLoc.add(ds.getValue(String.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayAdapter locAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listLoc);
        listView.setAdapter(locAdapter);

        builder.setView(rl);
        AlertDialog dialog = builder.create();
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textLocation.setText("ПЕРЕМЕСТИТЬ ИЗ: " + listLoc.get(i));
                textSelectProduct.setVisibility(View.VISIBLE);
                location = listLoc.get(i);
                dialog.dismiss();
            }
        });

    }


}