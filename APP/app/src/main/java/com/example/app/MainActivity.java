package com.example.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText password;
    private EditText address;
    private Button signup;
    private Button login;

    private Intent intent;

    private Intent intentLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        intent = new Intent(MainActivity.this, SignUpActivity.class);
        intentLogin = new Intent(MainActivity.this, ChooseDateActivity.class);

        login.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    private void init(){
        password = (EditText)findViewById(R.id.password);
        address = (EditText)findViewById(R.id.address);
        signup = (Button)findViewById(R.id.signup);
        login = (Button)findViewById(R.id.login);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login:
                startActivity(intentLogin);

                break;
            case R.id.signup:
                startActivity(intent);
                break;
        }
    }
}
