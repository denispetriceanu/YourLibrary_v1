package com.example.yourlibrary_v1;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yourlibrary_v1.More.CustomToast;
import com.example.yourlibrary_v1.More.Utils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class reset_pass extends AppCompatActivity {
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
                submitButtonTask(view, getApplicationContext());
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

    private void submitButtonTask(View view, Context context) {
        String getEmailId = emailId.getText().toString();

        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.regEx);

        // Match the pattern
        Matcher m = p.matcher(getEmailId);

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0)
            new CustomToast().Show_Toast(context, view,
                    "Please enter your Email Id.");
            // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(context, view,
                    "Your Email Id is Invalid.");
        else
            Toast.makeText(context, "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}