package ca.hccis.recordstore.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "albums")
data class Album(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    // 👇 Added to match Java API
    @SerializedName("dateOfSale")
    var dateOfSale: String = "",

    @SerializedName("artistName")
    var artistName: String = "",

    @SerializedName("customerName")
    var customerName: String = "",

    @SerializedName("formatType")
    var formatType: String = "Vinyl",

    @SerializedName("giftWrapped")
    var giftWrapped: Boolean = false,

    @SerializedName("albumPrice")
    var albumPrice: Double = 0.0,

    // 👇 Added to match Java API
    @SerializedName("subtotal")
    var subtotal: Int = 0,

    @SerializedName("totalCost")
    var totalCost: Double = 0.0,

    @SerializedName("unitsSold")
    var unitsSold: Int = 1
)