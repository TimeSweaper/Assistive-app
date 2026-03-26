package com.example.assitive;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends  AppCompatActivity{
    EditText name, email, password, phone, disability;
    Button signupBtn;
    ADatabase ADatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ADatabase = new ADatabase(this);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signupBtn = findViewById(R.id.signupBtn);
        phone = findViewById(R.id.phone);
        disability = findViewById(R.id.disability);

        signupBtn.setOnClickListener(v -> {

            String n = name.getText().toString().trim();
            String e = email.getText().toString().trim();
            String p = password.getText().toString().trim();
            String ph = phone.getText().toString().trim();
            String d = disability.getText().toString().trim();

            if (n.isEmpty() || e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            } else {

                boolean inserted = ADatabase.insertUser(n, e, p, ph, d);

                if (inserted) {
                    Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
