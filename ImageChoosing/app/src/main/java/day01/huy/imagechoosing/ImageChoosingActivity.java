package day01.huy.imagechoosing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import day01.huy.imagechoosing.fileProcess.ImageProcess;
import day01.huy.imagechoosing.fileProcess.ReadPath;

public class ImageChoosingActivity extends AppCompatActivity {

    ImageView imgView;
    Button btn,btnProcess;
    TextView textView,textResult;
    Uri imgURI;
    String imageURIString;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_choosing);

        imgView = findViewById(R.id.imgView);
        btn = findViewById(R.id.btnChoose);
        textView = findViewById(R.id.lblURI);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE){
            imgURI = data.getData();
            imgView.setImageURI(data.getData());
            textView.setText(imgURI.toString());
            imageURIString = ReadPath.getPath(this,imgURI);
            textView.setText(imageURIString);

        }
    }



    //image process


    public void processImage(View view) {
        btnProcess = findViewById(R.id.btnProcess);
        textResult = findViewById(R.id.txtResult);
        ImageProcess imageProcess = new ImageProcess();
        imageProcess.getMimeType(imageURIString);
        try {
            textResult.setText(imageProcess.multipartRequest("https://api.sightengine.com/1.0/check.json",imageProcess.setParams(),imageURIString,"media",imageProcess.getMimeType(imageURIString)));
        }catch (Exception e){
            textResult.setText(e.getMessage());
        }

    }
}
