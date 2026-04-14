/**
 * MainNavigation.kt
 *
 * Handles navigation between screens in the Bus Pass application.
 *
 * This file defines an enum `Screen` to manage navigation routes and implements a
 * `MainNavigation` composable function to handle navigation between the main screen
 * and the add bus pass screen.
 *
 * @Author: Anthony Odu
 * @Date: 2025-01-11
 */

package com.example.buspass.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buspass.entity.BusPass

/**
 * Enum class representing the navigation routes for the application.
 *
 * @property route The string route for the navigation destination.
 */
enum class Screen(val route: String) {
    MainScreen("main_screen"),
    AddBusPass("add_bus_pass")
}

/**
 * MainNavigation
 *
 * The main navigation composable that defines navigation between the screens
 * of the Bus Pass application. It uses a [NavHost] with routes defined in the
 * [Screen] enum.
 *
 * The navigation includes:
 * - `MainScreen`: Displays the list of bus passes and a floating action button (FAB) to navigate to the "Add Bus Pass" screen.
 * - `AddBusPass`: A screen for adding a new bus pass.
 *
 * The bus passes are stored in a mutable list that is shared between the screens.
 */
@SuppressLint("MutableCollectionMutableState")
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    var busPasses by remember { mutableStateOf(mutableListOf<BusPass>()) } // State-tracked list
    var busPass: BusPass? = null
    var id by remember { mutableIntStateOf(1) }
    var editId by remember { mutableIntStateOf(0) }

    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        // Main screen composable
        composable(Screen.MainScreen.route) {
            HomeScreen(
                busPasses = busPasses, // Pass the current list of bus passes
                onAddPassClick = { navController.navigate(Screen.AddBusPass.route) },
                onEdit = { selectedBussPass ->
                    /* Handle edit action */
                    busPass = selectedBussPass
                    editId = selectedBussPass.id
                    navController.navigate(Screen.AddBusPass.route)
                },
                onDelete = { busPass ->
                    /* Handle delete action */
                    busPasses = busPasses.toMutableList().apply { remove(busPass) } // Update state
                }
            )
        }

        // Add bus pass screen composable
        composable(Screen.AddBusPass.route) {
            AddBusPassScreen(
                bussPass = busPass,
                onBusPassAdded = { newBusPass ->
                    println(newBusPass.id)


                    busPasses = if (busPass == null) {
                        // Add new BusPass
                        busPasses.toMutableList().apply {
                            newBusPass.id = id
                            add(newBusPass)
                            id++
                        }
                    } else {
                        // Update existing BusPass
                        busPasses.toMutableList().apply {
                            val index = indexOfFirst { it.id == busPass!!.id }
                            if (index != -1) {
                                newBusPass.id = editId
                                set(index, newBusPass)
                            }
                        }
                    }

                    // Reset states
                    busPass = null
                    editId = 0
                    // Navigate back to the main screen
                    navController.navigateUp()
                },
                back = {
                    navController.navigateUp()
                }
            )
        }
    }
}
