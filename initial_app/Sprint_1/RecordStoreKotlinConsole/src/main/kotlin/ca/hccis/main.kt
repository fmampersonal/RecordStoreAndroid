package ca.hccis

import bo.AlbumBO
import entity.Album
import utility.CisUtility

var albums: ArrayList<Album> = ArrayList()
var nextId = 1

fun main() {
    val name = "Record Store Manager"
    println("Welcome to the $name application!\n")

    val MENU = "\nMain Menu\nA-Add an Album\nB-Show all Albums\nE-Edit an Album\nX-eXit"
    var option: String

    do {
        option = CisUtility.getInputString(MENU)
        when (option.uppercase()) {
            "A" -> add()
            "B" -> show()
            "E" -> edit()
            "X" -> println("Goodbye!")
            else -> println("Invalid option.")
        }
    } while (!option.equals("X", ignoreCase = true))
}

fun add(): Album {
    println("\n--- Add a new Album ---")
    val album = Album()
    album.id = nextId++

    album.getInformation()
    AlbumBO.calculateFinalPrice(album)

    albums.add(album)

    println("\nSUCCESS: Album Added!")
    println("Summary of added entity:")
    println(album.toString())

    return album
}

fun show() {
    println("\n--- Current Inventory ---")
    if (albums.isEmpty()) {
        println("No albums in inventory.")
    } else {
        for (album in albums) {
            println(album)
        }
    }
}

fun edit() {
    println("\n--- Edit an Album ---")
    if (albums.isEmpty()) {
        println("No albums available to edit.")
        return
    }

    val idToEdit = CisUtility.getInputInt("Enter the ID of the album you want to edit")
    val albumToEdit = albums.find { it.id == idToEdit }

    if (albumToEdit != null) {
        albumToEdit.editInformation()
        AlbumBO.calculateFinalPrice(albumToEdit)

        println("\nSUCCESS: Album Updated!")
        println("Summary of updated entity:")
        println(albumToEdit.toString())
    } else {
        println("Error: Album with ID $idToEdit not found.")
    }
}