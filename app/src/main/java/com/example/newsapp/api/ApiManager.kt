package com.example.newsapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//'https://gnews.io/api/v4/top-headlines?token=5ff816d5e910a9bda80be039e253304c&country=eg&topic=$category'//newestApi

class ApiManager {
    companion object{
        private var retorfit: Retrofit? = null
        fun getRetrofitInstance(): Retrofit? {
            if (retorfit == null) {
                retorfit = Retrofit.Builder()
                    .baseUrl("https://gnews.io/api/v4/top-headlines")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
            return retorfit!!

    }
        fun getApis():webServices{
            return getRetrofitInstance()!!.create(webServices::class.java)
        }
}}