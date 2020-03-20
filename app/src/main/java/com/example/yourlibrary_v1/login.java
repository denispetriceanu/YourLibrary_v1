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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourlibrary_v1.More.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {
    EditText email_id, password;
    FirebaseAuth mAuth;

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
        Button loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new login().checkValidation(getApplicationContext(), email_id.getText().toString(), password.getText().toString())) {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(email_id.getText().toString(), password.getText().toString()).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            System.out.println("Task: " + task.toString());
                            if (task.isSuccessful()) {
                                startActivity(new Intent(login.this, MainActivity.class));
                                DynamicToast.makeSuccess(getApplicationContext(), "You are login with success!", Toast.LENGTH_LONG).show();
                            } else {
                                DynamicToast.makeError(getApplicationContext(), "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
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

    // Check Validation before login
    private boolean checkValidation(Context context, String email, String pass) {
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(email);

        if (email.equals("") || email.length() == 0
                || pass.equals("") || pass.length() == 0) {
            DynamicToast.makeError(context, "Enter both .").show();
            return false;

        } else if (!m.find()) {
            DynamicToast.makeError(context, "Your Email is Invalid.").show();
            return false;
        } else
            return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
