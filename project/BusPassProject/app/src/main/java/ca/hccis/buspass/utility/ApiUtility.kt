package ca.hccis.buspass.utility

import android.util.Log
import ca.hccis.buspass.entity.BusPass
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object ApiUtility {
    private const val BASE_URL = "http://10.0.2.2:8081/api"
    private const val URL_EXTRA = "/BusPassService/busPasses"
    private const val TAG: String = "BJM API"

    /**
     * Fetches bus passes from the REST API.
     */
    fun fetchBusPasses(): List<BusPass> {
        val url = URL("$BASE_URL$URL_EXTRA")
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                return parseBusPasses(response)
            } else {
                throw Exception("Failed to fetch bus passes: HTTP ${connection.responseCode}")
            }
        } finally {
            connection.disconnect()
        }
    }

    /**
     * Parses the JSON response into a list of BusPass objects.
     */
    private fun parseBusPasses(jsonResponse: String): List<BusPass> {
        val busPassList = mutableListOf<BusPass>()
        val jsonArray = JSONArray(jsonResponse)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val busPass = BusPass(
                id = jsonObject.getInt("id"),
                name = jsonObject.getString("name"),
                address = jsonObject.getString("address"),
                city = jsonObject.getString("city"),
                preferredRoute = jsonObject.getString("preferredRoute"),
                passType = jsonObject.getInt("passType"),
                validForRuralRoute = jsonObject.getBoolean("validForRuralRoute"),
                lengthOfPass = jsonObject.getInt("lengthOfPass"),
                startDate = jsonObject.getString("startDate"),
                cost = jsonObject.getDouble("cost").toBigDecimal() // Convert to BigDecimal
            )
            busPassList.add(busPass)
        }

        return busPassList
    }

    /**
     * Add league to REST API.
     */
    fun insert(busPass: BusPass) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL("$BASE_URL$URL_EXTRA")
            val connection = url.openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true // Enables sending data

                var gson = Gson()
                var jsonString = gson.toJson(busPass)
                CisUtility.log("BJM gson", jsonString)

                // Write data to output stream
                OutputStreamWriter(connection.outputStream).use { it.write(jsonString) }

                connection.connect()

                if (connection.responseCode == 201) {
                    Log.d(TAG, "data is inserted in api")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection.disconnect()
            }
        }
    }

    /**
     * DELETE
     */
    fun delete(busPass: BusPass) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL("$BASE_URL$URL_EXTRA" + "/" + busPass.id)
            val connection = url.openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "DELETE"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    CisUtility.log("DELETE", "Successfully deleted " + busPass.id)
                } else {
                    throw Exception("Failed to delete bus pass: HTTP ${connection.responseCode}")
                }
            } finally {
                connection.disconnect()
            }
        }
    }


}
