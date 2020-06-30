package com.lustycoder.myapplication.rest;


import com.lustycoder.myapplication.model.MeetingModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("schedule")
    Call<List<MeetingModel>> postRawJSON(@Query("date") String one);


}
