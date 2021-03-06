package com.example.codegladiators;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextView signup;
    Button login;
    ProgressBar progressBar;
    EditText email,password;
    TextInputLayout email_container,password_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlueDark));
        signup = findViewById(R.id.link_signup);
        email_container = findViewById(R.id.email_container);
        password_container = findViewById(R.id.password_container);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
        progressBar = findViewById(R.id.progress_bar);


        login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = Objects.requireNonNull(email.getText()).toString();
                String passWord = Objects.requireNonNull(password.getText().toString());


                if (userName.isEmpty()) {
//                    email_container.setError("Enter the username");
                    Toast.makeText(getApplicationContext(),"Enter UserName",Toast.LENGTH_SHORT).show();
                } else if (passWord.isEmpty()) {
//                    password_container.setError("Enter the password");
                    Toast.makeText(getApplicationContext(),"Enter Password  ",Toast.LENGTH_SHORT).show();
                } else {
                    signup.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(login.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    login.setText("Logging in...");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    },3000);
                }



            }
        });
    }
}
