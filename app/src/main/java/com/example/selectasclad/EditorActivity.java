package com.example.selectasclad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditorActivity extends AppCompatActivity {
    Button addNewProduct,addNewLocation,changeLocation;
    DatabaseReference aluminDataBase,metDataBase,locationDataBase;
    Product newProduct;
    List<String> listLocation,listMaterial;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        aluminDataBase = FirebaseDatabase.getInstance().getReference("Alumin");
        metDataBase = FirebaseDatabase.getInstance().getReference("Met");
        locationDataBase = FirebaseDatabase.getInstance().getReference("location");

        addNewProduct = findViewById(R.id.btn_add_new_product);
        addNewLocation = findViewById(R.id.btn_add_location);
        changeLocation = findViewById(R.id.btn_change_location);

        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        addNewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewLocation();
            }
        });

        listLocation = new ArrayList<>();
        listMaterial = new ArrayList<>();
        initList();

    }

    private void addNewLocation() {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(EditorActivity.this);
        View lView = getLayoutInflater().inflate(R.layout.dialog_add_new_loc,null);
        EditText edtTxtNewLocation = lView.findViewById(R.id.edt_txt_new_location);
        lBuilder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(edtTxtNewLocation.getText().toString())){
                    Toast.makeText(EditorActivity.this, "Ведите место", Toast.LENGTH_SHORT).show();
                    return;
                }
                locationDataBase.push().setValue(edtTxtNewLocation.getText().toString());

            }
        });


        lBuilder.setView(lView);
        AlertDialog dialog = lBuilder.create();
        dialog.show();
    }

    private void initList() {
        listMaterial.add("железо");
        listMaterial.add("алюминий");
        listLocation.add("ворота 1");
        listLocation.add("ворота 2");
    }

    public  void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_new_pr,null);
        builder.setTitle("Введите данные");
        Spinner spinnerMaterial = mView.findViewById(R.id.spinner_material);
        Spinner spinnerLocation = mView.findViewById(R.id.spinner_location);
        EditText editArticle = mView.findViewById(R.id.ed_txt_article);
        EditText editName = mView.findViewById(R.id.ed_txt_name);
        EditText editParam = mView.findViewById(R.id.ed_txt_param);

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(editArticle.getText().toString())){
                    Toast.makeText(EditorActivity.this, "Ведите артикул", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(editName.getText().toString())){
                    Toast.makeText(EditorActivity.this, "Ведите название", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(editParam.getText().toString())){
                    Toast.makeText(EditorActivity.this, "Ведите параметры", Toast.LENGTH_SHORT).show();
                    return;
                }
                newProduct = new Product(spinnerMaterial.getSelectedItem().toString(),editArticle.getText().toString()
                        ,editName.getText().toString(),editParam.getText().toString(),spinnerLocation.getSelectedItem().toString(),"1");
                Toast.makeText(EditorActivity.this,"Добавлено", Toast.LENGTH_SHORT).show();
                if(newProduct.getMaterial().equals("алюминий")){
                    aluminDataBase.push().setValue(newProduct);
                }
                if(newProduct.getMaterial().equals("железо")){
                    metDataBase.push().setValue(newProduct);
                }

            }
        });

        ArrayAdapter adapterMaterial = new ArrayAdapter(getApplicationContext(),R.layout.spinner_add_new,listMaterial);
        adapterMaterial.setDropDownViewResource(R.layout.spinner_add_new_drop);
        spinnerMaterial.setAdapter(adapterMaterial);

        ArrayAdapter adapterLocation = new ArrayAdapter(getApplicationContext(),R.layout.spinner_add_new,listLocation);
        adapterLocation.setDropDownViewResource(R.layout.spinner_add_new_drop);
        spinnerLocation.setAdapter(adapterLocation);

        builder.setView(mView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}