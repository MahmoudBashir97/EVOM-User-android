package com.mahmoud.bashir.evom_user_app.Api_Interface;


import com.mahmoud.bashir.evom_user_app.pojo.SendRequest.data_request;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface api_Interface {

    //لما تبعت الداتا مع بعض زي مثلا عبارة عن package
    @Headers({"Authorization: key=AAAAWTfqi_A:APA91bHiuXVv9PZxm24FNqKLHN1Te6qz4OH9KsgJ9Vdzv-BoA-rrc8Sow2mo0E4AFHPbBamwgugD7vczzur1S-n1vKN58QbWMrXgBXHmb1osHnZbpX82ThaE7SP_n2wohQH67vx9c_Ma",
            "Content-Type:application/json"})
    @POST("fcm/data")
    public Call<data_request> storedata(@Body data_request data);
/*
    //لما تبعت الداتا مع بعض زي مثلا عبارة عن package
    @Headers({"Authorization: key=AAAAWTfqi_A:APA91bHiuXVv9PZxm24FNqKLHN1Te6qz4OH9KsgJ9Vdzv-BoA-rrc8Sow2mo0E4AFHPbBamwgugD7vczzur1S-n1vKN58QbWMrXgBXHmb1osHnZbpX82ThaE7SP_n2wohQH67vx9c_Ma",
            "Content-Type:application/json"})
    @POST("fcm/send")
    public Call<com.chatho.chatho.Model.send> chatRequest(@Body com.chatho.chatho.Model.send send);*/
}
