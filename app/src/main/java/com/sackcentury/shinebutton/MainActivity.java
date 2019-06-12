package com.sackcentury.shinebutton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.sackcentury.shinebuttonlib.ShineButton;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    ShineButton shineButton;
    ShineButton porterShapeImageView1;
    ShineButton porterShapeImageView2;
    ShineButton porterShapeImageView3;

    Button listDemo;
    Button fragmentDemo;
    Button dialogDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shineButton = findViewById(R.id.po_image0);
        listDemo = findViewById(R.id.btn_list_demo);
        fragmentDemo = findViewById(R.id.btn_fragment_demo);
        dialogDemo = findViewById(R.id.btn_dialog_demo);


        LinearLayout linearLayout = findViewById(R.id.wrapper);

        if (shineButton != null)
            shineButton.init(this);
        porterShapeImageView1 = findViewById(R.id.po_image1);
        if (porterShapeImageView1 != null)
            porterShapeImageView1.init(this);
        porterShapeImageView2 = findViewById(R.id.po_image2);
        if (porterShapeImageView2 != null)
            porterShapeImageView2.init(this);
        porterShapeImageView3 = findViewById(R.id.po_image3);
        if (porterShapeImageView3 != null)
            porterShapeImageView3.init(this);


        ShineButton shineButtonJava = new ShineButton(this);

        shineButtonJava.setBtnColor(Color.GRAY);
        shineButtonJava.setBtnFillColor(Color.RED);
        shineButtonJava.setShapeResource(R.raw.heart);
        shineButtonJava.setAllowRandomColor(true);
        shineButtonJava.setShineSize(100);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        shineButtonJava.setLayoutParams(layoutParams);
        if (linearLayout != null) {
            linearLayout.addView(shineButtonJava);
        }


        shineButton.setOnClickListener(view -> Log.e(TAG, "click"));
        shineButton.setOnCheckStateChangeListener((view, checked) -> Log.e(TAG, "click " + checked));

        porterShapeImageView2.setOnClickListener(view -> Log.e(TAG, "click"));
        porterShapeImageView3.setOnClickListener(view -> Log.e(TAG, "click"));
        listDemo.setOnClickListener(v -> startActivity(new Intent(v.getContext(), ListDemoActivity.class)));
        fragmentDemo.setOnClickListener(v -> showFragmentPage());
        dialogDemo.setOnClickListener(v -> {
            Dialog dialog = new Dialog(MainActivity.this);
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog, null);
            ShineButton shineButton = view.findViewById(R.id.po_image);
            shineButton.setOnClickListener(v1 -> Log.e(TAG, "click"));
            dialog.setContentView(view);
            dialog.show();
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void showFragmentPage() {
        new FragmentDemo().showFragment(getSupportFragmentManager());
    }
}
