package com.changer.modloader.ml.molo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.changer.modloader.ml.molo.Adapter.DashboardAdapter;
import com.changer.modloader.ml.molo.Objects.DashboardItem;
import com.changer.modloader.ml.molo.Service.BackgroundSoundService;
import com.codekidlabs.storagechooser.StorageChooser;
import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements DashboardAdapter.OnItemClickListener, View.OnClickListener, Response.ErrorListener, Response.Listener<String> {

    RecyclerView dashboard_list;
    DashboardAdapter adapter;
    ImageView img_parallax;
    ArrayList<DashboardItem> listItems = new ArrayList<>();
    FloatingActionButton fab_targetdirectory;
    FloatingActionButton fab_exitapp;
    FloatingActionButton fab_logout;
    FloatingActionButton fab_musiconoff;
    CardView wallet_container;
    CircleImageView img_userpic;
    boolean guest = false;
    String verName;
    String uType="guest";
    final int SDCARD_REQUEST_PERMISSION=1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();

        assetsDefaultSetter();

        latestVersion();
        initElements();
        initListeners();



        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            if (intent.getExtras().containsKey("uType")) {
                uType = intent.getExtras().get("uType").toString();
                tool.t(this,uType,0);
                if (uType.toLowerCase().equals("guest")) {
                    guest = true;
                }
            } else {
                tool.t(this, "Please login as user/guest!", 0);
                finish();
            }
        } else {
            tool.t(this, "Please login as user/guest!", 0);
            finish();
        }
        if (guest) {
            wallet_container.setVisibility(View.INVISIBLE);
            img_userpic.setImageDrawable(getResources().getDrawable(R.drawable.moloicon));

        } else wallet_container.setVisibility(View.VISIBLE);


    }

    private void latestVersion() {
        String path = tool.getMLDefaultPath(this);
        if (path != null) {
            tool.t(this, "Path is ready!", 0);
        } else tool.t(this, "Default ML Path is empty!", 0);

        verName = tool.versionCheck(this);
        if (verName != null) {
            //VERSION CHECKING
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://pastebin.com/raw/fT2pEbDb";
            Log.d("packagenamelog", this.getPackageName());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            stringRequest.setShouldCache(false);
            queue.add(stringRequest);
        } else finish();
    }

    @Override
    public void onBackPressed() {

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK)
//            return;
//        else {
//            Uri treeUri = data.getData();
//            DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri);
////
////            if(treeUri.getPath().split(":").length>0){
////               String split = treeUri.getPath().split(":")[1];
////                SharedPreferences mPrefs = this.getSharedPreferences("mlforsd", 0);
////                SharedPreferences.Editor editor = mPrefs.edit();
////                editor.putString("documentfile", new Gson().toJson(treeUri.toString()));
////                tool.setMLDefaulPath(this,Environment.getExternalStorageDirectory()+"/"+split);
////                editor.commit();
////            }
//
//            FileOutputStream fos = null;
//            grantUriPermission(getPackageName(), treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            }
//
//            Log.e("test",treeUri.getPath());
//            copyFileToSd("/storage/F220-0BEB/Android/data/com.mobile.legends/files/dragon2017/assets//UI/android/","Atlas_BattleGround.unity3d",pickedDir);
////            Log.e("test",test);
//
//        }
//    }
//
//    public void copyFileToSd(String inputPath, String inputFile,DocumentFile pickedDir) {
//
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//
//            in = new FileInputStream(inputPath + inputFile);
//
//            DocumentFile finalFile = pickedDir.findFile("files");
//            if(finalFile==null){
//                finalFile.createDirectory("files");
//            }else{
//                finalFile = pickedDir;
//            }
//            finalFile.createFile("//MIME type", "/files/tae.txt");
//
//            out = getContentResolver().openOutputStream(finalFile.getUri());
//
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = in.read(buffer)) != -1) {
//                out.write(buffer, 0, read);
//            }
//            in.close();
//
//
//            // write the output file (You have now copied the file)
//            out.flush();
//            out.close();
//        } catch (FileNotFoundException fnfe1) {
//            /* I get the error here */
//            Log.e("tag", fnfe1.getMessage());
//        } catch (Exception e) {
//            Log.e("tag", e.getMessage());
//        }
//    }

    @Override
    public void onResponse(String response) {
        Log.d("pastebin", response + " " + getPackageName() + ":" + verName);
        if ((getPackageName() + ":" + verName).equals(response)) {

        } else {
            tool.t(this, "App version is old/package is not authentic! [Pleae download the latest version]", 1);
            finish();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        tool.t(this, "Please request for a new version [mobilelegendsvoice@gmail.com]", 1);
        finish();
    }


    private void assetsDefaultSetter() {
        AssetManager assetManager = getAssets();
        if (tool.getSharedPref(this, "mlmymodsjoystick", 1) == null) {
            try {
                for (String name : assetManager.list("")) {
                    if (name.toLowerCase().contains("default") && name.toLowerCase().contains("joystick-")) {
                        tool.setSharedPref(this, name.toLowerCase(), "default", 1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initElements() {
        img_userpic = findViewById(R.id.img_userpic);
        wallet_container = findViewById(R.id.wallet_container);
        dashboard_list = findViewById(R.id.dashboard_list);
        dashboard_list.setLayoutManager(new GridLayoutManager(this, 2));
        img_parallax = findViewById(R.id.img_parallax);
        fab_musiconoff=findViewById(R.id.fab_musiconoff);

        fab_targetdirectory = findViewById(R.id.fab_targetdirectory);
        fab_exitapp = findViewById(R.id.fab_exitapp);
        fab_logout = findViewById(R.id.fab_logout);

        Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.terizla);
        aniSlide.setInterpolator(new LinearInterpolator());
        img_parallax.setAnimation(aniSlide);


        listItems.add(new DashboardItem("How to use?", "", "listItems", null,R.drawable.how));
        listItems.add(new DashboardItem("Free Mods", "", "listItems", ModsActivity.class,R.drawable.free));
        listItems.add(new DashboardItem("Premium Mods", "", "listItems", ModsActivity.class,-1));
        listItems.add(new DashboardItem("Trending Mods", "", "listItems", null,-1));
        listItems.add(new DashboardItem("Request Mod", "", "listItems", null,-1));
        listItems.add(new DashboardItem("Drone Mod", "", "listItems", null,-1));
        listItems.add(new DashboardItem("Earn Points", "", "listItems", null,-1));
        listItems.add(new DashboardItem("Global Chat", "", "listItems", null,-1));
        listItems.add(new DashboardItem("Report Bug", "", "listItems", null,-1));
        listItems.add(new DashboardItem("About", "", "listItems", null,-1));
        adapter = new DashboardAdapter(this, listItems);
        adapter.setOnItemClickListener(this);
        dashboard_list.setAdapter(adapter);


    }

    private void initListeners() {
        fab_targetdirectory.setOnClickListener(this);
        fab_logout.setOnClickListener(this);
        fab_exitapp.setOnClickListener(this);
        fab_musiconoff.setOnClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        DashboardItem dashboardItem = listItems.get(position);
        if (dashboardItem != null) {
            Intent extra = new Intent();
            extra.putExtra("uType",uType);
            if (dashboardItem.getDashboard_name().toLowerCase().contains("free") || dashboardItem.getDashboard_name().toLowerCase().contains("premium")) {
                if (dashboardItem.getDashboard_name().toLowerCase().contains("free")) {
                    extra.putExtra("selected_mod", "free");
                } else {
                    extra.putExtra("selected_mod", "premium");
                }
                tool.openXtra(this, dashboardItem.getTargetActivity(), extra);
            } else {
                if (dashboardItem.getTargetActivity() == null) {
                    tool.t(this, "Feature is currently unavailable!", 0);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_targetdirectory:
                tool.Modal modal = tool.modal(this, R.layout.directory_detector, true);
                View view = modal.view;
                Button btn_phonedetect = view.findViewById(R.id.btn_phonedetect);
                Button btn_sddetect = view.findViewById(R.id.btn_sddetect);
                Button btn_smartdetect = view.findViewById(R.id.btn_smartdetect);
                btn_smartdetect.setOnClickListener(this);
                btn_sddetect.setOnClickListener(this);
                btn_phonedetect.setOnClickListener(this);
                break;
            case R.id.fab_exitapp:
                System.exit(0);
                break;
            case R.id.fab_musiconoff:

                Intent svc = new Intent(this, BackgroundSoundService.class);
                if(fab_musiconoff.getLabelText().toLowerCase().contains("on")) {
                    fab_musiconoff.setLabelText("Music Off");
                    fab_musiconoff.setImageDrawable(getResources().getDrawable(R.drawable.vector_off_music));
                    stopService(svc);
                }else if(fab_musiconoff.getLabelText().toLowerCase().contains("off")){
                    fab_musiconoff.setLabelText("Music On");
                    fab_musiconoff.setImageDrawable(getResources().getDrawable(R.drawable.vector_music_on));
                    startService(svc);
                }
                break;
            case R.id.fab_logout:
                tool.t(this, "Feature is unavailable!", 0);
                break;
            case R.id.btn_phonedetect:

                File mlFolder = tool.phoneDetect(this);
                if (mlFolder != null) {
                    tool.t(this, "Success! " + mlFolder.getPath(), 0);
                    tool.clearSharePrefs(this,"mlforsd");
                }
                break;
            case R.id.btn_sddetect:
                // Initialize Builder
                StorageChooser chooser = new StorageChooser.Builder()
                        .withActivity(this)
                        .withFragmentManager(getFragmentManager())
                        .withMemoryBar(true)
                        .allowCustomPath(true)
                        .setType(StorageChooser.DIRECTORY_CHOOSER)
                        .build();
                chooser.show();
                chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
                    @Override
                    public void onSelect(String path) {
                        Log.e("SELECTED_PATH", path);
                        if(getMLAssetsFromSD(path)) {
                            tool.setMLDefaulPath(DashboardActivity.this, path);
                        }
                    }
                });

                //startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE), SDCARD_REQUEST_PERMISSION);
                break;
            case R.id.btn_smartdetect:
                tool.t(this, "Feature is currently unavailable!", 0);
                // Initialize Builder
//                StorageChooser chooser = new StorageChooser.Builder()
//                        .withActivity(this)
//                        .withFragmentManager(getFragmentManager())
//                        .withMemoryBar(true)
//                        .allowCustomPath(true)
//                        .setType(StorageChooser.DIRECTORY_CHOOSER)
//                        .build();
//                chooser.show();
//                chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
//                    @Override
//                    public void onSelect(String path) {
//                        Log.e("SELECTED_PATH", path);
//                        if(getMLAssetsFromSD(path)) {
//                            tool.setMLDefaulPath(DashboardActivity.this, path);
//                        }
//                    }
//                });

                break;
        }
    }

    public boolean getMLAssetsFromSD(String path) {
        String folderpath = path;
        File test = Environment.getExternalStorageDirectory();
//                        Log.d("filechooser",test.get);
        File file = new File(folderpath);
        if (path.toLowerCase().contains("com.mobile.legends")) {
            if (file.listFiles() == null) {
                tool.t(this, "No Mobile Legends directory detected!", 1);
                return false;
            }
            for (File mlFile : file.listFiles()) {
                if (mlFile.getName().toLowerCase().equals("files")) {
                    if (mlFile.listFiles().length > 0) {
                        tool.t(this, "Success sd storage!", 0);

                        return true;
                    } else {
                        tool.t(this, "I think MobileLegends is Installed in Phone Storage!", 1);

                        return false;
                    }

                }
            }
        } else {
            tool.t(this, "Select \"SD Card/Android/data/com.mobile.legends\"", 1);
            return false;
        }
        return false;
    }


}
