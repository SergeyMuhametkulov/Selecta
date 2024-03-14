package com.example.selectasclad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    private Button priem,vidacha,editor,info;
    Intent intent;
    ImageView imageVinny;
    TextView textView;
    Animation animVinny,animText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        intent = new Intent(MenuActivity.this,PVActivity.class);
        priem = findViewById(R.id.btn_priem);
        vidacha = findViewById(R.id.btn_vidacha);
        editor = findViewById(R.id.btn_editor);
        info = findViewById(R.id.btn_info);
        imageVinny = findViewById(R.id.imageVinny);
        textView = findViewById(R.id.textView);

        animVinny = AnimationUtils.loadAnimation(this,R.anim.anim_vinny);
        imageVinny.startAnimation(animVinny);
        imageVinny.setVisibility(View.INVISIBLE);
        animText = AnimationUtils.loadAnimation(this,R.anim.anim_text);
        textView.startAnimation(animText);
        textView.setVisibility(View.INVISIBLE);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==priem.getId()){
                    intent.putExtra("message","priem");
                    startActivity(intent);
                }if(view.getId()==vidacha.getId()){
                    intent.putExtra("message","vidacha");
                    startActivity(intent);
                }
            }
        };
        priem.setOnClickListener(onClickListener);
        vidacha.setOnClickListener(onClickListener);
        info.setOnClickListener(onClickListener);

        editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent ed_intent = new Intent(MenuActivity.this, EditorActivity.class);
                startActivity(ed_intent);
            }
        });


    }
}