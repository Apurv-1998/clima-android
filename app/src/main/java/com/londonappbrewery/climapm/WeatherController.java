package com.londonappbrewery.climapm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;


public class WeatherController extends AppCompatActivity {
    final int REQUEST_CODE = 123;
    // Constants:
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather"; //Base Url
    // App ID to use OpenWeather data
    final String APP_ID = "e72ca729af228beabd5d20e3b7749713";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    // TODO: Set LOCATION_PROVIDER here:
    String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;


    // Member Variables:
    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;

    // TODO: Declare a LocationManager and a LocationListener here:
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);

        // Linking the elements in the layout to Java code
        mCityLabel = (TextView) findViewById(R.id.locationTV);
        mWeatherImage = (ImageView) findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = (TextView) findViewById(R.id.tempTV);
        ImageButton changeCityButton = (ImageButton) findViewById(R.id.changeCityButton);


        // TODO: Add an OnClickListener to the changeCityButton here:
        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(WeatherController.this,ChangeCityController.class);
                startActivity(myIntent);
            }
        });

    }


    // TODO: Add onResume() here:
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Clima", "onResume() called");


        //Retrieving the Intent
        Intent myIntent = getIntent();
        String city = myIntent.getStringExtra("city"); //The key value of newCityIntent is called

        //We will check if the user has provided the city
        //if yes then we will display the weather contditions for that city
        // else we will continue with our getWeatherForCurrentLocation() method

        if(city != null){

            getWeatherForNewCity(city);

        }else {


            Log.d("Clima", "Getting Weather for Current Location");
            getWeatherForCurrentLocation();
        }

    }


    // TODO: Add getWeatherForNewCity(String city) here:
    private void getWeatherForNewCity(String city){
            RequestParams params = new RequestParams();
            params.put("q",city); //key value pair
            params.put("appid",APP_ID); //key value pair
            letDoSomeNetworking(params);

    }


    // TODO: Add getWeatherForCurrentLocation() here:
    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) { //will report if location is changed

                String Longitude = String.valueOf(location.getLongitude());
                String Latitude = String.valueOf(location.getLatitude());

                Log.d("Clima","Longitude is : "+Longitude);
                Log.d("Clima","Latitude is : "+Latitude);

                //From loopj that we added in our Gradle build module
                RequestParams params = new RequestParams();
                params.put("lat",Latitude); //key and value pair
                params.put("lon",Longitude); //key and value pair
                params.put("appid",APP_ID); //key and value pair
                letDoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //most important callback
                Log.d("Clima", "onLocationChanged() callback recieved");
            }

            @Override
            public void onProviderEnabled(String s) { //will report if GPS is enabled

            }

            @Override
            public void onProviderDisabled(String s) { //will report if GPS is disabled
                //also an important one
                Log.d("Clima", "onProviderDisabled() callback recieved");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.d("Clima","Permissions Granted");
                getWeatherForCurrentLocation();
            } else {
                Log.d("Clima","Permissions Denied");
            }
        }
    }
    // TODO: Add letsDoSomeNetworking(RequestParams params) here:
    private void letDoSomeNetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                    log.d("Clima","Success! JSON : "+response.toString());

                    //Declaring the instance of the weatherdatamodel with the static method and not the constructor
                    WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
                    updateUI(weatherData);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response){
                log.e("Clima","Fail " +e.toString());
                log.d("Clima","Status code : "+statusCode);
                Toast.makeText(WeatherController.this,"Request Failed",Toast.LENGTH_SHORT).show();

            }

        });
    }


    // TODO: Add updateUI() here:
    public void updateUI(WeatherDataModel weather)
    {
        mTemperatureLabel.setText(weather.getmTemperature());
        mCityLabel.setText(weather.getmCity());

        int resourceId = getResources().getIdentifier(weather.getIconName(),"drawable",getPackageName());
        mWeatherImage.setImageResource(resourceId);
    }


    // TODO: Add onPause() here:


    @Override
    protected void onPause() {
        super.onPause();

        if(mLocationManager != null)
            mLocationManager.removeUpdates(mLocationListener);
    }
}
