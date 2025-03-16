package com.example.newsapp.api

data class NewsApi(

    val articles: List<Articles>,

)


data class Articles(
    val title: String,
    val description: String,
    val image: String,
    val publishedAt: String,
    val url: String,
    var isFav: Boolean = false

)