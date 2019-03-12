/**
 * Checks to see if the user has requested to join the group, requests can be accepted
 * or denied.
 */

package com.appdevelopment.asathiya.walksafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckSentRequestPage extends AppCompatActivity {
    RequestQueue requestQueue;
    Button acceptRequest, denyRequest, nextRequest, homeButton;

    TextView userRequest;
    ArrayList<Integer> reqId = new ArrayList<Integer>();
    ArrayList<Integer> userId = new ArrayList<Integer>();
    int numOfRequest = 0;
    int count = 0;
    View.OnClickListener accept = new View.OnClickListener() {
        public void onClick(View view) {
            System.out.println("XXX");
            decision("accept", userId.get(count), reqId.get(count));
            count++;
            if(count < numOfRequest) {
                String userText = "User ID: " + userId.get(count);
                System.out.println(userText);
                userRequest.setText(userText);
            } else {
                String noRequest = "No requests found";
                userRequest.setText(noRequest);
                homeButton.setVisibility(View.VISIBLE);
                acceptRequest.setVisibility(View.INVISIBLE);
                denyRequest.setVisibility(View.INVISIBLE);
                nextRequest.setVisibility(View.INVISIBLE);
            }
        }
    };
    View.OnClickListener deny = new View.OnClickListener() {
        public void onClick(View view) {
            decision("deny", userId.get(count), reqId.get(count));
            count++;
            if(count < numOfRequest) {
                String userText = "User ID: " + userId.get(count);
                System.out.println(userText);
                userRequest.setText(userText);
            } else {
                String noRequest = "No requests found";
                userRequest.setText(noRequest);
                homeButton.setVisibility(View.VISIBLE);
                acceptRequest.setVisibility(View.INVISIBLE);
                denyRequest.setVisibility(View.INVISIBLE);
                nextRequest.setVisibility(View.INVISIBLE);
            }
        }
    };
    View.OnClickListener next = new View.OnClickListener() {
        public void onClick(View view) {
            count++;
            if(count < numOfRequest) {
                String userText = "User ID: " + userId.get(count);
                System.out.println(userText);
                userRequest.setText(userText);
            } else {
                String noRequest = "No requests found";
                userRequest.setText(noRequest);
                homeButton.setVisibility(View.VISIBLE);
                acceptRequest.setVisibility(View.INVISIBLE);
                denyRequest.setVisibility(View.INVISIBLE);
                nextRequest.setVisibility(View.INVISIBLE);
            }
        }
    };
    View.OnClickListener home = new View.OnClickListener() {
        public void onClick(View view) {
            Intent myIntent = new Intent(CheckSentRequestPage.this, HomePage.class);
            startActivity(myIntent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_sent_request_page);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //CookieHandler.setDefault(new CookieManager());
        getData("https://gc-project.herokuapp.com/group/checkForRequest");

    }
    public void getData(String requestUrl){
        try {
            String url = requestUrl;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    acceptRequest = (Button) findViewById(R.id.requestButton);
                    denyRequest = (Button) findViewById(R.id.denyButton);
                    nextRequest = (Button) findViewById(R.id.nextButton);
                    userRequest = (TextView) findViewById(R.id.userIDView);
                    homeButton = (Button) findViewById(R.id.homeButton);
                    acceptRequest.setVisibility(View.VISIBLE);
                    denyRequest.setVisibility(View.VISIBLE);
                    nextRequest.setVisibility(View.VISIBLE);

                    try {
                        numOfRequest = Integer.valueOf(response.getString("numOfRequest"));
                        System.out.println(numOfRequest);
                        if(numOfRequest != 0) {
                            for (int i = 0; i < numOfRequest; i++) {
                                String userIdName = String.format("[%d] User ID", i);
                                String reqIdName = String.format("[%d] Request Num:", i);
                                userId.add(Integer.parseInt(response.getString(userIdName)));
                                reqId.add(Integer.parseInt(response.getString(reqIdName)));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(count < numOfRequest) {
                        String userText = "User ID: " + userId.get(count);
                        System.out.println(userText);
                        userRequest.setText(userText);
                    } else {
                        String noRequest = "No requests found";
                        userRequest.setText(noRequest);
                        homeButton.setVisibility(View.VISIBLE);
                        acceptRequest.setVisibility(View.INVISIBLE);
                        denyRequest.setVisibility(View.INVISIBLE);
                        nextRequest.setVisibility(View.INVISIBLE);
                    }
                    acceptRequest.setOnClickListener(accept);
                    denyRequest.setOnClickListener(deny);
                    nextRequest.setOnClickListener(next);
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
    public void decision(String decision, int reqId, int reqNum) {
        System.out.println("This is reqId" + reqId);
        try {
            String url = "https://gc-project.herokuapp.com/group/decideRequest";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("decision", decision);
            params.put("reqNum", reqNum);
            params.put("reqId", reqId);
            JSONObject object = new JSONObject(params);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int responseCode = response.getInt("code");
                        if (responseCode == 200) {
                            System.out.println("XXX");
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
