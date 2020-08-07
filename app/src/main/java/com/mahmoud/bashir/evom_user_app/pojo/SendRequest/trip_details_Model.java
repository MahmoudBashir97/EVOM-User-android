package com.mahmoud.bashir.evom_user_app.pojo.SendRequest;

public class trip_details_Model {

    String date ;
    String time ;
    Double userplace_lat ;
    Double userplace_lng ;
    Double userdest_lat ;
    Double userdest_lng ;
    String tripPrice ;

    public trip_details_Model(String date, String time, Double userplace_lat, Double userplace_lng, Double userdest_lat, Double userdest_lng, String tripPrice) {
        this.date = date;
        this.time = time;
        this.userplace_lat = userplace_lat;
        this.userplace_lng = userplace_lng;
        this.userdest_lat = userdest_lat;
        this.userdest_lng = userdest_lng;
        this.tripPrice = tripPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getUserplace_lat() {
        return userplace_lat;
    }

    public void setUserplace_lat(Double userplace_lat) {
        this.userplace_lat = userplace_lat;
    }

    public Double getUserplace_lng() {
        return userplace_lng;
    }

    public void setUserplace_lng(Double userplace_lng) {
        this.userplace_lng = userplace_lng;
    }

    public Double getUserdest_lat() {
        return userdest_lat;
    }

    public void setUserdest_lat(Double userdest_lat) {
        this.userdest_lat = userdest_lat;
    }

    public Double getUserdest_lng() {
        return userdest_lng;
    }

    public void setUserdest_lng(Double userdest_lng) {
        this.userdest_lng = userdest_lng;
    }

    public String getTripPrice() {
        return tripPrice;
    }

    public void setTripPrice(String tripPrice) {
        this.tripPrice = tripPrice;
    }
}
