package com.example.newsapp.category

import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.CategoryListBinding

class CategoryAdapter(
    private val category: List<CategoryList>,
    private val onCategoryClick: (String) -> Unit,
    private var selectedCategory: String? = null
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(val binding: CategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageView: ImageView = itemView.findViewById(R.id.image)
//            val titleTextView: TextView = itemView.findViewById(R.id.cardName)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = CategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryItem = category[position]
        holder.binding.image.setImageResource(categoryItem.image)
//        holder.titleTextView.text = categoryItem.title
        if (category[position].title == selectedCategory) {
            holder.binding.parrent.setBackgroundResource( R.drawable.category_list)
        } else {
            holder.binding.parrent.setBackgroundResource(R.drawable.category_list_unselected)
        }
        holder.itemView.setOnClickListener {
            onCategoryClick(categoryItem.title)
        }
    }

    override fun getItemCount(): Int {
        return category.size
    }
    fun updateSelectedCategory(newCategory: String) {
        selectedCategory = newCategory
        notifyDataSetChanged()
    }

}