package com.example.androidlab1

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.FileInputStream

class StorageHelper(private val context: Context) {

    private val fileName = "data_storage.txt"

    fun saveData(data: String): Boolean {
        return try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_APPEND)
            fileOutputStream.write((data + "\n").toByteArray())
            fileOutputStream.close()
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
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
