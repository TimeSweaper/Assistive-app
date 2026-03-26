package com.example.assitive;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity{
    EditText email, password;
    Button loginBtn;
    TextView goSignup;
    ADatabase ADatabase;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("user_session",MODE_PRIVATE);
        String savedEmail = prefs.getString("email",null);

        if(savedEmail != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        ADatabase = new ADatabase(this);

        email  = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        goSignup = findViewById(R.id.goSignup);


        loginBtn.setOnClickListener(v -> {
            String e = email.getText().toString();
            String p = password.getText().toString();

            if(e.isEmpty() || p.isEmpty()){
                Toast.makeText(this,"Fill detail",Toast.LENGTH_SHORT).show();
            }
            else{
                boolean valid = ADatabase.checkUser(e,p);
                if(valid){
                    Toast.makeText(this,"Login Successfully",Toast.LENGTH_SHORT).show();


                    android.content.SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("email",e);
                    editor.apply();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(this,"No user found..",Toast.LENGTH_SHORT).show();
                }
            }
        });

        goSignup.setOnClickListener(v -> {

            startActivity(new Intent(this,SignupActivity.class));
        });
    }
}
