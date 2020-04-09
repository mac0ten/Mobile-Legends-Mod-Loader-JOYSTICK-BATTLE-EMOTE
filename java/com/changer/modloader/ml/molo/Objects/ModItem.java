package com.changer.modloader.ml.molo.Objects;

public class ModItem {
    private String mod_name
            ,mod_pic
            ,mod_id
            ,mod_path; // SAMPLE PATH Android/data/com.mobile.legends/files/UI/android/
    private Class targetActivity;
    private boolean defaultAsset;
    private int drawableImage;

    public ModItem() {
    }

    public String getMod_name() {
        return mod_name;
    }

    public void setMod_name(String mod_name) {
        this.mod_name = mod_name;
    }

    public String getMod_pic() {
        return mod_pic;
    }

    public void setMod_pic(String mod_pic) {
        this.mod_pic = mod_pic;
    }

    public String getMod_id() {
        return mod_id;
    }

    public void setMod_id(String mod_id) {
        this.mod_id = mod_id;
    }

    public String getMod_path() {
        return mod_path;
    }

    public void setMod_path(String mod_path) {
        this.mod_path = mod_path;
    }

    public Class getTargetActivity() {
        return targetActivity;
    }

    public void setTargetActivity(Class targetActivity) {
        this.targetActivity = targetActivity;
    }

    public boolean isDefaultAsset() {
        return defaultAsset;
    }

    public void setDefaultAsset(boolean defaultAsset) {
        this.defaultAsset = defaultAsset;
    }

    public int getDrawableImage() {
        return drawableImage;
    }

    public void setDrawableImage(int drawableImage) {
        this.drawableImage = drawableImage;
    }

    public ModItem(String mod_name, String mod_pic, String mod_id, String mod_path, Class targetActivity, boolean defaultAsset, int drawableImage) {

        this.mod_name = mod_name;
        this.mod_pic = mod_pic;
        this.mod_id = mod_id;
        this.mod_path = mod_path;
        this.targetActivity = targetActivity;
        this.defaultAsset = defaultAsset;
        this.drawableImage = drawableImage;
    }
}
