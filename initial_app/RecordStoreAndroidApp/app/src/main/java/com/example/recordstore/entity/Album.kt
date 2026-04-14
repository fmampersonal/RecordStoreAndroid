package com.example.recordstore.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "albums")
data class Album(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @SerializedName("artistName")
    var artistName: String = "", // Rename variable from 'artist' to 'artistName'

    @SerializedName("customerName")
    var customerName: String = "", // Rename variable from 'title' to 'customerName'

    @SerializedName("formatType")
    var formatType: String = "Vinyl", // Rename variable from 'genre' to 'formatType'

    @SerializedName("giftWrapped")
    var giftWrapped: Boolean = false, // Rename variable from 'isUsed' to 'giftWrapped'

    @SerializedName("albumPrice")
    var albumPrice: Double = 0.0, // Rename variable from 'basePrice' to 'albumPrice'

    @SerializedName("totalCost")
    var totalCost: Double = 0.0, // Rename variable from 'finalPrice' to 'totalCost'

    @SerializedName("unitsSold")
    var unitsSold: Int = 1
)