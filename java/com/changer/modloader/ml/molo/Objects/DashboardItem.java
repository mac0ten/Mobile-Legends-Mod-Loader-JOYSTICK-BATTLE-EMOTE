package com.changer.modloader.ml.molo.Objects;

public class DashboardItem {
    private String dashboard_name
            ,dashboard_img
            ,dashboard_id;
    private Class targetActivity;
    private int dashboard_drawable_img;

    public String getDashboard_name() {
        return dashboard_name;
    }

    public void setDashboard_name(String dashboard_name) {
        this.dashboard_name = dashboard_name;
    }

    public String getDashboard_img() {
        return dashboard_img;
    }

    public void setDashboard_img(String dashboard_img) {
        this.dashboard_img = dashboard_img;
    }

    public String getDashboard_id() {
        return dashboard_id;
    }

    public void setDashboard_id(String dashboard_id) {
        this.dashboard_id = dashboard_id;
    }

    public Class getTargetActivity() {
        return targetActivity;
    }

    public void setTargetActivity(Class targetActivity) {
        this.targetActivity = targetActivity;
    }

    public int getDashboard_drawable_img() {
        return dashboard_drawable_img;
    }

    public void setDashboard_drawable_img(int dashboard_drawable_img) {
        this.dashboard_drawable_img = dashboard_drawable_img;
    }

    public DashboardItem(String dashboard_name, String dashboard_img, String dashboard_id, Class targetActivity, int dashboard_drawable_img) {

        this.dashboard_name = dashboard_name;
        this.dashboard_img = dashboard_img;
        this.dashboard_id = dashboard_id;
        this.targetActivity = targetActivity;
        this.dashboard_drawable_img = dashboard_drawable_img;
    }
}
