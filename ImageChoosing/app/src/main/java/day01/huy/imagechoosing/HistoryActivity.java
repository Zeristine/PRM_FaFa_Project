package day01.huy.imagechoosing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        DBManager db = new DBManager(this);
        List<String[]> historyList = db.getHistory();

    }
}
