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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.yourlibrary_v1.More.CustomToast;
import com.example.yourlibrary_v1.More.Utils;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity implements View.OnClickListener {
    private  View view;
    private  EditText fullName, emailId, mobileNumber, location,
            password, confirmPassword;
    private TextView login;
    private  Button signUpButton;
    private  CheckBox terms_conditions;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Bundle bundle = getIntent().getExtras();
        login = findViewById(R.id.loginBtn);
        signUpButton = findViewById(R.id.signUpBtn);
        terms_conditions = findViewById(R.id.terms_conditions);
        XmlResourceParser xrp = getResources().getXml(R.xml.text_selector);

        {
            ColorStateList csl = null;
//            try {
//                csl = ColorStateList.createFromXml(getResources(),
//                        xrp);
//            } catch (IOException | XmlPullParserException ex) {
//                ex.printStackTrace();
//            }

        }
    }

    @Override
    public void onClick(View view) {
        System.out.println("Test");
    }


    private void initViews() {
        fullName = view.findViewById(R.id.fullName);
        emailId = view.findViewById(R.id.userEmailId);
        mobileNumber = view.findViewById(R.id.mobileNumber);
        location = view.findViewById(R.id.location);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        signUpButton = view.findViewById(R.id.signUpBtn);
        login = view.findViewById(R.id.already_user);
        terms_conditions = view.findViewById(R.id.terms_conditions);
    }


//        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.xml.text_selector);
//        try {
//            ColorStateList csl = ColorStateList.createFromXml(getResources(),
//                    xrp);
//
//            login.setTextColor(csl);
//            terms_conditions.setTextColor(csl);
//        } catch (Exception e) {
//        }
//    }
//
//    private void setListeners() {
//        signUpButton.setOnClickListener(this);
//        login.setOnClickListener(this);
//    }
//}


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.signUpBtn:
//
//                // Call checkValidation method
//                checkValidation();
//                break;
//
//            case R.id.already_user:
//
//                // Replace login fragment
//                new MainActivity();
//                break;
//        }
//
//    }
//
//    // Check Validation Method
    private void checkValidation(View view, Context context, String FullName,String email, String pass,String mobilenumber) {

        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getLocation = location.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(email);
//
//        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0)

            new CustomToast().Show_Toast(context, view,
                    "All fields are required.");

            // Check if email id valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(context, view,
                    "Your Email Id is Invalid.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            new CustomToast().Show_Toast(context, view,
                    "Both password doesn't match.");

            // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked())
            new CustomToast().Show_Toast(context, view,
                    "Please select Terms and Conditions.");

            // Else do signup or do your stuff
        else
            Toast.makeText(context, "Do SignUp.", Toast.LENGTH_SHORT)
                    .show();

    }
@Override
public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
}
}