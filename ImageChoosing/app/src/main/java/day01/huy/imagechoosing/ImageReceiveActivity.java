package day01.huy.imagechoosing;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
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

 public class ImageReceiveActivity extends AppCompatActivity {

    private ListView listView;
    private ImageView imageView;
    private List<String> resultList = new ArrayList<>();
    private List<Face> celebs = new ArrayList<>();
    private List<Celebrity> faceList = new ArrayList<>();
    private Button btnAnalyze;
    private String realPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_receive);
        Intent intent = getIntent();
        listView = findViewById(R.id.rearCamListView);
        Uri imageURI = (Uri) intent.getExtras().get("picUri");
        imageView = findViewById(R.id.imageView);
        imageView.setImageURI(imageURI);
        realPath = ReadPath.getPath(this,imageURI);
        imageProcess(imageURI);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ImageReceiveActivity.this, InfoActivity.class);
                intent.putExtra("name", resultList.get(position));
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
                            Toast.makeText(ImageReceiveActivity.this, "No Face(s) Found please try another one!", Toast.LENGTH_LONG).show();
                            listProcess();
                        } else {
                            resultList.clear();
                            Toast.makeText(ImageReceiveActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i <= celebs.size() - 1; i++) {
                                faceList = celebs.get(i).getCelebrity();
                                resultList.add(faceList.get(0).getName());
                            }
                            listProcess();
                        }

                    } else {
                        Toast.makeText(ImageReceiveActivity.this, "Cannot connect", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Toast.makeText(ImageReceiveActivity.this, "Fails", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void listProcess() {
        ArrayAdapter<String> arrayAdapter = null;
        if (!resultList.isEmpty()) {
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultList);
        }
        listView.setAdapter(arrayAdapter);
    }


}
