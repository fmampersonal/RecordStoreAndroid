package com.example.recordstore.bo

import com.example.recordstore.entity.Album
import java.math.BigDecimal

object AlbumBO {
    fun calculateFinalPrice(album: Album): Double {
        var cost = album.basePrice

        if (album.isUsed) {
            cost *= 0.80 // 20% discount for used
        }

        album.finalPrice = cost // <-- Updated this line
        return cost
    }

    fun setAlbumDefaults(album: Album) {
        album.basePrice = 0.0
        album.isUsed = false
        album.finalPrice = 0.0
    }
}