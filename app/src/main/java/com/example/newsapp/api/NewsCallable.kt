package com.example.newsapp.api

import retrofit2.Call
import retrofit2.http.GET
//'https://gnews.io/api/v4/top-headlines?token=5ff816d5e910a9bda80be039e253304c&country=eg&topic=$category'//newestApi

interface NewsCallable {
    @GET("/api/v4/top-headlines?token=5ff816d5e910a9bda80be039e253304c&country=eg&topic=general")
    fun GetNews(): Call<NewsApi>
}