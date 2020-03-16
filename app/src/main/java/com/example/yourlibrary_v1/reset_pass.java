package com.example.yourlibrary_v1;

import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Objects;

public class reset_pass extends AppCompatActivity implements OnClickListener {
    private View view;

    private EditText emailId;
    private TextView submit, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Reset_pass");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        emailId = findViewById(R.id.login_emailid);
        submit = findViewById(R.id.forgot_button);
        XmlResourceParser xrp = getResources().getXml(R.xml.text_selector);

        {
            ColorStateList csl = null;
            try {
                csl = ColorStateList.createFromXml(getResources(),
                        xrp);
            } catch (IOException | XmlPullParserException ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View view) {
        System.out.println("Test");
    }

    private void initViews() {
        emailId = view.findViewById(R.id.registered_emailid);
        submit = view.findViewById(R.id.forgot_button);

    }

//        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.xml.text_selector);
//        try {
//            ColorStateList csl = ColorStateList.createFromXml(getResources(),
//                    xrp);
//
//            back.setTextColor(csl);
//            submit.setTextColor(csl);
//
//        } catch (Exception e) {
//        }
//
//    }

    // Set Listeners over buttons
//    private void setListeners() {
//        back.setOnClickListener(this);
//        submit.setOnClickListener(this);
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.backToLoginBtn:
//
//                // Replace Login Fragment on Back Presses
//                new MainActivity();
//                break;
//
//            case R.id.forgot_button:
//
//                // Call Submit button task
//                submitButtonTask();
//                break;
//
//        }
//
//    }
//
//    private void submitButtonTask() {
//        String getEmailId = emailId.getText().toString();
//
//        // Pattern for email id validation
//        Pattern p = Pattern.compile(Utils.regEx);
//
//        // Match the pattern
//        Matcher m = p.matcher(getEmailId);
//
//        // First check if email id is not null else show error toast
//        if (getEmailId.equals("") || getEmailId.length() == 0)
//
//            new CustomToast().Show_Toast(new Activity(), view,
//                    "Please enter your Email Id.");
//
//            // Check if email id is valid or not
//        else if (!m.find())
//            new CustomToast().Show_Toast(new Activity(), view,
//                    "Your Email Id is Invalid.");
//
//            // Else submit email id and fetch passwod or do your stuff
//        else
//            Toast.makeText(new Activity(), "Get Forgot Password.",
//                    Toast.LENGTH_SHORT).show();
//    }
//}
@Override
public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
}
}