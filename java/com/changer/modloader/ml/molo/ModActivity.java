package com.changer.modloader.ml.molo;

import android.Manifest;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ModActivity extends AppCompatActivity {

    AssetManager assetManager = null;

    Map<String, Object> main_path = new HashMap<>();
    String dragon = "/dragon2017/";
    String assets = "/assets/";
    String parent = "Android/data/com.mobile.legends/files/";

    Map<String, Object> file_names = new HashMap<>();


    TextView hello;

    private void PathLoader() {
        main_path.put("UIPATH", dragon + assets + "/UI/android/");
        main_path.put("SCENEPATH", dragon + assets + "/Scences/android/");
        LoadNames();
    }

    private void LoadNames() {
        file_names.put("UIPATH", "Atlas_BattleGround.unity3d");
        file_names.put("SCENEPATH", "PVP_015_add.unity3d");
    }

    private void promptPermission() {
        ActivityCompat.requestPermissions(ModActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PathLoader();
        promptPermission();
        assetManager = getAssets();
        try {
            String[] files = assetManager.list("");
            for (String name : files) {
                Log.d("filesassets", name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        hello = findViewById(R.id.hello);
        hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyAssets("joystick-Dragon0001.unity3d");
            }
        });
    }

    private String checkPath(String assetName) {
        if (assetName.contains("joystick")) {
            return main_path.get("UIPATH").toString();
        } else if (assetName.contains("tournament")) {
            return main_path.get("SCENCEPATH").toString();
        } else if (assetName.contains("aniemoji")) {

        } else {

        }
        return "";
    }

    private String checkName(String assetName) {
        if (assetName.contains("joystick")) {
            return file_names.get("UIPATH").toString();
        } else if (assetName.contains("tournament")) {
            return file_names.get("SCENCEPATH").toString();
        } else if (assetName.contains("aniemoji")) {

        } else {

        }
        return "";
    }

    private void copyAssets(String assetname) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(assetname);
//                    File fileDir = getExternalFilesDir(null);
            File fileDir = new File(Environment.getExternalStorageDirectory(), parent + checkPath(assetname));

            File outFile = new File(fileDir, checkName(assetname));

            out = new FileOutputStream(outFile);
            copyFile(in, out);
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + assetname, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
        }

    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
