package day01.huy.imagechoosing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
    private static final int REQUEST_CODE = 100;
    private Button btnToRegister;
    private final String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions,
                    REQUEST_CODE);
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
        if (requestCode == REQUEST_CODE) {
            for (String permission : permissions) {
                switch (permission) {
                    case Manifest.permission.CAMERA:
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "external permission granted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "external permission denied", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
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
            int result = db.checkLogin(username, password);
            if (result != -1) {
                UserSession.setUserId(result);
                Intent intent = new Intent(this, NavigationActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please fill your username and password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasPermissions(String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
