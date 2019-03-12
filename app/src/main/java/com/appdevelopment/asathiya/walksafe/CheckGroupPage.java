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
import com.appdevelopment.asathiya.walksafe.HomePage;
import com.appdevelopment.asathiya.walksafe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckGroupPage extends AppCompatActivity {
    RequestQueue requestQueue;
    Button homeButton;

    TextView startAddress, endAddress, time, date, groupMembersOutput;
    String startResponse, endResponse, timeResponse, dateResponse;
    ArrayList<String> groupMembers = new ArrayList<String>();
    int numOfMembers = 0;


    View.OnClickListener home = new View.OnClickListener() {
        public void onClick(View view) {
            Intent myIntent = new Intent(com.appdevelopment.asathiya.walksafe.CheckGroupPage.this, HomePage.class);
            startActivity(myIntent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_group_page);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //CookieHandler.setDefault(new CookieManager());
        getData("https://gc-project.herokuapp.com/group/checkGroupMembers");

    }
    public void getData(String requestUrl){
        try {
            String url = requestUrl;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    homeButton = (Button) findViewById(R.id.homeButton);
                    startAddress = (TextView) findViewById(R.id.startAddress);
                    endAddress = (TextView) findViewById(R.id.endAddress);
                    time = (TextView) findViewById(R.id.time);
                    date = (TextView) findViewById(R.id.date);
                    groupMembersOutput = (TextView) findViewById(R.id.members);
                    try {
                        numOfMembers = Integer.valueOf(response.getString("numOfMembers"));
                        System.out.println(numOfMembers);
                        startResponse = response.getString("startAddress");
                        endResponse = response.getString("endAddress");
                        timeResponse = response.getString("time");
                        dateResponse = response.getString("date");
                        if(numOfMembers != 0) {
                            for (int i = 0; i < numOfMembers; i++) {
                                String groupMemberId = Integer.toString(i);
                                groupMembers.add(response.getString(groupMemberId));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(numOfMembers != 0) {
                        String inStartAddress = startResponse;
                        startAddress.setText(inStartAddress);
                        String inEndAddress = endResponse;
                        endAddress.setText(inEndAddress);
                        String inTime = timeResponse;
                        time.setText(inTime);
                        String inDate = dateResponse;
                        date.setText(inDate);
                        String members = "";
                        for(int i = 0; i < numOfMembers; i++) {
                            if(i == 0) {
                                members = members + groupMembers.get(i);
                            } else {
                                members = members + ", "  + groupMembers.get(i);
                            }
                        }
                        groupMembersOutput.setText(members);
                    } else {
                        String noStartAddress = "N/A";
                        startAddress.setText(noStartAddress);
                        String noEndAddress = "N/A";
                        endAddress.setText(noEndAddress);
                        String noTime = "N/A";
                        time.setText(noTime);
                        String noDate = "N/A";
                        date.setText(noDate);
                        String noMembers = "N/A";
                        groupMembersOutput.setText(noMembers);
                    }
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
