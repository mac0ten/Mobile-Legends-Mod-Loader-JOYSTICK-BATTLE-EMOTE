package com.changer.modloader.ml.molo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;


public class tool {
    public static void t(Context context, String msg, int duration) {
        Toast.makeText(context, msg, (duration == 1) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static String sVal(EditText editText) {
        return editText.getText().toString();
    }

    public static void picChange(Context context, ImageView imageView, String url) {
        if (url.length() > 5) {
            Picasso.get()
                    .load(url)
                    .resize(1080, 720)
                    .centerCrop()
                    .placeholder(R.drawable.loadinganim)
                    .error(R.drawable.empty)
                    .into(imageView);
        } else imageView.setImageResource(R.drawable.empty);
    }

    public static String[] lastDigits(String userid) {
        if (userid.length() > 4) {
            int len = userid.length();
            return new String[]
                    {
                            userid.substring(len - 2, len),
                            userid.substring(len - 4, len - 2)
                    };
        } else return null;
    }

    public static void eyeMod(Button upload,boolean eye){
        if(upload!=null){
            upload.setVisibility((eye)?View.VISIBLE:View.GONE);
        }
    }

    public static void open(Context context, Class target) {
        context.startActivity(new Intent(context, target)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public static void openXtra(Context context, Class target, Intent extra) {
        context.startActivity(new Intent(context, target)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtras(extra)
        );
    }

    public static Modal modal(Context context, int layout, boolean cancelable) {
        AlertDialog.Builder modal = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(layout, null);
        modal.setView(view);
        modal.setCancelable(cancelable);
        AlertDialog show = modal.show();
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return new Modal(view, show);
    }

    public static class Modal {
        View view;
        AlertDialog show;

        public Modal(View view, AlertDialog modal) {
            this.view = view;
            this.show = modal;
        }
    }

    public static File phoneDetect(Context context) {
        File[] files = Environment.getExternalStorageDirectory().listFiles();
        boolean androidExists = false;
        boolean dataExists = false;
        boolean mlExists = false;
        if (files != null) {
            if (files.length > 0) {
                for (File file : files) {
                    if (file.getName().toLowerCase().equals("android")) {
                        androidExists = true;
                        File androidDir = new File(file.getPath());
                        File[] androidFiles = androidDir.listFiles();
                        if (androidFiles.length > 0) {
                            for (File aFile : androidFiles) {
                                if (aFile.getName().toLowerCase().equals("data")) {
                                    dataExists = true;
                                    File dataDir = new File(aFile.getPath());
                                    File[] dataFiles = dataDir.listFiles();
                                    for (File dFile : dataFiles) {
                                        if (dFile.getName().toLowerCase().contains("com.mobile.legends")) {
                                            mlExists = true;
                                            File MLdirectory = dFile;
                                            tool.setMLDefaulPath(context, MLdirectory.getPath());
//                                                THIS IS THE DETECTED ML FILE DIRECTORY
                                            return dFile;
                                        }
                                    }
                                }
                            }
                        } else {
                            tool.t(context, "Unable to find in the Phone Storage try the SD Card!", 1);
                            return null;
                        }
                    }

                }
                if (!androidExists) tool.t(context, "Android folder not found!", 0);
                if (!dataExists) tool.t(context, "Data folder not found!", 0);
                if (!mlExists) tool.t(context, "MobileLegends folder not found!", 0);
            } else tool.t(context, "Can't access Phone Storage Directory!", 1);
        } else tool.t(context, "Can't access Phone Storage Directory!", 1);
        return null;
    }


    public static void setMLDefaulPath(Context context, String path) {
        SharedPreferences mPrefs = context.getSharedPreferences("mldefaultpath", 0);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("mlpath", path);
        editor.commit();
    }

    public static String getMLDefaultPath(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("mldefaultpath", 0);
        if (mPrefs != null) {
            String str = mPrefs.getString("mlpath", "");
            if (str != "") {
                return str;
            } else return null;
        } else return null;
    }

    public static String versionCheck(Context context) {
        try {
            PackageInfo appInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = appInfo.versionName;
            Log.d("versionname", version);
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            tool.t(context, "Unable to get app version!", 0);
            return null;
        }
    }

    //FOR JOYSTICK
    public static void setSharedPref(Context context, String name, String value, int target) {
        String tarSpref = "";
        switch (target) {
            case 1:
                tarSpref = "mlmymodsjoystick";
                break;
            case 2:
                tarSpref = "mlmymodsaniemoji";
                break;
            case 3:
                tarSpref = "mlmymodstouretwarning";
                break;
        }
        SharedPreferences mPrefs = context.getSharedPreferences(tarSpref, 0);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getSharedPref(Context context, String name, int target) {
        try {
            String tarSpref = "";
            switch (target) {
                case 1:
                    tarSpref = "mlmymodsjoystick";
                    break;
                case 2:
                    tarSpref = "mlmymodsaniemoji";
                    break;
                case 3:
                    tarSpref = "mlmymodstouretwarning";
                    break;
            }
            SharedPreferences mPrefs = context.getSharedPreferences(tarSpref, 0);
            if (mPrefs != null) {
                if (mPrefs.getAll().size() > 0) {
                    if (mPrefs.contains(name)) {
                        String str = mPrefs.getString(name, "");
                        if (str != "") {
                            return str;
                        } else return null;
                    } else {
                        return null;
                    }
                } else return null;
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }


    public static void clearSharePrefs(Context context, String prefName) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(prefName, 0);
            preferences.edit().clear().commit();
        } catch (Exception e) {

        }
    }

    //    FILE HANDLING
    static String getMLAssetName(String assetName, boolean wholepath, boolean pathonly) {
        String afterPackageName = "/files/dragon2017/assets/";
        String joystickAssetFolder = "/UI/android/";
        String tournamentAssetFolder = "/Scenes/android/";
        String aniemojiAssetFolder = "/Art/android/";
        if (assetName.contains("joystick")) {
            return ((wholepath) ? afterPackageName + joystickAssetFolder : "") + ((pathonly) ? "" : "Atlas_BattleGround.unity3d");
        } else if (assetName.contains("tournament")) {
            return ((wholepath) ? afterPackageName + tournamentAssetFolder : "") + ((pathonly) ? "" : "PVP_015_add.unity3d");
        } else if (assetName.contains("aniemoji")) {
            return ((wholepath) ? afterPackageName + aniemojiAssetFolder : "") + ((pathonly) ? "" : "AniEmoji_eff_40200_add.unity3d");
        } else {

        }
        return "";
    }

//    static void copyAssets(Context context, String assetname, String filePathToMod) {
//        InputStream in = null;
//        OutputStream out = null;
//        AssetManager assetManager = context.getAssets();
//        try {
//            in = assetManager.open(assetname);
////                    File fileDir = getExternalFilesDir(null);
//            File fileDir = new File(filePathToMod);
//
//            File outFile = new File(fileDir, getMLAssetName(assetname,false,false));
//
//            out = new FileOutputStream(outFile);
//            copyFile(context,in, out);
//        } catch (IOException e) {
//            tool.t(context, e.getMessage(), 0);
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    tool.t(context,e.getMessage(),1);
//                }
//            }
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    tool.t(context,e.getMessage(),1);
//                }
//            }
//        }
//
//    } COPY ASSETS BACKUP

    static void copyAssets(Context context, String assetname, String filePathToMod) {
//        try {
//
//        } catch (IOException e) {
//            tool.t(context, e.getMessage(), 0);
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    tool.t(context,e.getMessage(),1);
//                }
//            }
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    tool.t(context,e.getMessage(),1);
//                }
//            }
//        }
        new CopyFile().execute(
                new FileCopyParams(
                        context,
                        assetname,
                        filePathToMod
                )
        );

    }


    public static class FileCopyParams {
        Context context;
        String assetname;
        String filePathToMod;

        public void setContext(Context context) {
            this.context = context;
        }

        public String getAssetname() {
            return assetname;
        }

        public void setAssetname(String assetname) {
            this.assetname = assetname;
        }

        public String getFilePathToMod() {
            return filePathToMod;
        }

        public void setFilePathToMod(String filePathToMod) {
            this.filePathToMod = filePathToMod;
        }

        public FileCopyParams(Context context, String assetname, String filePathToMod) {

            this.context = context;
            this.assetname = assetname;
            this.filePathToMod = filePathToMod;
        }

        public Context getContext() {
            return context;
        }

    }

    private static class CopyFile extends AsyncTask<FileCopyParams, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("loaderlog", "loading started");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d("loaderlog", "loading done");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            Log.d("loaderlog", "loading " + values[0]);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Integer doInBackground(FileCopyParams... fileCopyParams) {

            FileCopyParams params = fileCopyParams[0];
            InputStream in = null;
            OutputStream out = null;
            AssetManager assetManager = params.getContext().getAssets();
            Log.e("TESTINGRODEL", params.getFilePathToMod());


            try {
//                Log.e("test", "for phone" + tool.getMLDefaultPath(params.getContext()));
//                in = assetManager.open(params.getAssetname());
////                    File fileDir = getExternalFilesDir(null);
//                File fileDir = new File(params.getFilePathToMod());
//                Log.e("testingrodel", params.getAssetname());
//                File outFile = new File(fileDir, getMLAssetName(params.getAssetname(), false, false));
//                out = new FileOutputStream(outFile);
//                int read;
//                byte[] buffer = new byte[1024];
//                while ((read = in.read(buffer)) != -1) {
//                    out.write(buffer, 0, read);
//                }

                Log.e("test", "for phone" + tool.getMLDefaultPath(params.getContext()));
                in = assetManager.open(params.getAssetname());
                File fileDir = new File(params.getFilePathToMod());
                Log.e("testingrodel", params.getAssetname());
                File outFile = new File(fileDir, getMLAssetName(params.getAssetname(), false, false));
                out = new FileOutputStream(outFile);
                int read;
                byte[] buffer = new byte[1024];
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("test", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("test", e.getMessage());
            } catch (Exception e) {
                tool.t(params.getContext(), e.getMessage(), 0);
            }

            return null;
        }
    }


    static ArrayList<String> assetFiles(Context context) {
        AssetManager assetManager = context.getAssets();
        ArrayList<String> assetFiles = new ArrayList<>();
        try {
            for (String assetName : assetManager.list("")) {
                Log.d("assetsmen", assetName);
                if (assetName.toLowerCase().contains("unity3d")) {
                    assetFiles.add(assetName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return assetFiles;
    }
//    END

    public static void restartML(Context context, String mlPackage) {
        if (mlPackage != null) {
            ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
            am.killBackgroundProcesses(mlPackage);
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(mlPackage);
            if (launchIntent != null) {
                context.startActivity(launchIntent);
            }
        }
    }


    //FILE CHOOSER GET PATH
}

