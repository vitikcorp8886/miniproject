package com.example.vitikcorp.miniproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnSignUp,btnSignIn;
    TextView txtslogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignUp=(Button)findViewById(R.id.signup);
        btnSignIn=(Button)findViewById(R.id.signin);
        txtslogan=(TextView)findViewById(R.id.textSlogan);

    btnSignUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signUp = new Intent(MainActivity.this,SignUp.class);
            startActivity(signUp);
        }
    });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent(MainActivity.this,SignIn.class);
                startActivity(signIn);
            }
        });
    }

    /*public void signnup(View view) {
    }

    public void signinn(View view) {
    }*/
}
