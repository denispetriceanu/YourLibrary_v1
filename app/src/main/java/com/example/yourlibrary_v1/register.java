package com.example.yourlibrary_v1;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourlibrary_v1.More.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity implements View.OnClickListener {
    private EditText fullName, emailId, mobileNumber, location, password, confirmPassword;
    private CheckBox terms_conditions;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // set title
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init variables
        fullName = findViewById(R.id.fullName);
        emailId = findViewById(R.id.userEmailId);
        mobileNumber = findViewById(R.id.mobileNumber);
        location = findViewById(R.id.location);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        terms_conditions = findViewById(R.id.terms_conditions);

        Button signUpButton = findViewById(R.id.signUpBtn);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation(view, getApplicationContext());
            }
        });


        XmlResourceParser xrp = getResources().getXml(R.xml.text_selector);
        {
            ColorStateList csl = null;
            try {
                csl = ColorStateList.createFromXml(getResources(),
                        xrp);
            } catch (IOException | XmlPullParserException ex) {
                ex.printStackTrace();
            }
            // set special color
            signUpButton.setTextColor(csl);
        }

    }

    @Override
    public void onClick(View view) {
        System.out.println("Test");
    }

    // Check Validation Method
    private void checkValidation(View view, Context context) {

        // Get all edit text texts
        String terms = terms_conditions.getText().toString();
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getLocation = location.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);
        if (password.length() > 6)
            createAccount(getEmailId, getPassword);
        else
            Toast.makeText(getBaseContext(), "Your password is to short", Toast.LENGTH_LONG).show();

        // Check if all strings are null or not
//        if (getFullName.equals("") || getFullName.length() == 0
//                || getEmailId.equals("") || getEmailId.length() == 0
//                || getMobileNumber.equals("") || getMobileNumber.length() == 0
//                || getLocation.equals("") || getLocation.length() == 0
//                || getPassword.equals("") || getPassword.length() == 0
//                || getConfirmPassword.equals("") || getConfirmPassword.length() == 0
//                || terms.length() == 0)
//
//            new CustomToast().Show_Toast(context, view,
//                    "All fields are required.");
//            // Check if email id valid or not
//        else if (!m.find())
//            new CustomToast().Show_Toast(context, view,
//                    "Your Email Id is Invalid.");
//            // Check if both password should be equal
//        else if (!getConfirmPassword.equals(getPassword))
//            new CustomToast().Show_Toast(context, view,
//                    "Both password doesn't match.");
//            // Make sure user should check Terms and Conditions checkbox
//        else if (!terms_conditions.isChecked())
//            new CustomToast().Show_Toast(context, view,
//                    "Please select Terms and Conditions.");
//            // Else do sign up or do your stuff
//        else {
//            createAccount(getEmailId, getPassword);
////            Toast.makeText(context, "Do SignUp.", Toast.LENGTH_SHORT)
////                    .show();
//        }

        // here in this else we must call the request to firebase for create user
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void createAccount(String email, String pass) {
        System.out.println(email + " " + pass);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        System.out.println(currentUser);
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            new Utils().updateUI(user, getBaseContext());
                            startActivity(new Intent(register.this, login.class));
                        } else {
                            System.out.println(task.toString());
                            Toast.makeText(getBaseContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}