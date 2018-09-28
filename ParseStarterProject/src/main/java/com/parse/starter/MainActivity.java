/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

    boolean login = true;
    Button loginButton;
    TextView loginOrSignUp;
    EditText username;
    EditText password;

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            loginOrSingUp(view);
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Instagram");

        if (ParseUser.getCurrentUser() != null) {
            autoLogIn();
        }

        loginButton = (Button) findViewById(R.id.singUpButton);
        loginOrSignUp = (TextView) findViewById(R.id.changeTextView);
        username = (EditText) findViewById(R.id.nameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        username.setOnKeyListener(this);
        password.setOnKeyListener(this);

        ImageView logoImageView = (ImageView) findViewById(R.id.logoImageView);
        ConstraintLayout backgroundLayout = (ConstraintLayout) findViewById(R.id.backgroudLayout);

        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };

        logoImageView.setOnClickListener(click);
        backgroundLayout.setOnClickListener(click);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void autoLogIn() {
        Intent intent = new Intent(MainActivity.this, ListOfUsers.class);
        startActivity(intent);
    }

    public void loginOrSingUp(View v) {

        username = (EditText) findViewById(R.id.nameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);

        if (username.getText().length() == 0 || password.getText().length() == 0) {
            Toast.makeText(this, "Username or Password empty", Toast.LENGTH_SHORT).show();
        } else {
            if (login) {
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null && user != null) {
                            autoLogIn();
                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, username.getText().toString() + " Registration Complete", Toast.LENGTH_SHORT).show();
                            autoLogIn();
                        } else {
                            Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    public void changeOption(View v) {
        if (login) {
            login = false;
            loginButton.setText("SIGN UP");
            loginOrSignUp.setText("or, login");
        } else {
            login = true;
            loginButton.setText("LOGIN");
            loginOrSignUp.setText("or, sign up");
        }
    }

}