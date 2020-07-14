package com.mahmoud.bashir.evom_user_app.pojo;

public class driver_Info_Model {

    String id;
    Double driver_lat;
    Double driver_lng;
    String driver_name;
    String driver_ph;
    String driver_img;
    String car_Model;
    String car_Number;
    String driver_token;

    public driver_Info_Model(String id, Double driver_lat, Double driver_lng, String driver_name, String driver_ph, String driver_img, String car_Model, String car_Number,String driver_token) {
        this.id = id;
        this.driver_lat = driver_lat;
        this.driver_lng = driver_lng;
        this.driver_name = driver_name;
        this.driver_ph = driver_ph;
        this.driver_img = driver_img;
        this.car_Model = car_Model;
        this.car_Number = car_Number;
        this.driver_token = driver_token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getDriver_lat() {
        return driver_lat;
    }

    public void setDriver_lat(Double driver_lat) {
        this.driver_lat = driver_lat;
    }

    public Double getDriver_lng() {
        return driver_lng;
    }

    public void setDriver_lng(Double driver_lng) {
        this.driver_lng = driver_lng;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_ph() {
        return driver_ph;
    }

    public void setDriver_ph(String driver_ph) {
        this.driver_ph = driver_ph;
    }

    public String getDriver_img() {
        return driver_img;
    }

    public void setDriver_img(String driver_img) {
        this.driver_img = driver_img;
    }

    public String getCar_Model() {
        return car_Model;
    }

    public void setCar_Model(String car_Model) {
        this.car_Model = car_Model;
    }

    public String getCar_Number() {
        return car_Number;
    }

    public void setCar_Number(String car_Number) {
        this.car_Number = car_Number;
    }

    public String getDriver_token() {
        return driver_token;
    }

    public void setDriver_token(String driver_token) {
        this.driver_token = driver_token;
    }
}
