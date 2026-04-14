package com.example.recordstore.bo

import com.example.recordstore.entity.Album
import java.math.BigDecimal

object AlbumBO {
    fun calculateFinalPrice(album: Album): Double {
        var cost = album.albumPrice // Changed from basePrice

        if (album.giftWrapped) { // Changed from isUsed
            cost *= 0.80 // 20% discount for used
        }

        album.totalCost = cost // Changed from finalPrice
        return cost
    }

    fun setAlbumDefaults(album: Album) {
        album.albumPrice = 0.0
        album.giftWrapped = false
        album.totalCost = 0.0
    }
}