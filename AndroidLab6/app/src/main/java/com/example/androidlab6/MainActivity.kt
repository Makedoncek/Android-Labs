package com.example.androidlab6

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
    private lateinit var categorySpinner: Spinner
    private val categories = listOf("general", "business", "entertainment", "health", "science", "sports", "technology")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        categorySpinner = findViewById(R.id.categorySpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                fetchNews(categories[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        fetchNews("general")
    }

    private fun fetchNews(category: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val newsApiService = retrofit.create(NewsApiService::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = newsApiService.getTopHeadlines("us", category, "cbdae7febc34435da16f12b447c6069d")
                if (response.articles.isNotEmpty()) {
                    val newsList = response.articles
                    newsAdapter = NewsAdapter(this@MainActivity, newsList)
                    recyclerView.adapter = newsAdapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
