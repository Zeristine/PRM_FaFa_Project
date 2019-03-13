package day01.huy.imagechoosing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ImageView imgTitle;
    private EditText txtUsername, txtPassword;
    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgTitle = findViewById(R.id.imgTitle);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (displayMetrics.heightPixels * 3) / 5);
        imgTitle.setLayoutParams(imageLayoutParams);
    }

    public void clickToSignUp(View view) {
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
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
            }else{
                Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
            }
            
        } else {
            Toast.makeText(this, "Please fill your username and password", Toast.LENGTH_SHORT).show();
        }
    }
}
