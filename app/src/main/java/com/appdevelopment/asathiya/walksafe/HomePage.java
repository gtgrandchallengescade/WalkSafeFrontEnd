/**
 * The home page of the app.
 *
 * @author Akshay Sathiya
 * @version 1.1
 */
package com.appdevelopment.asathiya.walksafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class HomePage extends AppCompatActivity {

    // represents the image button that takes the user to their profile page
    ImageButton profileButton;

    // represents the image button that takes the user to their groups page
    ImageButton groupsButton;

    // represents the image button that takes the user to their history page
    ImageButton historyButton;

    // represents the image button that takes the user to their trip-scheduling page
    ImageButton tripScheduleButton;

    // represents the log out button that takes the user back to the splash screen
    Button logOutButton;

    // request queue
    RequestQueue requestQueue;

    @Override
    public void onBackPressed() {
        //do nothing
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // for user input validation
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        profileButton = (ImageButton) findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomePage.this, ProfilePage.class);
                startActivity(myIntent);
            }
        });

        groupsButton = (ImageButton) findViewById(R.id.groupsButton);
        groupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomePage.this, GroupsPage.class);
                startActivity(myIntent);
            }
        });

        historyButton = (ImageButton) findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomePage.this, RequestPortal.class);
                startActivity(myIntent);
            }
        });

        groupsButton = (ImageButton) findViewById(R.id.groupsButton);
        groupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomePage.this, GroupsPage.class);
                startActivity(myIntent);
            }
        });

        tripScheduleButton = (ImageButton) findViewById(R.id.tripButton);
        tripScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomePage.this, ScheduleTripPage.class);
                startActivity(myIntent);
            }
        });

        logOutButton = (Button) findViewById(R.id.homeButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut("https://gc-project.herokuapp.com/user/logout");
            }
        });
    }

    public void logOut(String requestUrl){
        try {
            String url = requestUrl;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Intent myIntent = new Intent(HomePage.this, MainActivity.class);
                    startActivity(myIntent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
