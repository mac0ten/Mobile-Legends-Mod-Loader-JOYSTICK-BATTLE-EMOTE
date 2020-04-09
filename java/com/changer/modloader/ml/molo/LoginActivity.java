package com.changer.modloader.ml.molo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.changer.modloader.ml.molo.Service.BackgroundSoundService;
import com.codekidlabs.storagechooser.StorageChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView img_header;
    EditText et_email, et_password;
    Button btn_login;
    Button btn_guest;
    TextView tv_register;

    boolean toExit = false;
    String verName;

    private void initElements() {
        img_header = findViewById(R.id.img_header);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tv_register);
        btn_guest = findViewById(R.id.btn_guest);
        btn_guest.setOnClickListener(this);
    }

    private void initListeners() {
        Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        aniSlide.setInterpolator(new LinearInterpolator());
        img_header.setAnimation(aniSlide);
        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initElements();
        initListeners();
        promptPermission();
        if (shouldAskPermissions()) {
            askPermissions();
        }
//        Intent svc = new Intent(this, BackgroundSoundService.class);
//        startService(svc);
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    private void promptPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }


    @Override
    public void onBackPressed() {
        if (toExit) {
            finish();
            System.exit(0);
        }

        toExit = true;
        tool.t(this, "Press back again to exit!", 0);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                toExit = false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        boolean permissionOk = false;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
            permissionOk = true;
        } else {
            tool.t(this, "Please allow [Access to storage] to replace Unity files!", 1);
            promptPermission();
        }
        if (permissionOk) {
            Intent intent = null;
            String uType = "guest";
            Class target=null;
            switch (v.getId()) {
                case R.id.tv_register:
                    target = RegisterActivity.class;
                    break;
                case R.id.btn_guest:
                    uType = "guest";
                    target = DashboardActivity.class;
                    break;
                case R.id.btn_login:
                    if (tool.sVal(et_email).equals("admin") && tool.sVal(et_password).equals("admin")) {
                        uType = "admin";
                    }
                    target = DashboardActivity.class;
                    break;
            }
            if (uType.length() > 1 && target!=null) {
                intent = new Intent();
                intent.putExtra("uType", uType);
                tool.openXtra(this, target, intent);
            }

        }
    }
}
