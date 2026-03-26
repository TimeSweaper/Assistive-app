package com.example.assitive;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageManager;

import java.util.Locale;
import android.speech.RecognizerIntent;
import android.app.Activity;

import java.util.ArrayList;
import android.telephony.SmsManager;
import com.google.android.gms.location.*;

public class MainActivity extends AppCompatActivity {
    ADatabase ADatabase;
    TextView userName;
    EditText inputText;
    Button speakBtn;
    TextToSpeech tts;

    Button voiceBtn;

    String emergencyNumber = "7983262199";
    FusedLocationProviderClient fusedLocationClient;

    Button emergencyBtn;

    Button profileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    android.Manifest.permission.CALL_PHONE,
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.SEND_SMS,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, 1);
        }

            ADatabase = new ADatabase(this);

            userName = findViewById(R.id.userName);
            inputText = findViewById(R.id.inputText);
            speakBtn  = findViewById(R.id.speakBtn);
            voiceBtn = findViewById(R.id.voiceBtn);

            profileBtn = findViewById(R.id.profileBtn);
            profileBtn.setOnClickListener(v->{
                startActivity(new Intent(this, ProfileActivity.class));
            });

            emergencyBtn = findViewById(R.id.emergencyBtn);
            emergencyBtn.setOnClickListener(v->{
                triggerEmergency();
            });


            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



            android.content.SharedPreferences prefs = getSharedPreferences("user_session",MODE_PRIVATE);
            String email = prefs.getString("email",null);

            if(email != null) {
                android.database.Cursor cursor = ADatabase.getUser(email);
                if (cursor.moveToFirst()) {
                    String name = cursor.getString(1);
                    userName.setText("Welcome " + name);
                    cursor.close();
                }
            }

            tts = new TextToSpeech(this,status -> {
                if(status == TextToSpeech.SUCCESS){
                    int result = tts.setLanguage(new Locale("hi","IN"));

                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        tts.setLanguage(Locale.ENGLISH);
                    }
                }
            });

            speakBtn.setOnClickListener(v ->{
                String text = inputText.getText().toString();

                if(text.isEmpty()){
                    Toast.makeText(this,"Enter text first",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(tts != null){
                        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
                    }

                    if(text.contains("help") ||
                            text.contains("emergency") ||
                            text.contains("bachao")){
                        triggerEmergency();
                    }
                }
            });

            voiceBtn.setOnClickListener(v->{
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en_US");
//                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak Something...");

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi-IN");

                try{
                    speechLauncher.launch(intent);

                }
                catch (Exception e){
                    Toast.makeText(this,"Voice not supported",Toast.LENGTH_SHORT).show();
                }
            });
    }

    private final androidx.activity.result.ActivityResultLauncher<Intent> speechLauncher =
            registerForActivityResult(
                    new androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                            ArrayList<String> data =
                                    result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                            String spokenText = data.get(0).toLowerCase();

                            inputText.setText(spokenText);
                            tts.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null, null);

                            if (spokenText.contains("help") ||
                                    spokenText.contains("emergency") ||
                                    spokenText.contains("bachao")) {
                                triggerEmergency();
                            }
                        }
                    });

    private void triggerEmergency(){
        Toast.makeText(this,"Emergency Triggered",Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return;
            }
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                String message = "Emergency I need help.\nLocation: https://maps.google.com/?q=" + lat + "," + lon;
                sendSMS(message);
            } else {
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                sendSMS("Emergency! Location not available.");
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show();
        });

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(android.net.Uri.parse("tel:"+emergencyNumber));

        try{
            startActivity(callIntent);
        }
        catch (SecurityException e){
            Toast.makeText(this,"Permission not granted",Toast.LENGTH_SHORT).show();
        }
    }

private void sendSMS(String message){
        try{
            SmsManager smsManager =  SmsManager.getDefault();
            smsManager.sendTextMessage(emergencyNumber,null,message,null,null);
            Toast.makeText(this,"SMS Sent with location!",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"SMS Failed",Toast.LENGTH_SHORT).show();
        }
}

@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == 1) {
        Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
    }
}
    @Override
    protected void onDestroy(){
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}