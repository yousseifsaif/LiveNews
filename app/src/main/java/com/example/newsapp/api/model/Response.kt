package com.example.newsapp.api.model

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("totalArticles")
	val totalArticles: Int? = null,

	@field:SerializedName("articles")
	val articles: List<ArticlesItem?>? = null,


	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

