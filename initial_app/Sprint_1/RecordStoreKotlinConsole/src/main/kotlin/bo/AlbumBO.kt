package bo

import entity.Album
import java.math.BigDecimal

object AlbumBO {
    /**
     * Calculates the final price.
     * Used records get a 20% discount.
     */
    fun calculateFinalPrice(album: Album): Double {
        var cost = album.basePrice ?: 0.0

        if (album.isUsed) {
            cost *= 0.80 // 20% discount for used
        }

        album.finalPrice = BigDecimal(cost)
        return cost
    }
}