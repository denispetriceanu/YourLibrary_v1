package com.example.yourlibrary_v1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yourlibrary_v1.More.CustomToast;
import com.example.yourlibrary_v1.More.Utils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity implements OnClickListener {
    EditText email_id, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize this var here because we need to use in show password and in get data
        email_id = findViewById(R.id.login_emailid);
        password = findViewById(R.id.login_password);

        password.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);

        CheckBox checkBox = findViewById(R.id.show_hide_password);
        checkBox.setChecked(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setTransformationMethod(null);
                }
            }
        });

        // open forgot pass activity
        TextView forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, reset_pass.class);
                startActivity(intent);
            }
        });

        // login button
        // TODO: here we put the code for send to the firebase if user exists in data base
        Button loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new login().checkValidation(view, getApplicationContext(), email_id.getText().toString(), password.getText().toString());
            }
        });

        // go to sign up page
        TextView signUp = findViewById(R.id.createAccount);
        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, register.class));
            }
        });

    }

    @Override
    public void onClick(View view) {
        System.out.println("Test");
    }

//    public static void setShakeAnimation(Animation shakeAnimation) {
//        Login.shakeAnimation = shakeAnimation;
//        shakeAnimation= AnimationUtils.loadAnimation(R.anim.shake);
//    }


    // Check Validation before login
    private void checkValidation(View view, Context context, String email, String pass) {

        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(email);

        if (email.equals("") || email.length() == 0
                || pass.equals("") || pass.length() == 0) {
            new CustomToast().Show_Toast(context, view,
                    "Enter both .");

        } else if (!m.find())
            new CustomToast().Show_Toast(context, view,
                    "Your Email is Invalid.");
        else
            Toast.makeText(context, "Login.", Toast.LENGTH_SHORT)
                    .show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
