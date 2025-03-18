package com.example.newsapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//'https://gnews.io/api/v4/top-headlines?token=5ff816d5e910a9bda80be039e253304c&country=eg&topic=$category'//newestApi

interface NewsCallable {

    @GET("/api/v4/top-headlines?token=2fad7be1432adca32c1f30f8db45d7cc&country=eg&topic=general&max=50")
    fun GetNews(
        @Query("category") category: String,
        @Query("country") country: String


        ): Call<NewsApi>
}