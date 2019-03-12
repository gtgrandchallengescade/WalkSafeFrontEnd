/**
 * The splash screen of the app.
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

import java.net.CookieHandler;
import java.net.CookieManager;

public class MainActivity extends AppCompatActivity {

    // the button that leads the user to the login page
    Button toLoginPageButton;

    // the button that leads new users to the sign in page
    Button toSignInPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CookieHandler.setDefault(new CookieManager());

        // when the sign up button is clicked, take the user to the sign up page
        toSignInPageButton = (Button) findViewById(R.id.toSignInPage);
        toSignInPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, SignUpPage.class);
                startActivity(myIntent);
            }
        });

        // when the login button is clicked, take the user to the login page
        toLoginPageButton = (Button) findViewById(R.id.toLoginPage);
        toLoginPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, LoginPage.class);
                startActivity(myIntent);
            }
        });
    }


}
