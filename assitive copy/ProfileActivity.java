package com.example.assitive;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.database.Cursor;


public class ProfileActivity extends AppCompatActivity{
    EditText name,phone,disability;
    Button updateBtn;
    ADatabase ADatabase;

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        disability = findViewById(R.id.disability);
        updateBtn = findViewById(R.id.updateBtn);

        ADatabase = new ADatabase(this);

        SharedPreferences prefs = getSharedPreferences("user_session",MODE_PRIVATE);
        userEmail =  prefs.getString("email",null);


        loadUserData();
        updateBtn.setOnClickListener(v->updateProfile());
    }

    private void loadUserData(){
        Cursor cursor = ADatabase.getUser(userEmail);

        if(cursor.moveToFirst()){
            name.setText(cursor.getString(1));
            phone.setText(cursor.getString(4));
            disability.setText(cursor.getString(5));

        }
        cursor.close();
    }

    private void updateProfile(){
        String n = name.getText().toString().trim();
        String p = phone.getText().toString().trim();
        String d = disability.getText().toString().trim();

        boolean update = ADatabase.updateProfile(userEmail,n,p,d);
        if(update){
            Toast.makeText(this,"Profile Updated",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Update Failed",Toast.LENGTH_SHORT).show();
        }
    }
}
