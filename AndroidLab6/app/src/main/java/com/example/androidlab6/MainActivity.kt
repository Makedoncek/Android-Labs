package com.example.androidlab6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchNews()
    }

    private fun fetchNews() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val newsApiService = retrofit.create(NewsApiService::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = newsApiService.getTopHeadlines("us", "cbdae7febc34435da16f12b447c6069d")
                if (response.articles.isNotEmpty()) {
                    val newsList = response.articles
                    newsAdapter = NewsAdapter(newsList)
                    recyclerView.adapter = newsAdapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Обробка помилок
            }
        }
    }
}
