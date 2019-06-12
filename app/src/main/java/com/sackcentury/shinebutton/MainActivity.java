package com.sackcentury.shinebutton;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sackcentury.shinebuttonlib.ShineButton;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivityTag";
    ShineButton shineButton;

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
        shineButton.init(this);
        shineButton.setOnClickListener(view -> Log.e(TAG, "click"));
        shineButton.setOnCheckStateChangeListener((view, checked) -> Log.e(TAG, "click " + checked));
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

    private void showFragmentPage() {
        new FragmentDemo().showFragment(getSupportFragmentManager());
    }
}
