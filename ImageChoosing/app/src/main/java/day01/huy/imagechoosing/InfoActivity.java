package day01.huy.imagechoosing;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import day01.huy.imagechoosing.CelebNameAPI.FetchCelebName;
import day01.huy.imagechoosing.Models.Cele;
import day01.huy.imagechoosing.Models.Celebrity;

public class InfoActivity extends AppCompatActivity {

    private TextView txtInfoView,txtDesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        txtInfoView = findViewById(R.id.txtInfoView);
        txtDesView = findViewById(R.id.txtDesView);

        Intent intent = getIntent();
        String queryString = intent.getExtras().get("name").toString();
        txtInfoView.setText(queryString);

        //get from db
        DBManager dbManager = new DBManager(this);
        Cele cele = dbManager.getCele(queryString);
        if (cele == null) {
            String name = cele.getName();
            String description = cele.getDescription();
            txtInfoView.setText(name);
            txtDesView.setText(description);
        } else { //cele is not in DB. Get cele from wikipedia
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connMgr != null) {
                networkInfo = connMgr.getActiveNetworkInfo();
            }

            if (networkInfo != null && networkInfo.isConnected()) {
                new FetchCelebName(txtInfoView, txtDesView).execute(queryString);
                txtDesView.setText("Loading...");
                txtInfoView.setText("Loading...");
            } else {
                Toast.makeText(this, "Please check your network connection and try again", Toast.LENGTH_LONG).show();
            }
        }

    }
}
