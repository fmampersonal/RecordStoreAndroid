package entity

import utility.CisUtility
import java.math.BigDecimal

class Album {
    var id: Int? = 0
    var title: String?
    var artist: String? = ""
    var genre: String? = ""
    var isUsed: Boolean = false
    var basePrice: Double? = 0.0
    var finalPrice: BigDecimal? = null

    init {
        title = "Unknown"
    }

    // Gathers initial information for a new entity
    fun getInformation() {
        title = CisUtility.getInputString("Album Title")
        artist = CisUtility.getInputString("Artist")
        genre = CisUtility.getInputString("Genre")
        isUsed = CisUtility.getInputBoolean("Is this a used record?")
        basePrice = CisUtility.getInputDouble("Base Price (e.g., 20.00)")
    }

    // New method to allow editing existing data
    fun editInformation() {
        println("Editing Album. Enter new values, or enter 'x' to keep current value.")

        val newTitle = CisUtility.getInputString("Title [Current: $title]")
        if (!newTitle.equals("x", ignoreCase = true)) title = newTitle

        val newArtist = CisUtility.getInputString("Artist [Current: $artist]")
        if (!newArtist.equals("x", ignoreCase = true)) artist = newArtist

        val newGenre = CisUtility.getInputString("Genre [Current: $genre]")
        if (!newGenre.equals("x", ignoreCase = true)) genre = newGenre

        // Re-asking boolean and double for simplicity
        isUsed = CisUtility.getInputBoolean("Is this a used record? [Current: $isUsed]")
        basePrice = CisUtility.getInputDouble("Base Price [Current: $basePrice]")
    }

    override fun toString(): String {
        return "\nAlbum(ID: $id)\n" +
                "Title=$title\n" +
                "Artist=$artist\n" +
                "Genre=$genre\n" +
                "Used Record=$isUsed\n" +
                "Final Price=" + CisUtility.toCurrency(finalPrice?.toDouble())
    }
}