package com.londonappbrewery.climapm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChangeCityController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_city_layout);

        final EditText editTextField = findViewById((R.id.queryET));
        ImageButton backButton = findViewById((R.id.backButton));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); //Because of this our activity will be destroyed and our screen will go back to the previous activity
            }
        });

        editTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                String newCity = editTextField.getText().toString();
                Intent newCityIntent = new Intent(ChangeCityController.this,WeatherController.class);

                //To get the Weather Controller get the name of the city we add the newCityIntent with an extra
                //inputs are any arbitary string as the key and the city entered as the value
                newCityIntent.putExtra("city",newCity);
                startActivity(newCityIntent);
                return false;
            }
        });
    }

}
