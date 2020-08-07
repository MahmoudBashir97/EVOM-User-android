package com.mahmoud.bashir.evom_user_app.pojo.SendRequest;

public class data_request {

    String id;
    String deviceToken;
    String name;
    String phone;
    double user_lat;
    double user_lng;
    double dest_lat;
    double dest_lng;
    String time;
    String requestStatus;


    public data_request(String id, String deviceToken, String name, String phone, double user_lat, double user_lng, double dest_lat, double dest_lng, String time,String requestStatus) {
        this.id = id;
        this.deviceToken = deviceToken;
        this.name = name;
        this.phone = phone;
        this.user_lat = user_lat;
        this.user_lng = user_lng;
        this.dest_lat = dest_lat;
        this.dest_lng = dest_lng;
        this.time = time;
        this.requestStatus=requestStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getUser_lat() {
        return user_lat;
    }

    public void setUser_lat(double user_lat) {
        this.user_lat = user_lat;
    }

    public double getUser_lng() {
        return user_lng;
    }

    public void setUser_lng(double user_lng) {
        this.user_lng = user_lng;
    }

    public double getDest_lat() {
        return dest_lat;
    }

    public void setDest_lat(double dest_lat) {
        this.dest_lat = dest_lat;
    }

    public double getDest_lng() {
        return dest_lng;
    }

    public void setDest_lng(double dest_lng) {
        this.dest_lng = dest_lng;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
