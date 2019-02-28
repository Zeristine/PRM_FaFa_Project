package day01.huy.imagechoosing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class NavigationActivity extends AppCompatActivity {

    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        imgView = findViewById(R.id.imgView);
        imgView.setImageResource(R.drawable.fafa);
    }

    public void rearCamAct(View view) {

    }

    public void chooseImgAct(View view) {
        Intent intent = new Intent(this, ImageChoosingActivity.class);
        startActivity(intent);
    }

    public void frontCamAct(View view) {

    }
}
