package com.indev.jubicare_assistants.model;

public class HouseHoldModel {
    private String user_id;
    private String village_id;
    private String notification_from;
    private String notification_to;
    private String mobile;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }

    public String getNotification_from() {
        return notification_from;
    }

    public void setNotification_from(String notification_from) {
        this.notification_from = notification_from;
    }

    public String getNotification_to() {
        return notification_to;
    }

    public void setNotification_to(String notification_to) {
        this.notification_to = notification_to;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
