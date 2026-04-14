package com.example.recordstore.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

// @Entity tells Room to make a database table named "albums"
@Entity(tableName = "albums")
data class Album(
    // @PrimaryKey auto-generates a unique ID for every new record
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var artist: String = "",
    var genre: String = "",
    var isUsed: Boolean = false,
    var basePrice: Double = 0.0,

    // Room doesn't natively know how to save BigDecimal, so we convert it to a Double for storage
    var finalPrice: Double = 0.0
)
