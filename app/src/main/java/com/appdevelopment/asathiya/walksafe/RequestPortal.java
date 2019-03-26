/**
 * A request portal page by which users can see their incoming requests, sent requests, and group
 * status
 *
 * @author Akshay Sathiya
 */

package com.appdevelopment.asathiya.walksafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;

public class RequestPortal extends AppCompatActivity {

    // button linking to check incoming request page
    Button toIncomingRequests, toSentRequests, toCheckGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_portal);

        toIncomingRequests = findViewById(R.id.checkIncomingButton);
        toIncomingRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RequestPortal.this, CheckSentRequestPage.class);
                startActivity(myIntent);
            }
        });
        toSentRequests = findViewById(R.id.checkSentButton);
        toSentRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RequestPortal.this, CheckOutgoingRequestPage.class);
                startActivity(myIntent);
            }
        });
        toCheckGroup = findViewById(R.id.checkGroupButton);
        toCheckGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RequestPortal.this, CheckGroupPage.class);
                startActivity(myIntent);
            }
        });
    }
}
