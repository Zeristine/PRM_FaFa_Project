package day01.huy.imagechoosing;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellIdentity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import day01.huy.imagechoosing.Api.ApiController;
import day01.huy.imagechoosing.Api.CheckImageClient;
import day01.huy.imagechoosing.Models.Celebrity;
import day01.huy.imagechoosing.Models.Face;
import day01.huy.imagechoosing.Models.Result;
import day01.huy.imagechoosing.fileProcess.ReadPath;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraFrontReceiveActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button moreInfo, analyzeFront;
    private TextView txtName, txtPercent;
    private String realPath;
    private List<String> resultList = new ArrayList<>();
    private List<Face> celebs = new ArrayList<>();
    private List<Celebrity> faceList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_front_receive);
        imageView = findViewById(R.id.imageView);
        moreInfo = findViewById(R.id.btnMoreInfo);
        analyzeFront = findViewById(R.id.btnAnalyzeFront);
        txtName = findViewById(R.id.txtName);
        txtPercent = findViewById(R.id.txtPercent);
        Intent intent = getIntent();
        Uri imageURI = (Uri) intent.getExtras().get("picUri");
        imageView.setImageURI(imageURI);
        realPath = ReadPath.getPath(this,imageURI);
        imageProcess(imageURI);
        analyzeFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageProcess(imageURI);
            }
        });
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraFrontReceiveActivity.this,InfoActivity.class);
                intent.putExtra("name",txtName.getText().toString());
                startActivity(intent);
            }
        });

    }

    private void imageProcess( Uri imgURI){
        if (imgURI == null) {
            Toast.makeText(this, "Please select an image first!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Your request is being sent, please wait...", Toast.LENGTH_LONG).show();

            String api_user = "947674538";
            String api_sceret = "jwRJBvFs3zxGJa7jTXaH";
            String models = "celebrities";

            File file = new File(realPath);

            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            final RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
            MultipartBody.Part media = MultipartBody.Part.createFormData("media", file.getName(), requestFile);

            RequestBody user = RequestBody.create(MultipartBody.FORM, api_user);
            RequestBody secret = RequestBody.create(MultipartBody.FORM, api_sceret);
            RequestBody model = RequestBody.create(MultipartBody.FORM, models);


            CheckImageClient client = ApiController.checkImage();

            Call<Result> call = client.upload(user, secret, model, media);
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    if (response.isSuccessful()) {

                        celebs = response.body().getFaces();

                        if (celebs.isEmpty()) {
                            resultList.clear();
                            Toast.makeText(CameraFrontReceiveActivity.this, "No Face(s) Found please try another one!", Toast.LENGTH_LONG).show();
//                            listProcess();
                        } else {
                            if(celebs.size()>1){
                                Toast.makeText(CameraFrontReceiveActivity.this, "Look it's more than one face", Toast.LENGTH_SHORT).show();
                            }else{

                                faceList = celebs.get(0).getCelebrity();
                                String name = faceList.get(0).getName();
                                double prob = faceList.get(0).getProb();
                                txtName.setText(name);
                                txtPercent.setText(prob*100+"%");
                                moreInfo.setVisibility(View.VISIBLE);
                                Toast.makeText(CameraFrontReceiveActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
//                            resultList.clear();
//                            Toast.makeText(CameraFrontReceiveActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                            for (int i = 0; i <= celebs.size() - 1; i++) {
//                                faceList = celebs.get(i).getCelebrity();
//                                resultList.add(faceList.get(0).getName());
//                            }
//                            listProcess();
                        }

                    } else {
                        Toast.makeText(CameraFrontReceiveActivity.this, "Cannot connect", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Toast.makeText(CameraFrontReceiveActivity.this, "Fails", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
