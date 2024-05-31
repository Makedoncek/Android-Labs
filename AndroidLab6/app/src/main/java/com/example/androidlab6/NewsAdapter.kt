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

class NewsAdapter(private val context: Context, private val newsList: List<NewsArticle>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = newsList[position]

        holder.newsTitleTextView.text = article.title ?: "[No Title]"
        holder.newsDescriptionTextView.text = article.description ?: "[No Description]"

        val imageUrl = article.urlToImage ?: "https://example.com/placeholder_image.png"
        Glide.with(context)
            .load(imageUrl)
            .into(holder.newsImageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra("url", article.url)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsTitleTextView: TextView = itemView.findViewById(R.id.newsTitleTextView)
        val newsDescriptionTextView: TextView = itemView.findViewById(R.id.newsDescriptionTextView)
        val newsImageView: ImageView = itemView.findViewById(R.id.newsImageView)
    }
}
