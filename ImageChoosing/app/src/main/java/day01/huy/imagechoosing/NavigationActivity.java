package day01.huy.imagechoosing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import day01.huy.imagechoosing.Models.HistoryDTO;
import day01.huy.imagechoosing.Models.HistoryList;

public class NavigationActivity extends AppCompatActivity {

    private ImageView imgView;
    private DisplayMetrics displayMetrics;
    private LinearLayout layoutTitle;
    private ImageButton btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        imgView = findViewById(R.id.imgView);
        imgView.setImageResource(R.drawable.fafa);
        layoutTitle = findViewById(R.id.layoutTitle);
        btnHistory = findViewById(R.id.btnHistory);

        DBManager db = new DBManager(this);
        HistoryList.setList(db.getHistory());

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (displayMetrics.heightPixels * 3) / 5);
        layoutTitle.setLayoutParams(layoutParams);
        btnHistory.setImageResource(R.drawable.icons_history);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    public void chooseImgAct(View view) {
        Intent intent = new Intent(this, ImageChoosingActivity.class);
        startActivity(intent);
    }

    public void launchCamera(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void frontCamAct(View view) {
        Intent intent = new Intent(this, CameraFrontActivity.class);
        startActivity(intent);
    }

    public void clickToLogout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        UserSession.setUserId(-1);
        startActivity(intent);
    }
}
