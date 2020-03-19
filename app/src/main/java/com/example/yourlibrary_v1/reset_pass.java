package com.example.yourlibrary_v1;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourlibrary_v1.More.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class reset_pass extends AppCompatActivity {
    FirebaseAuth mAuth;
    private EditText emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Reset password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emailId = findViewById(R.id.reset_email);
        Button submit = findViewById(R.id.forgot_button);
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitButtonTask(getApplicationContext())) {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.sendPasswordResetEmail(emailId.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(reset_pass.this, login.class);
                                startActivity(intent);
                                DynamicToast.makeSuccess(getApplicationContext(), "Your password will be reset", Toast.LENGTH_LONG).show();
                            } else {
                                DynamicToast.makeError(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
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
            submit.setTextColor(csl);
        }
    }

    private boolean submitButtonTask(Context context) {
        String getEmailId = emailId.getText().toString();
        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.regEx);
        // Match the pattern
        Matcher m = p.matcher(getEmailId);

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0) {
            DynamicToast.makeError(context, "Please enter your Email.").show();
            return false;
        }
        // Check if email id is valid or not
        else if (!m.find()) {
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