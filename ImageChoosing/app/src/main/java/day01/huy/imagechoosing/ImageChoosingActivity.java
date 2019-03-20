package day01.huy.imagechoosing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
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

public class ImageChoosingActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private ImageView imgView;
    private Button btn;
    private Uri imgURI;
    private String imageURIString;
    private List<Face> celebs = new ArrayList<>();
    private List<Celebrity> faceList = new ArrayList<>();
    private List<String> resultList = new ArrayList<>();
    private DisplayMetrics displayMetrics;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_choosing);

        imgView = findViewById(R.id.imgView);
        btn = findViewById(R.id.btnChoose);
        listView = findViewById(R.id.listResult);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        resizeView(imgView, 3, 5);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

                resizeView(imgView, 3, 5);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ImageChoosingActivity.this, InfoActivity.class);
                intent.putExtra("name", resultList.get(position));
                startActivity(intent);
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imgURI = data.getData();
            imgView.setImageURI(data.getData());
            imageURIString = ReadPath.getPath(this, imgURI);
        }

    }

    private void listProcess() {
        ArrayAdapter<String> arrayAdapter = null;
        if (!resultList.isEmpty()) {
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultList);
        }
        listView.setAdapter(arrayAdapter);
    }

    private void resizeView(View view, int x, int y) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (displayMetrics.heightPixels * x) / y);
        view.setLayoutParams(layoutParams);
    }


    //image process


    public void processImage(View view) {

        if (imgURI == null) {
            Toast.makeText(this, "Please select an image first!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Your request is being sent, please wait...", Toast.LENGTH_LONG).show();

            String api_user = "947674538";
            String api_sceret = "jwRJBvFs3zxGJa7jTXaH";
            String models = "celebrities";

            File file = new File(imageURIString);
            System.out.println("RealpathI: "+imageURIString);
            System.out.println("FileI: "+file);

            final RequestBody requestFile = RequestBody.create(MediaType.parse(getApplication().getContentResolver().getType(imgURI)), file);
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
                            Toast.makeText(ImageChoosingActivity.this, "No Face(s) Found please try another one!", Toast.LENGTH_LONG).show();
                            listProcess();
                        } else {
                            resultList.clear();
                            Toast.makeText(ImageChoosingActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i <= celebs.size() - 1; i++) {
                                faceList = celebs.get(i).getCelebrity();
                                resultList.add(faceList.get(0).getName());
                            }
                            listProcess();
                        }

                    } else {
                        Toast.makeText(ImageChoosingActivity.this, "Cannot connect", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Toast.makeText(ImageChoosingActivity.this, "Fails", Toast.LENGTH_SHORT).show();
                }
            });

            resizeView(imgView, 2, 5);
        }
    }


}
