package com.example.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    private EditText address;
    private EditText password;
    private EditText passwordCfm;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init(){
        address = (EditText)findViewById(R.id.address);
        password = (EditText)findViewById(R.id.password);
        passwordCfm = (EditText)findViewById(R.id.passwordCfm);
        confirm = (Button)findViewById(R.id.confirm);
    }
}
