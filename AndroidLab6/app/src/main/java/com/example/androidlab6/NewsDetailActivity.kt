package com.example.androidlab6

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class NewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        val url = intent.getStringExtra("url") ?: return

        val webView: WebView = findViewById(R.id.webView)
        webView.loadUrl(url)
    }
}
