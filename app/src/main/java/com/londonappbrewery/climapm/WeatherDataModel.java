package com.londonappbrewery.climapm;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataModel {

    // TODO: Declare the member variables here
    private String mTemperature;
    private String mCity;
    private String IconName;
    private int mCondition;

    // TODO: Create a WeatherDataModel from a JSON:
        public static WeatherDataModel fromJson(JSONObject jasonobject)
        {
            try {
                WeatherDataModel weatherdata = new WeatherDataModel();
                weatherdata.mCity = jasonobject.getString("name");
                weatherdata.mCondition = jasonobject.getJSONArray("weather").getJSONObject(0).getInt("id");
                weatherdata.IconName = updateWeatherIcon(weatherdata.mCondition); //this icon name will hold the image name in the drawable

                double tempresult = jasonobject.getJSONObject("main").getDouble("temp")-273.15;
                int roundedvalue =  (int)Math.rint(tempresult);
                weatherdata.mTemperature = Integer.toString(roundedvalue);
                return weatherdata;

            }catch (JSONException e){
                e.printStackTrace();
                return null;
            }
        }

    // TODO: Uncomment to this to get the weather image name from the condition:
    private static String updateWeatherIcon(int condition) {
        //codes from openweathermap
        //the reuturn type can be used to map the image in the drawable folder
        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }

    // TODO: Create getter methods for temperature, city, and icon name:


    public String getmTemperature() {
        return mTemperature + "°";
    }

    public String getmCity() {
        return mCity;
    }

    public String getIconName() {
        return IconName;
    }
}
