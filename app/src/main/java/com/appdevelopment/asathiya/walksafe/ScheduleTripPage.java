/**
 * Represents the page fo the app on which the user schedules their trip.
 *
 * @author Akshay Sathiya
 * @version 1.1
 */

package com.appdevelopment.asathiya.walksafe;

import java.io.IOException;
import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleTripPage extends AppCompatActivity {

    String userIdString;

    // represents the button that allows the user to proceed
    Button nextButton;

    // the text field that contains the street address of the destination
    EditText inputStreetAddress1;

    // the text field that contains the city of the destination
    EditText inputCity1;

    // the text field that contains the state of the destination
    EditText inputState1;

    // the text field that contains the street address of the destination
    EditText inputStreetAddress2;

    // the text field that contains the city of the destination
    EditText inputCity2;

    // the text field that contains the state of the destination
    EditText inputState2;

    // create a RequestQueue
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_trip_page);

        inputStreetAddress1 = (EditText) findViewById(R.id.inputStreetAddress);

        inputCity1 = (EditText) findViewById(R.id.inputCity);

        inputState1 = (EditText) findViewById(R.id.inputState);

        inputStreetAddress2 = (EditText) findViewById(R.id.inputStreetAddress2);

        inputCity2 = (EditText) findViewById(R.id.inputCity2);

        inputState2 = (EditText) findViewById(R.id.inputState2);

        // for user input validation
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //CookieHandler.setDefault(new CookieManager());

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String streetAddress = inputStreetAddress1.getText().toString();
                String city = inputCity1.getText().toString();
                String state = inputState1.getText().toString();

                String startAddress = streetAddress + ", " + city + ", " + state;

                String streetAddress2 = inputStreetAddress2.getText().toString();
                String city2 = inputCity2.getText().toString();
                String state2 = inputState2.getText().toString();

                String endAddress = streetAddress2 + ", " + city2 + ", " + state2;

                // get the current date
                String currentDate = LocalDate.now().toString();

                // get the current time
                String currentTime = LocalTime.now().toString();
                currentTime = currentTime.substring(0, 2) + currentTime.substring(3,5)
                    + currentTime.substring(6, 8);

                // create two new arrays to hold the lat and long values of the addresses
                double[] latLongsStart = getLatLong(startAddress, getApplicationContext());
                double[] latLongsEnd = getLatLong(endAddress, getApplicationContext());

                // provide the info
                provideInfo(latLongsStart[0], latLongsStart[1], latLongsEnd[0], latLongsEnd[1],
                        currentTime, currentDate, startAddress, endAddress);

            }
        });
    }


    public static double[] getLatLong(String strAddress, Context context) {
        Geocoder coder = new Geocoder(context);
        double[] latLong = new double[2];
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 1);
            Address location = address.get(0);
            latLong[0] = location.getLatitude();
            latLong[1] = location.getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLong;
    }


    public void provideInfo(double startLat, double startLong, double endLat, double endLong,
                            String time, String date, String startAddress, String endAddress) {
        try {
            String url = "https://gc-project.herokuapp.com/location/request";

            Map<String, Object> params = new HashMap<>();
            params.put("startAddress", startAddress);
            params.put("startLocationLat", startLat);
            params.put("startLocationLong", startLong);
            params.put("endAddress", endAddress);
            params.put("endLocationLat", endLat);
            params.put("endLocationLong", endLong);
            params.put("timeLeaving", time);
            params.put("dateOfRequest", date);
            JSONObject object = new JSONObject(params);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println("XXX");
                        int responseCode = response.getInt("code");
                        System.out.println(responseCode);
                        if (responseCode == 201) {
                            // change layout to match page
                            setContentView(R.layout.match_page);
                            Button requestButton = (Button) findViewById(R.id.requestButton);
                            Button homeButton = (Button) findViewById(R.id.homeButton);
                            TextView userID = (TextView) findViewById(R.id.matchUserID);
                            userIdString = response.getString("userIds");
                            userID.setText(userIdString);
                            requestButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    sendRequest(Integer.parseInt(userIdString));
                                }
                            });

                            homeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent myIntent = new Intent(ScheduleTripPage.this, HomePage.class);
                                    startActivity(myIntent);
                                }
                            });
                        } else if (responseCode == 202) {
                            // change layout to create new request page
                            setContentView(R.layout.new_request);

                            Button homeButton = (Button) findViewById(R.id.homeButton);
                            homeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent myIntent = new Intent(ScheduleTripPage.this, HomePage.class);
                                    startActivity(myIntent);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley Error", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(int userId) {
        try {
            String url = "https://gc-project.herokuapp.com/group/sendRequest";

            Map<String, Integer> params = new HashMap<>();
            params.put("userId", userId);
            JSONObject object = new JSONObject(params);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int responseCode = response.getInt("code");
                        if (responseCode == 200) {
                            Intent myIntent = new Intent(ScheduleTripPage.this, HomePage.class);
                            startActivity(myIntent);
                            Toast.makeText(ScheduleTripPage.this, "Request sent!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ScheduleTripPage.this, "Request failed. Try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley Error", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
