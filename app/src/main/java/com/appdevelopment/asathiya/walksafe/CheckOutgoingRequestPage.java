/**
 * Allows the user to check their outgoing requests
 *
 * @author Connor Bradshaw
 * @version 1.1
 */

package com.appdevelopment.asathiya.walksafe;

import android.content.Intent;
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

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckOutgoingRequestPage extends AppCompatActivity {
    RequestQueue requestQueue;
    Button nextButton, homeButton;

    TextView userSent, statusSent;
    ArrayList<String> status = new ArrayList<String>();
    ArrayList<Integer> userId = new ArrayList<Integer>();
    int numOfRequest = 0;
    int count = 0;

    View.OnClickListener next = new View.OnClickListener() {
        public void onClick(View view) {
            count++;
            if(count < numOfRequest) {
                String userText = "" + userId.get(count);
                System.out.println(userText);
                userSent.setText(userText);
                String statusText = "" + status.get(count);
                statusSent.setText(statusText);
            } else {
                String noRequest = "N/A";
                userSent.setText(noRequest);
                String noStatus = "N/A";
                statusSent.setText(noStatus);
            }
        }
    };
    View.OnClickListener home = new View.OnClickListener() {
        public void onClick(View view) {
            Intent myIntent = new Intent(CheckOutgoingRequestPage.this, HomePage.class);
            startActivity(myIntent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_outgoing_request_page);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //CookieHandler.setDefault(new CookieManager());
        getData("https://gc-project.herokuapp.com/group/checkSentRequest");

    }
    public void getData(String requestUrl){
        try {
            String url = requestUrl;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    nextButton = (Button) findViewById(R.id.nextButton);
                    userSent = (TextView) findViewById(R.id.userIDView);
                    statusSent = (TextView) findViewById(R.id.statusView);
                    homeButton = (Button) findViewById(R.id.homeButton);

                    try {
                        numOfRequest = Integer.valueOf(response.getString("numOfRequest"));
                        System.out.println(numOfRequest);
                        if(numOfRequest != 0) {
                            for (int i = 0; i < numOfRequest; i++) {
                                String userIdName = String.format("[%d] User ID", i);
                                String reqIdName = String.format("[%d] Status", i);
                                userId.add(Integer.parseInt(response.getString(userIdName)));
                                status.add(response.getString(reqIdName));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(count < numOfRequest) {
                        String userText = "" + userId.get(count);
                        System.out.println(userText);
                        userSent.setText(userText);
                        String statusText = "" + status.get(count);
                        statusSent.setText(statusText);
                    } else {
                        String noRequest = "N/A";
                        userSent.setText(noRequest);
                        String noStatus = "N/A";
                        statusSent.setText(noStatus);
                    }
                    nextButton.setOnClickListener(next);
                    homeButton.setOnClickListener(home);
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
