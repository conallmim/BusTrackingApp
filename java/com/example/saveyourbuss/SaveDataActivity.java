package com.example.saveyourbuss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SaveDataActivity extends AppCompatActivity {

    private EditText numberBusEditText,aboutBusEditText;
    private TextView latitudeTextView,longitudeTextView;
    private Button submitBtn;
    private String latt,lann,numberbus,about;
    DBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data);

        numberBusEditText = findViewById(R.id.numberBusEditText);
        aboutBusEditText = findViewById(R.id.aboutBusEditText);
        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);




        submitBtn = findViewById(R.id.submitBtn);
        Bundle bnd1 = getIntent().getExtras();
        helper = new DBHelper(this);
        final double lat = bnd1.getDouble("LAT");
        final double lan = bnd1.getDouble("LAN");

        String newlat = Double.toString(lat);
        String newlan = Double.toString(lan);

        latitudeTextView.setText(newlat);
        longitudeTextView.setText(newlan);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValues();
            }
        });

    }

    private void checkValues(){
        latt = latitudeTextView.getText().toString();
        lann = longitudeTextView.getText().toString();
        numberbus = numberBusEditText.getText().toString();
        about = aboutBusEditText.getText().toString();


       if (numberbus.isEmpty()){
           numberBusEditText.setError("Input Number!");
           return;
       }else if (about.isEmpty()){
           aboutBusEditText.setError("Input Text!");
           return;
       }else{
            submitBusValue();
       }
    }
    private void submitBusValue(){
        Bundle bnd1 = getIntent().getExtras();
        final double lat = bnd1.getDouble("LAT");
        final double lan = bnd1.getDouble("LAN");

        helper.addData(lat, lan, numberbus, about);
        Intent moveback = new Intent(SaveDataActivity.this, MapsActivity.class);
        startActivity(moveback);
        finish();
        Toast.makeText(this, "Saved Succeed!", Toast.LENGTH_SHORT).show();
    }
}
