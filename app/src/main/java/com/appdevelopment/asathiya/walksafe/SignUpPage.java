/**
 * The page of the app on which new users can sign up.
 *
 * @author Akshay Sathiya
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
import java.util.HashMap;
import java.util.Map;

public class SignUpPage extends AppCompatActivity {

    // this button processes the username and password input
    Button signUpButton;

    // this is the email text field
    EditText inputEmail;

    // this is the password text field
    EditText inputPassword;

    // for user input validation
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        // clear the email text field
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputEmail.setText("");

        // clear the password text field
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputPassword.setText("");

        // for user input validation
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //CookieHandler.setDefault(new CookieManager());

        // When the signup button is clicked, validate input data
        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain the email
                inputEmail = (EditText) findViewById(R.id.inputEmail);
                String email = inputEmail.getText().toString();

                // Obtain the password
                inputPassword = (EditText) findViewById(R.id.inputPassword);
                String password = inputPassword.getText().toString();

                // validate and process the user input
                signUp(email, password);
            }
        });
    }

    public void signUp(String email, String password) {
        try {
            String url = "https://gc-project.herokuapp.com/user/register";

            Map<String, String> params = new HashMap<String, String>();
            params.put("email", email);
            params.put("password", password);
            JSONObject object = new JSONObject(params);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int responseCode = response.getInt("code");
                        if (responseCode == 200) {
                            Intent myIntent = new Intent(SignUpPage.this, HomePage.class);
                            startActivity(myIntent);
                            Toast.makeText(SignUpPage.this, "Welcome to WalkSafe!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpPage.this, "Invalid credentials, try again.",
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
