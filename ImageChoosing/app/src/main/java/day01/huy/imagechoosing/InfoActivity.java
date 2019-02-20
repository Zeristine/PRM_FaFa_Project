package day01.huy.imagechoosing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private TextView txtInfoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        txtInfoView = findViewById(R.id.txtInfoView);

        Intent intent = getIntent();
        txtInfoView.setText(intent.getExtras().get("name").toString());
    }
}
