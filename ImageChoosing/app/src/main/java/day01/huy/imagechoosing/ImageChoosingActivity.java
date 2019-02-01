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
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import day01.huy.imagechoosing.Api.ApiController;
import day01.huy.imagechoosing.Api.CheckImageClient;
import day01.huy.imagechoosing.Models.Celebrity;
import day01.huy.imagechoosing.Models.Face;
import day01.huy.imagechoosing.Models.Result;
import day01.huy.imagechoosing.fileProcess.ImageProcess;
import day01.huy.imagechoosing.fileProcess.ReadPath;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageChoosingActivity extends AppCompatActivity {

    ImageView imgView;
    Button btn,btnProcess;
    TextView textView,textResult;
    Uri imgURI;
    String imageURIString;
    private static final int PICK_IMAGE = 100;
    List<Face> celebs = new ArrayList<>();

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
       //textResult = findViewById(R.id.txtResult);

       //new Thread(new Runnable() {
       //    ImageProcess imageProcess = new ImageProcess();
       //    @Override
       //    public void run() {
       //        try {
       //            textResult.setText(imageProcess.multipartRequest("https://api.sightengine.com/1.0/check.json",imageProcess.setParams(),imageURIString,"media",imageProcess.getMimeType(imageURIString)));
       //        }catch (Exception e){
       //            textResult.setText(e.getMessage());
       //        }
       //    }
       //}){
       //}.start();
        String api_user = "947674538";
        String api_sceret = "jwRJBvFs3zxGJa7jTXaH";
        String models = "celebrities";
        File file = new File(imageURIString);
        RequestBody requestFile = RequestBody.create(MediaType.parse(getApplication().getContentResolver().getType(imgURI)), file);
        MultipartBody.Part media = MultipartBody.Part.createFormData("media", file.getName(), requestFile);

        RequestBody user = RequestBody.create(MultipartBody.FORM, api_user);
        RequestBody secret = RequestBody.create(MultipartBody.FORM, api_sceret);
        RequestBody model = RequestBody.create(MultipartBody.FORM, models);


        CheckImageClient client = ApiController.checkImage();

        Call<Result> call = client.upload(user, secret, model,media);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ImageChoosingActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    celebs = response.body().getFaces();
                }
                else {
                    Toast.makeText(ImageChoosingActivity.this, "Cant connect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(ImageChoosingActivity.this, "Fails", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
