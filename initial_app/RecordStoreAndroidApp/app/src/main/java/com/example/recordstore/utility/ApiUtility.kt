package com.example.recordstore.utility

import com.example.recordstore.entity.Album
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object ApiUtility {
    // 10.0.2.2 points to your computer's localhost from the Android Emulator
    private const val BASE_URL = "http://10.0.2.2:8080/api/albums"
    private val gson = Gson()

    suspend fun fetchAlbums(): List<Album> = withContext(Dispatchers.IO) {
        val url = URL(BASE_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        try {
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                // Read the JSON response and convert it into a List of Album objects
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val listType = object : TypeToken<List<Album>>() {}.type
                gson.fromJson(response, listType)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        } finally {
            connection.disconnect()
        }
    }

    suspend fun insert(album: Album) = withContext(Dispatchers.IO) {
        val url = URL(BASE_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        try {
            // Convert the Album object into JSON and send it to the Java server
            val jsonPayload = gson.toJson(album)
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(jsonPayload)
                writer.flush()
            }
            CisUtility.log("API", "Insert Response Code: ${connection.responseCode}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }
    }

    suspend fun delete(album: Album) = withContext(Dispatchers.IO) {
        // Delete targets the specific album ID in the URL
        val url = URL("$BASE_URL/${album.id}")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "DELETE"

        try {
            CisUtility.log("API", "Delete Response Code: ${connection.responseCode}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }
    }
}