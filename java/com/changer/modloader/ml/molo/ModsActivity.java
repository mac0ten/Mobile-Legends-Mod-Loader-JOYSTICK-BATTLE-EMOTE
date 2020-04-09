package com.changer.modloader.ml.molo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.changer.modloader.ml.molo.Adapter.ModItemAdapter;
import com.changer.modloader.ml.molo.Adapter.SliderAdapter;
import com.changer.modloader.ml.molo.Objects.ModItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModsActivity extends AppCompatActivity implements ModItemAdapter.itemClickListener, View.OnClickListener {
    private boolean free = true;

    private SliderViewAdapter adapter;
    private ArrayList<ModItem> slideImages = new ArrayList<>();
    private SliderView imageSlider;
    private Toolbar toolbar;

    private RecyclerView rv_joystick, rv_aniemoji, rv_touretwarn, rv_tournament, rv_launchgame, rv_matchready, rv_matchstarted;
    private ModItemAdapter modJoystickAdapter;
    private ModItemAdapter modAniemojiAdapter;
    private FloatingActionButton btn_addjoystick;

    private Map<String, Object> mods = new HashMap<>();

    LinearSnapHelper snapHelper = new LinearSnapHelper() {
        @Override
        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
            View centerView = findSnapView(layoutManager);
            if (centerView == null)
                return RecyclerView.NO_POSITION;

            int position = layoutManager.getPosition(centerView);
            int targetPosition = -1;
            if (layoutManager.canScrollHorizontally()) {
                if (velocityX < 0) {
                    targetPosition = position - 1;
                } else {
                    targetPosition = position + 1;
                }
            }

            if (layoutManager.canScrollVertically()) {
                if (velocityY < 0) {
                    targetPosition = position - 1;
                } else {
                    targetPosition = position + 1;
                }
            }

            final int firstItem = 0;
            final int lastItem = layoutManager.getItemCount() - 1;
            targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
            return targetPosition;
        }
    };//HELP TO SNAP RECYCLERVIEW PER ITEM


    String uType = "guest";
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    final int WRITE_REQUEST_CODE=1111;
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_REQUEST_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Granted.

                }
                else{
                    //Denied.
                    tool.t(this,"Please allow read/write storage",0);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mods);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, WRITE_REQUEST_CODE);
        }
        initElemets();
        initListeners();
        createSlideItems();
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("selected_mod") && getIntent().getExtras().containsKey("uType")) {
                uType = String.valueOf(getIntent().getExtras().get("uType"));
                tool.t(this,uType,0);
                String[] mods = getResources().getStringArray(R.array.mods);
                if (getIntent().getExtras().get("selected_mod").equals("free")) {
                    free = true;
                    //toolbar.setTitle(mods[0]);
                } else {
                    free = false;
                    //toolbar.setTitle(mods[1]);
                    tool.t(this,"Premium is currently unavailable!",0);
                    finish();
                }
                String defaultPath = tool.getMLDefaultPath(this);
                if(defaultPath==null){
                    tool.t(this,"Setup target directory first!",0);
                    finish();
                }


            } else finish();
        } else {
            finish();
        }
    }

    private void createSlideItems() {
        slideImages.add(new ModItem("slide1", "", "id1", "", null, false,R.drawable.slide1));
        slideImages.add(new ModItem("slide2", "", "id1", "", null, false,R.drawable.slide2));
        slideImages.add(new ModItem("slide3", "", "id1", "", null, false,R.drawable.slide3));
        slideImages.add(new ModItem("slide4", "", "id1", "", null, false,R.drawable.slide4));
        slideImages.add(new ModItem("slide5", "", "id1", "", null, false,R.drawable.slide5));

        imageSlider.setSliderAdapter(adapter);
    }

    private void initElemets() {
        adapter = new SliderAdapter(ModsActivity.this, slideImages);
        imageSlider = findViewById(R.id.imageSlider);
        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setIndicatorSelectedColor(Color.WHITE);
        imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        imageSlider.setScrollTimeInSec(3); //set scroll delay in seconds :
        imageSlider.startAutoCycle();
        toolbar = findViewById(R.id.mods_toolbar);
        btn_addjoystick = findViewById(R.id.btn_addjoystick);

        //FOR JOYSTICK MODS

        //SET DATA
        setModsData();

        modJoystickAdapter = new ModItemAdapter((ArrayList<ModItem>) mods.get("joystick"), this, 1);
        modAniemojiAdapter = new ModItemAdapter((ArrayList<ModItem>) mods.get("aniemoji"), this, 2);


        //JOYSTICK
        rv_joystick = findViewById(R.id.mods_joystick_list);
        rv_joystick.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        rv_joystick.setHasFixedSize(true);
        //snapHelper.attachToRecyclerView(rv_joystick);

        //END
        rv_joystick.setAdapter(modJoystickAdapter);

        //ANIEMOJI
        rv_aniemoji = findViewById(R.id.mods_aniemoji_list);
        rv_aniemoji.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        rv_aniemoji.setHasFixedSize(true);
        //snapHelper.attachToRecyclerView(rv_joystick);

        //END
        rv_aniemoji.setAdapter(modAniemojiAdapter);


    }
    private void initListeners(){
        modAniemojiAdapter.setOnItemClickListener(this);
        modJoystickAdapter.setOnItemClickListener(this);
        btn_addjoystick.setOnClickListener(this);
    }


    private void setModsData() {
//        JOYSTICK
        ArrayList<ModItem> joystickmods = new ArrayList<>();
        ArrayList<ModItem> aniemoji = new ArrayList<>();

        //ADD ITEMS FROM ASSETS TO RECTCLERVIEW
        ArrayList<String> fromAssetsList = tool.assetFiles(this);
        //JOYSTICK PICS
        Map<String, Object> assetsPicsJoystick = new HashMap<>();
        /*
        assetsPicsJoystick.put("dragon0001", "https://www.mediafire.com/convkey/d9fe/hels89bt3s7twy5zg.jpg");
        assetsPicsJoystick.put("playstation0002", "https://www.mediafire.com/convkey/ff29/k7nxrm521xnrh0izg.jpg");
        assetsPicsJoystick.put("thanos0003", "http://www.pngmart.com/files/9/Thanos-Infinity-Stone-Gauntlet-Transparent-Background.png");
        assetsPicsJoystick.put("default0001", "https://www.mediafire.com/convkey/b053/6mn0nq5w48fs34tzg.jpg");
        */

        //ANIEMOJI PICS
        Map<String, Object> assetsPicsAniemoji = new HashMap<>();
        /*
        assetsPicsAniemoji.put("akosidogie0003", "https://res06.bignox.com/noxinfluencer/youtube/avatar/3d88991b6535ad071dae8e098c9e39d3.png");
        assetsPicsAniemoji.put("pewdiepie0004", "https://www.spriters-resource.com/resources/sheet_icons/81/84019.png");
        assetsPicsAniemoji.put("default0001", "https://img.mobilelegends.com/group1/M00/00/68/Cq2Ixlvz1UWACXYTAALyUn4Lhvc2082431");
        assetsPicsAniemoji.put("rickandmorty0005", "https://www.stickpng.com/assets/images/58f37731a4fa116215a92411.png");
        assetsPicsAniemoji.put("seagames0002", "https://3.bp.blogspot.com/-v_hyWldqm0E/XJRBZWgKsQI/AAAAAAAASMA/rfaRPQfh-GEWzPaaAsRZmiV9C28TItLhwCLcBGAs/s1600/Philiphines%2BSea%2BGames%2B2019.png");
        */

        if (fromAssetsList.size() > 0) {
            int i = 0;
            for (String assetFileName : fromAssetsList) {
                boolean defaultAsset = false;
                if (assetFileName.toLowerCase().contains("joystick-")) {
                    String name = assetFileName.replace("joystick-", "").replace(".unity3d", "");
                    String id = assetFileName.replace(".unity3d", "");
                    String path = tool.getMLDefaultPath(this) + tool.getMLAssetName(assetFileName, true, true);
                    String currentPic = "";
                    if (assetsPicsJoystick.containsKey(name.toLowerCase())) {
                        currentPic = assetsPicsJoystick.get(name.toLowerCase()).toString();
                    }
                    if (name.toLowerCase().contains("default")) defaultAsset = true;
                    int resId=-1;
                    if(name.length()>0 && currentPic.length()<=0) {
                        Resources res = this.getResources();
                        resId = res.getIdentifier(id.toLowerCase().replace("-","_"), "drawable", getPackageName());
                    }
                    joystickmods.add(new ModItem(name, currentPic, id, path, null, defaultAsset,resId));

                } else if (assetFileName.toLowerCase().contains("aniemoji-")) {
                    String name = assetFileName.replace("aniemoji-", "").replace(".unity3d", "");
                    String id = assetFileName.replace(".unity3d", "");
                    String path = tool.getMLDefaultPath(this) + tool.getMLAssetName(assetFileName, true, true);
                    String currentPic = "";
                    if (assetsPicsAniemoji.containsKey(name.toLowerCase())) {
                        currentPic = assetsPicsAniemoji.get(name.toLowerCase()).toString();
                    }
                    int resId=-1;
                    if(name.length()>0 && currentPic.length()<=0) {
                        Resources res = this.getResources();
                        resId = res.getIdentifier(id.toLowerCase().replace("-","_"), "drawable", getPackageName());

                    }
                    if (name.toLowerCase().contains("default")) defaultAsset = true;
                    aniemoji.add(new ModItem(name, currentPic, id, path, null, defaultAsset,resId));
                }
            }
        }
        mods.put("joystick", joystickmods);
        mods.put("aniemoji", aniemoji);
//        END

    }

    private void promptPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }


    @Override
    public void onItemClickListner(int position, ModItemAdapter.ViewHolder viewHolder, int target, View sourceClick) {

        switch (sourceClick.getId()) {
            case R.id.btn_trymod:
                promptPermission();
                final FloatingActionButton btn_trymod = viewHolder.btn_trymod;
                //CLEAR SHAREDPREF

                //END CLEAR
                Drawable iconVector = btn_trymod.getDrawable();
                String apply = "";
                String getList = null;
                String targetSharedpref = null;
                switch (target){
                    case 1:
                        getList="joystick";
                        targetSharedpref="mlmymodsjoystick";

                        for (int i = 0; i < rv_joystick.getChildCount(); i++) {
                            FloatingActionButton btntry = rv_joystick.getChildAt(i).findViewById(R.id.btn_trymod);
                            if (!rv_joystick.getChildAt(i).findViewById(R.id.btn_trymod).equals(sourceClick)) {
                                btntry.setImageDrawable(getResources().getDrawable(R.drawable.vector_check));
                            }
                        }
                        break;

                    case 2:
                        getList="aniemoji";
                        targetSharedpref="mlmymodsaniemoji";

                        for (int i = 0; i < rv_aniemoji.getChildCount(); i++) {
                            FloatingActionButton btntry = rv_aniemoji.getChildAt(i).findViewById(R.id.btn_trymod);
                            if (!rv_aniemoji.getChildAt(i).findViewById(R.id.btn_trymod).equals(sourceClick)) {
                                btntry.setImageDrawable(getResources().getDrawable(R.drawable.vector_check));
                            }
                        }
                        break;

                }


                ArrayList<ModItem> moditem = (ArrayList<ModItem>) mods.get(getList);
                if (tool.getSharedPref(ModsActivity.this, moditem.get(position).getMod_id(),target) == null) {
                    tool.clearSharePrefs(ModsActivity.this, targetSharedpref);
                    iconVector = getResources().getDrawable(R.drawable.vector_close);
                    apply = moditem.get(position).getMod_name();
                    tool.t(ModsActivity.this, "Enabled!", 0);
                    tool.setSharedPref(ModsActivity.this, moditem.get(position).getMod_id(), apply,target);
                    tool.copyAssets(this, moditem.get(position).getMod_id() + ".unity3d", moditem.get(position).getMod_path());
                } else {
                    if (!moditem.get(position).isDefaultAsset()) {
                        tool.clearSharePrefs(ModsActivity.this, targetSharedpref);
                        iconVector = getResources().getDrawable(R.drawable.vector_check);
                        tool.t(ModsActivity.this, "Disabled!", 0);
                        for (ModItem mi : moditem) {
                            if (mi.isDefaultAsset()) {
                                tool.setSharedPref(ModsActivity.this, mi.getMod_id(), mi.getMod_name(),target);
                                break;
                            }
                        }

                        //tool.setSharedPref(ModsActivity.this, moditem.get(position).getMod_id(), apply);
                    } else tool.t(ModsActivity.this, "Can't disable default!", 0);
                }
                Snackbar snackbar = Snackbar.make(sourceClick, R.string.modreadymsg, Snackbar.LENGTH_INDEFINITE).setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File getPackageName = new File(tool.getMLDefaultPath(ModsActivity.this));
                        if (getPackageName != null) {
                            if (getPackageName.exists()) {
                                String packageName = getPackageName.getName();
                                tool.restartML(ModsActivity.this, packageName);
                            } else
                                tool.t(ModsActivity.this, "Unable to restart ML (Restart ML Manually!)", 1);
                        } else
                            tool.t(ModsActivity.this, "Unable to restart ML (Restart ML Manually!)", 1);

                    }
                });

                snackbar.show();

                btn_trymod.setImageDrawable(iconVector);


                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addjoystick:
                //check if admin / moderator from database
                //end
                boolean canAdd=true;

                //show add dialog
                //end


                break;

        }
    }
}


//                    case 1://JOYSTICK
//final FloatingActionButton btn_trymod = viewHolder.btn_trymod;
//        //CLEAR SHAREDPREF
//
//        //END CLEAR
//        Drawable iconVector = btn_trymod.getDrawable();
//        String apply = "";
//        ArrayList<ModItem> moditem = (ArrayList<ModItem>) mods.get("joystick");
//        tool.t(ModsActivity.this, moditem.get(position).getMod_name(), 0);
//        if (tool.getSharedPref(ModsActivity.this, moditem.get(position).getMod_id()) == null) {
//        tool.clearSharePrefs(ModsActivity.this, "mlmymodsjoystick");
//        iconVector = getResources().getDrawable(R.drawable.vector_close);
//        apply = moditem.get(position).getMod_name();
//        tool.t(ModsActivity.this, "Enabled!", 0);
//        tool.setSharedPref(ModsActivity.this, moditem.get(position).getMod_id(), apply);
//        tool.copyAssets(this, moditem.get(position).getMod_id() + ".unity3d", moditem.get(position).getMod_path());
//        } else {
//        if (!moditem.get(position).isDefaultAsset()) {
//        tool.clearSharePrefs(ModsActivity.this, "mlmymodsjoystick");
//        iconVector = getResources().getDrawable(R.drawable.vector_check);
//        tool.t(ModsActivity.this, "Disabled!", 0);
//        for (ModItem mi : moditem) {
//        if (mi.isDefaultAsset()) {
//        tool.setSharedPref(ModsActivity.this, mi.getMod_id(), mi.getMod_name());
//        break;
//        }
//        }
//
//        //tool.setSharedPref(ModsActivity.this, moditem.get(position).getMod_id(), apply);
//        } else tool.t(ModsActivity.this, "Can't disable default!", 0);
//        }
//        Snackbar snackbar = Snackbar.make(sourceClick, "Mobile Legends Mod is Ready! Restart MobileLegends?", Snackbar.LENGTH_INDEFINITE).setAction("Yes", new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        File getPackageName = new File(tool.getMLDefaultPath(ModsActivity.this));
//        if (getPackageName != null) {
//        if (getPackageName.exists()) {
//        String packageName = getPackageName.getName();
//        tool.restartML(ModsActivity.this, packageName);
//        } else
//        tool.t(ModsActivity.this, "Unable to restart ML (Restart ML Manually!)", 1);
//        } else
//        tool.t(ModsActivity.this, "Unable to restart ML (Restart ML Manually!)", 1);
//
//        }
//        });
//
//        snackbar.show();
//
//        btn_trymod.setImageDrawable(iconVector);
//
//        for (int i = 0; i < rv_joystick.getChildCount(); i++) {
//        FloatingActionButton btntry = rv_joystick.getChildAt(i).findViewById(R.id.btn_trymod);
//        if (!rv_joystick.getChildAt(i).findViewById(R.id.btn_trymod).equals(sourceClick)) {
//        btntry.setImageDrawable(getResources().getDrawable(R.drawable.vector_check));
//        }
//        }
//
//        break;