package day01.huy.imagechoosing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import day01.huy.imagechoosing.CelebNameAPI.FetchCelebName;
import day01.huy.imagechoosing.CelebNameAPI.FetchCelebResponse;
import day01.huy.imagechoosing.CelebNameAPI.InterfaceFetchCelebResponse;
import day01.huy.imagechoosing.Models.Cele;
import day01.huy.imagechoosing.Models.HistoryDTO;
import day01.huy.imagechoosing.Models.HistoryList;
import day01.huy.imagechoosing.fileProcess.ImageProcess;
import day01.huy.imagechoosing.fileProcess.ImageProcessWithDB;

public class InfoActivity extends AppCompatActivity implements InterfaceFetchCelebResponse {

    private TextView txtInfoView, txtDesView;
    private ImageView celebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        txtInfoView = findViewById(R.id.txtInfoView);
        txtDesView = findViewById(R.id.txtDesView);

        Intent intent = getIntent();
        String queryString = (String) intent.getExtras().get("name");
        txtInfoView.setText(queryString);

        //get from db
        DBManager dbManager = new DBManager(this);
        Cele cele = dbManager.getCele(queryString);
        if (cele != null) {
            String url = cele.getUrl();
            String name = cele.getName();
            String description = cele.getDescription();
            txtInfoView.setText(name);
            txtDesView.setText(description);
            new ImageProcessWithDB((ImageView) findViewById(R.id.celebImage)).execute(url);
            //add to History
            dbManager.addHistory(cele);
            //get add to List
            HistoryDTO dto;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String today = simpleDateFormat.format(date);
            dto = new HistoryDTO(name,today);
            List<HistoryDTO> list = HistoryList.getList();
            list.add(dto);
            HistoryList.setList(list);
        } else { //cele is not in DB. Get cele from wikipedia
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connMgr != null) {
                networkInfo = connMgr.getActiveNetworkInfo();
            }

            if (networkInfo != null && networkInfo.isConnected()) {
                ImageProcess imgAsyncTask = new ImageProcess((ImageView) findViewById(R.id.celebImage));
                imgAsyncTask.callback = (String imgUrl) -> {
                    FetchCelebName asyncTask = new FetchCelebName(txtInfoView, txtDesView, imgUrl);
                    asyncTask.infoActivity = this;
                    asyncTask.execute(queryString);
                    txtDesView.setText("Loading...");
                    txtInfoView.setText("Loading...");
                };
                imgAsyncTask.execute(queryString);


            } else {
                Toast.makeText(this, "Please check your network connection and try again", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onFetchCelebResponse(FetchCelebResponse fcr) {
    // TODO: insert to DB
        DBManager db = new DBManager(this);
        Cele cele = new Cele();
        if(fcr.getDes().contains("may refer to:") || fcr.getDes().contains("also refer to:")){
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("name",fcr.getName());
            startActivity(intent);
            finish();
        }else{
            cele.setUrl(fcr.getImgUrl());
            cele.setDescription(fcr.getDes());
            cele.setName(fcr.getName());
            if(db.addCele(cele)){
                Toast.makeText(this, "This search is provided by Wikipedia and Bing Image Search, data has been added to DB.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Cannot add to DB", Toast.LENGTH_SHORT).show();
            }
            db.addHistory(cele);
            HistoryDTO dto;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String today = simpleDateFormat.format(date);
            dto = new HistoryDTO(fcr.getName(),today);
            List<HistoryDTO> list = HistoryList.getList();
            list.add(dto);
            HistoryList.setList(list);
        }
    }
}
