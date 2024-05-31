package com.example.androidlab6

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// Оновлюємо адаптер для відображення зображень
class NewsAdapter(private val context: Context, private val newsList: List<NewsArticle>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsImageView: ImageView = itemView.findViewById(R.id.newsImageView)
        val newsTitleTextView: TextView = itemView.findViewById(R.id.newsTitleTextView)
        val newsDescriptionTextView: TextView = itemView.findViewById(R.id.newsDescriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentArticle = newsList[position]
        holder.newsTitleTextView.text = currentArticle.title
        holder.newsDescriptionTextView.text = currentArticle.description
        Glide.with(context)
            .load(currentArticle.urlToImage)
            .placeholder(R.drawable.placeholder_image) // встановлюємо placeholder
            .error(R.drawable.placeholder_image) // встановлюємо зображення у разі помилки
            .into(holder.newsImageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra("url", currentArticle.url)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = newsList.size
}