package day01.huy.imagechoosing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ImageView imgTitle;
    private EditText txtUsername, txtPassword;
    private DisplayMetrics displayMetrics;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int WRITE_EXTERNAL_REQ_CODE = 10;
    private Button btnToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_REQ_CODE);
        }

        imgTitle = findViewById(R.id.imgTitle);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (displayMetrics.heightPixels * 3) / 5);
        imgTitle.setLayoutParams(imageLayoutParams);
        btnToRegister = findViewById(R.id.btnToRegister);
        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
        if (requestCode == WRITE_EXTERNAL_REQ_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "external permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "external permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }



    public void clickToLogIn(View view) {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {
            //Do the check Login here
            //
            DBManager db = new DBManager(this);
            
            if(db.checkLogin(username,password)){
                Intent intent = new Intent(this, NavigationActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
            }
            
        } else {
            Toast.makeText(this, "Please fill your username and password", Toast.LENGTH_SHORT).show();
        }
    }
}
