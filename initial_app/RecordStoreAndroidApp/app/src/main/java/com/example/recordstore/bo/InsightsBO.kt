package com.example.recordstore.bo

import com.example.recordstore.entity.Album

object InsightsBO {

    fun getTopArtist(albums: List<Album>): String {
        if (albums.isEmpty()) return "No Data"
        // Uses 'artistName' from your Album entity
        return albums.groupBy { it.artistName }
            .maxByOrNull { entry -> entry.value.sumOf { it.unitsSold } }
            ?.key ?: "Unknown"
    }

    fun getTotalRevenue(albums: List<Album>): Double {
        // In your entity, 'totalCost' represents the revenue/total price
        return albums.sumOf { it.totalCost }
    }

    fun getSmartRecommendation(albums: List<Album>): String {
        // Logic: Find albums where unitsSold is greater than 5
        val highSellers = albums.filter { it.unitsSold > 5 }

        return if (highSellers.isNotEmpty()) {
            val rec = highSellers.random()
            // Uses 'albumName' is likely missing from your provided snippet,
            // but assuming 'artistName' is available
            "High demand for records by ${rec.artistName}. Consider increasing stock."
        } else {
            "Inventory levels look stable. No immediate restock needed."
        }
    }
}