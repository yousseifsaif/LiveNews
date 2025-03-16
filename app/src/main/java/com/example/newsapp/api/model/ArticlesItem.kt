package com.example.newsapp.api.model

import com.google.gson.annotations.SerializedName

data class ArticlesItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("publishedAt")
    val publishedAt: String? = null,

    @field:SerializedName("description")
    val description: String? = null,
//
//    @field:SerializedName("source")
//    val source: Source? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)

