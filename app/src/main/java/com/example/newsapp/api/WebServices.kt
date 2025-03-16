package com.example.newsapp.api

import com.example.newsapp.api.model.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface webServices {
    //'https://gnews.io/api/v4/top-headlines?token=5ff816d5e910a9bda80be039e253304c&country=eg&topic=$category'//newestApi
    @GET("articles")
    fun getNewsSources(@Query("token") apiKey: String,
                      @Query("country") country: String,
                       @Query("topic")category: String): Call<Response>


}