package day01.huy.imagechoosing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtUsername = findViewById(R.id.username);
        txtPassword = findViewById(R.id.password);
    }

    public void clickToBack(View view) {
        finish();
    }

    public void clickToRegister(View view) {
        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        DBManager db = new DBManager(this);
        try{
            if(db.registed(username,password)){
                Toast.makeText(this, "You've successfully registered", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Something went wrong please try again later!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
