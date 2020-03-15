package com.example.yourlibrary_v1;

import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Login extends AppCompatActivity implements OnClickListener {
    private View view;
    private EditText emailid, password;
    private Button loginButton;
    private TextView forgotPassword, signUp;
    private CheckBox show_hide_password;

    //    private static LinearLayout loginLayout;
//    private static Animation shakeAnimation;
//    private static Activity activityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bundle bundle = getIntent().getExtras();
        forgotPassword = findViewById(R.id.forgot_password);
        signUp = findViewById(R.id.createAccount);
        show_hide_password = findViewById(R.id.show_hide_password);
        XmlResourceParser xrp = getResources().getXml(R.xml.text_selector);

        {
            ColorStateList csl = null;
            try {
                csl = ColorStateList.createFromXml(getResources(),
                        xrp);
            } catch (IOException | XmlPullParserException ex) {
                ex.printStackTrace();
            }

            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        }
    }

    @Override
    public void onClick(View view) {
        System.out.println("Test");
    }

    // Setting text selector over textviews


//    public static void setShakeAnimation(Animation shakeAnimation) {
//        Login.shakeAnimation = shakeAnimation;
//        shakeAnimation= AnimationUtils.loadAnimation(R.anim.shake);
//    }

    private void initViews() {

        emailid = view.findViewById(R.id.login_emailid);
        password = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.loginBtn);
        forgotPassword = view.findViewById(R.id.forgot_password);
        signUp = view.findViewById(R.id.createAccount);
        show_hide_password = view
                .findViewById(R.id.show_hide_password);
//        loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);
    }

//    // Set Listeners
//    private void setListeners() {
//        loginButton.setOnClickListener(this);
//        forgotPassword.setOnClickListener(this);
//        signUp.setOnClickListener(this);
//
//        show_hide_password
//                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//                    @Override
//                    public void onCheckedChanged(CompoundButton button,
//                                                 boolean isChecked) {
//
//                        if (isChecked) {
//
//                            show_hide_password.setText(R.string.hide_pwd);// change
//
//                            password.setInputType(InputType.TYPE_CLASS_TEXT);
//                            password.setTransformationMethod(HideReturnsTransformationMethod
//                                    .getInstance());// show password
//                        } else {
//                            show_hide_password.setText(R.string.show_pwd);// change
//
//                            password.setInputType(InputType.TYPE_CLASS_TEXT
//                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                            password.setTransformationMethod(PasswordTransformationMethod
//                                    .getInstance());
//                        }
//                    }
//                });
//    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onClick(View v) {
//        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
//        switch (v.getId()) {
//            case R.id.loginBtn:
//                Intent intent = new Intent(this, Login.class);
//                startActivity(intent);
//                break;
//
//            case R.id.forgot_password:
////                activityManager
////                .replace(R.id.frameContainer, new SignUp(), Utils.signUp_activity).commit();
//                break;
//            case R.id.createAccount:
////                activityManager
////                .replace(R.id.frameContainer, new SignUp(), Utils.signUp_activity).commit();
//                break;
//        }
//
//    }

//    // Check Validation before login
//    private void checkValidation() {
//        String getEmailId = emailid.getText().toString();
//        String getPassword = password.getText().toString();
//
//        Pattern p = Pattern.compile(Utils.regEx);
//        Matcher m = p.matcher(getEmailId);
//
//        if (getEmailId.equals("") || getEmailId.length() == 0
//                || getPassword.equals("") || getPassword.length() == 0) {
//            loginLayout.startAnimation(shakeAnimation);
//            new CustomToast().Show_Toast(new Activity(), view,
//                    "Enter both .");
//
//        } else if (!m.find())
//            new CustomToast().Show_Toast(new Activity(), view,
//                    "Your Email is Invalid.");
//        else
//            Toast.makeText(new Activity(), " Login.", Toast.LENGTH_SHORT)
//                    .show();
//
//    }
}
