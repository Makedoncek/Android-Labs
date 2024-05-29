package com.example.androidlab1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), InputFragment.OnInputSelectedListener {

    private lateinit var storageHelper: StorageHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storageHelper = StorageHelper(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, InputFragment())
                .commit()
        }

        val buttonOpen = findViewById<Button>(R.id.buttonOpen)
        buttonOpen.setOnClickListener {
            val intent = Intent(this, DataDisplayActivity::class.java)
            startActivity(intent)
        }

        val buttonClear = findViewById<Button>(R.id.buttonClear)
        buttonClear.setOnClickListener {
            val isCleared = storageHelper.clearData()
            if (isCleared) {
                Toast.makeText(this, "Дані успішно видалено", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Помилка при видаленні даних", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onInputSelected(input: String) {
        val resultFragment = ResultFragment.newInstance(input)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, resultFragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        // Оновлення даних у файлі
        val dataToSave = "Вибір-Користувача: $input"
        val isSaved = storageHelper.saveData(dataToSave)
        if (isSaved) {
            Toast.makeText(this, "Дані успішно збережено", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Помилка при збереженні даних", Toast.LENGTH_SHORT).show()
        }
    }
}
