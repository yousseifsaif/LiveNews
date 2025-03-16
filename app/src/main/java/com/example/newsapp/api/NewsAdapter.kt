package com.example.newsapp.api

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsapp.R
import com.example.newsapp.databinding.ContentVerticalBinding

class NewsAdapter(val activity: Activity, val articles: List<Articles>) :
    Adapter<NewsAdapter.NewsViewHolder>() {
    class NewsViewHolder(val binding: ContentVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ContentVerticalBinding.inflate(activity.layoutInflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount() = articles.size


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.binding.tittle.text = articles[position].title
        Glide.with(holder.binding.img.context)
            .load(articles[position].image)
            .error(com.google.android.material.R.drawable.mtrl_ic_error)
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            .into(holder.binding.img)
        holder.binding.container.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articles[position].url))
            activity.startActivity(intent)
        }
        holder.binding.favourite.setImageResource (
            if (articles[position].isFav) R.drawable.favo else R.drawable.favourite

            )
        holder.binding.favourite.setOnClickListener {
            articles[position].isFav = !articles[position].isFav
            notifyItemChanged(position) // تحديث العنصر لتغيير الأيقونة
        }
        }
    }


