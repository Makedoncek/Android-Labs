package com.example.androidlab6

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class NewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        val webView: WebView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()

        val url = intent.getStringExtra("url")
        if (url != null) {
            webView.loadUrl(url)
        }
    }
}
