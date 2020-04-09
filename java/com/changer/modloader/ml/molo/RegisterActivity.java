package com.changer.modloader.ml.molo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class RegisterActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    EditText et_userid
            ,et_serverid
            ,et_password;
    CircleImageView img_profile;
    ImageView img_eye;
    boolean passVisible=false;
    TextView tv_signin;

    private void initElements(){
        et_userid = findViewById(R.id.et_userid);
        et_serverid = findViewById(R.id.et_serverid);
        et_password = findViewById(R.id.et_password);
        img_profile = findViewById(R.id.img_profilepic);
        img_eye = findViewById(R.id.img_eye);
        tv_signin = findViewById(R.id.tv_signin);
    }
    private void initElementsListeners(){
        et_serverid.addTextChangedListener(this);
        img_eye.setOnClickListener(this);
        tv_signin.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        initElements();
        initElementsListeners();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
            String userid = tool.sVal(et_userid);


            if(userid.length()>4 && s.length()>3){
                String [] nums = tool.lastDigits(userid);
                String picUrl = "http://face.yuanzhanapp.com/"+s.toString()+"/"+nums[0]+"/"+nums[1]+"/"+userid+"_1.jpg";
                tool.picChange(this,img_profile,picUrl);
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_eye:
                ImageView img = ((ImageView)v);
                int color;
                if(passVisible){
                    passVisible=false;
                    color = getResources().getColor(R.color.colorOrange);

                }else{
                    passVisible=true;
                    color = getResources().getColor(R.color.colorGreen);
                }
                et_password.setTransformationMethod((passVisible)?null:PasswordTransformationMethod.getInstance());
                ImageViewCompat.setImageTintList(img, ColorStateList.valueOf(color));
                break;
            case R.id.tv_signin:
                tool.open(this,LoginActivity.class);
                break;
        }
    }
}
