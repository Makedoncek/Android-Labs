package com.example.androidlab6

import NewsApiService
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
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
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private val categories = listOf("general", "business", "entertainment", "health", "science", "sports", "technology")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        categorySpinner = findViewById(R.id.categorySpinner)
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                fetchNews(categories[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        searchEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                val query = searchEditText.text.toString()
                fetchNews(categorySpinner.selectedItem.toString(), query)
                true
            } else {
                false
            }
        }

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            fetchNews(categorySpinner.selectedItem.toString(), query)
        }

        fetchNews("general")
    }

    private fun fetchNews(category: String, query: String = "") {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val newsApiService = retrofit.create(NewsApiService::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = if (query.isEmpty()) {
                    newsApiService.getTopHeadlines("us", category, "cbdae7febc34435da16f12b447c6069d")
                } else {
                    newsApiService.searchNews(query, "cbdae7febc34435da16f12b447c6069d")
                }
                if (response.articles.isNotEmpty()) {
                    val newsList = response.articles.filterNot {
                        it.title?.contains("[Removed]") == true || it.description?.contains("[Removed]") == true
                    }
                    newsAdapter = NewsAdapter(this@MainActivity, newsList)
                    recyclerView.adapter = newsAdapter
                } else {
                    // Handle the case when no articles match the search query
                    newsAdapter = NewsAdapter(this@MainActivity, emptyList())
                    recyclerView.adapter = newsAdapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
