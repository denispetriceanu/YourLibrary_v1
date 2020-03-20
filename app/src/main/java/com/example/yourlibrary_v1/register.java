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

import com.example.yourlibrary_v1.More.User_model;
import com.example.yourlibrary_v1.More.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
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
                checkValidation(getApplicationContext());
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

    // Check Validation Method
    private void checkValidation(Context context) {

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

        //Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("") || getConfirmPassword.length() == 0
                || terms.length() == 0)
            DynamicToast.makeError(context, "All fields are required.").show();
//            new CustomToast().Show_Toast(context, view,
//                    "All fields are required.");
            // Check if email id valid or not
        else if (!m.find())
            DynamicToast.makeError(context,
                    "Your Email Id is Invalid.").show();
            // Check if both password should be equal
        else if (getPassword.length() <= 6)
            DynamicToast.makeError(context, "Your password is to short.").show();
        else if (!getConfirmPassword.equals(getPassword))
            DynamicToast.makeError(context, "Both password doesn't match.").show();
            // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked())
            DynamicToast.makeError(context, "Please select Terms and Conditions.").show();
            // Else do sign up or do your stuff
        else {
            createAccount(getEmailId, getPassword);
        }
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

                            assert user != null;
                            writeUserDetails(user.getUid());

                            startActivity(new Intent(register.this, login.class));
                            DynamicToast.makeSuccess(getApplicationContext(), "Account created with success!", Toast.LENGTH_LONG).show();

                            // set al text null
                            reset_form();
                        } else {
                            System.out.println(task.toString());
                            DynamicToast.makeError(getBaseContext(), Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void writeUserDetails(String id_user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users");
        myRef.child(id_user).setValue(new User_model(fullName.getText().toString(), emailId.getText().toString(),
                mobileNumber.getText().toString(), location.getText().toString()));
    }

    private void reset_form() {
        fullName.setText("");
        emailId.setText("");
        location.setText("");
        mobileNumber.setText("");
        password.setText("");
        confirmPassword.setText("");
        terms_conditions.setChecked(false);
    }
}