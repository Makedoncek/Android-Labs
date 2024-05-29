package com.example.androidlab1

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.FileInputStream

class StorageHelper(private val context: Context) {

    private val fileName = "data_storage.txt"
    private val counterFileName = "counter_storage.txt"

    fun saveData(data: String): Boolean {
        return try {
            val currentCount = getCounter()
            val fileOutputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_APPEND)
            fileOutputStream.write(("Вибір-Користувача $currentCount: $data\n").toByteArray())
            fileOutputStream.close()
            incrementCounter()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun readData(): String {
        return try {
            val fileInputStream: FileInputStream = context.openFileInput(fileName)
            val inputStreamReader = fileInputStream.bufferedReader()
            val data = inputStreamReader.use { it.readText() }
            inputStreamReader.close()
            data
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun clearData(): Boolean {
        return try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write("".toByteArray())
            fileOutputStream.close()
            resetCounter()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun deleteLastEntry(): Boolean {
        return try {
            val data = readData().split("\n").filter { it.isNotBlank() }
            if (data.isNotEmpty()) {
                val updatedData = data.dropLast(1).joinToString("\n")
                val fileOutputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
                fileOutputStream.write(updatedData.toByteArray())
                fileOutputStream.close()
                decrementCounter()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getCounter(): Int {
        return try {
            val fileInputStream: FileInputStream = context.openFileInput(counterFileName)
            val inputStreamReader = fileInputStream.bufferedReader()
            val count = inputStreamReader.use { it.readText() }
            inputStreamReader.close()
            count.toIntOrNull() ?: 1
        } catch (e: Exception) {
            1
        }
    }

    private fun incrementCounter() {
        val currentCount = getCounter()
        val newCount = currentCount + 1
        try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(counterFileName, Context.MODE_PRIVATE)
            fileOutputStream.write(newCount.toString().toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun decrementCounter() {
        val currentCount = getCounter()
        val newCount = if (currentCount > 1) currentCount - 1 else 1
        try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(counterFileName, Context.MODE_PRIVATE)
            fileOutputStream.write(newCount.toString().toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun resetCounter() {
        try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(counterFileName, Context.MODE_PRIVATE)
            fileOutputStream.write("1".toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
