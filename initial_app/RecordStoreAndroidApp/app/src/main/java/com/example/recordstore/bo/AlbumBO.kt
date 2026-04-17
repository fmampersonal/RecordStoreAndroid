package com.example.recordstore.bo

import com.example.recordstore.entity.Album
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AlbumBO {
    fun calculateFinalPrice(album: Album): Double {

        // 1. Calculate Subtotal (Price x Units)
        val calculatedSubtotal = (album.albumPrice * album.unitsSold).toInt()
        album.subtotal = calculatedSubtotal

        // 2. Calculate Total (Subtotal + $5 Gift Wrap fee)
        var cost = calculatedSubtotal.toDouble()
        if (album.giftWrapped) {
            cost += 5.0 // Add flat $5 fee
        }
        album.totalCost = cost

        // 3. Set the current date if it is empty
        if (album.dateOfSale.isEmpty()) {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            album.dateOfSale = sdf.format(Date())
        }

        return cost
    }

    fun setAlbumDefaults(album: Album) {
        album.albumPrice = 0.0
        album.giftWrapped = false
        album.subtotal = 0
        album.totalCost = 0.0
        album.unitsSold = 1
        album.dateOfSale = ""
    }
}