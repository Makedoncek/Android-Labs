package com.example.androidlab1

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class DataDisplayActivity : AppCompatActivity() {

    private lateinit var storageHelper: StorageHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_display)

        storageHelper = StorageHelper(this)

        val textViewData = findViewById<TextView>(R.id.textViewData)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        val data = storageHelper.readData()

        if (data.isNotEmpty()) {
            textViewData.text = data
        } else {
            textViewData.text = "Дані відсутні"
        }

        buttonBack.setOnClickListener {
            finish() // Закриває поточну активність і повертається до попередньої
        }
    }
}
