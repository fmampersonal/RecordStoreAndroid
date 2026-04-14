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

package ca.hccis.buspass.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.hccis.buspass.viewmodel.BusPassViewModel
import ca.hccis.buspass.bo.NotificationBO
import ca.hccis.buspass.entity.BusPass
import ca.hccis.buspass.screens.chatpage.ChatPageScreen
import ca.hccis.buspass.screens.buspasslist.BusPassListScreen
import ca.hccis.buspass.utility.CisUtility
import ca.hccis.buspass.screens.buspassadd.AddBusPassScreen
import ca.hccis.buspass.screens.buspassqr.QrCodeBusPassScreen
import np.com.bimalkafle.easybot.viewModel.ChatViewModel


/**
 * Enum class representing the navigation routes for the application.
 *
 * @property route The string route for the navigation destination.
 */
enum class Screen(val route: String) {
    MainScreen("main_screen"),
    AddBusPass("add_bus_pass"),
    QrCodeBusPass("qr_code_bus_pass"),
    ChatPage("chat_page")

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
fun MainNavigation(
    busPassViewModel: BusPassViewModel,
    notificationBO: NotificationBO,
    chatViewModel: ChatViewModel,
    windowSize: WindowSizeClass
) {

    val navController = rememberNavController()
    val allBusPasses = busPassViewModel.allBusPasses.collectAsState(initial = emptyList()).value
    var busPass: BusPass? = null
    //*********************************************
    //Accessing context to allow Toast message.
    //*********************************************
    val context = LocalContext.current
    var recentlyDeletedBusPass: BusPass? = null // Tracks the most recently deleted BusPass

    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        // Main screen composable
        composable(Screen.MainScreen.route) {

            CisUtility.log("BJTEST", "MainNavigation checking busPasses list.  ")
            BusPassListScreen(
                busPasses = allBusPasses, // Observe and display all bus passes from Room
                onAddPassClick = {
                    busPassViewModel.playMusic(true)
                    busPass = BusPass() // Ensuring new bus pass when adding from FAB
                    navController.navigate(Screen.AddBusPass.route)
                },
                onSyncClick = {
                    busPassViewModel.syncWithApi(context) // Trigger sync with the API
                },
                onEdit = { selectedBusPass ->
                    /* Handle edit action */
                    busPass = selectedBusPass
                    navController.navigate(Screen.AddBusPass.route)
                },
                onQrCode = { selectedBusPass ->
                    /* Handle edit action */
                    busPass = selectedBusPass
                    navController.navigate(Screen.QrCodeBusPass.route)
                },
                onDelete = { selectedBusPass ->
                    recentlyDeletedBusPass = busPass
                    /* Handle delete action */
                    busPassViewModel.delete(selectedBusPass) // Delete from Room database
                },
                onUndoDelete = { deletedBusPass ->
                    recentlyDeletedBusPass = null
                    // Add a new bus pass
                    busPassViewModel.insert(deletedBusPass)
                },
                onShare = { selectedBusPass ->

                    val messageIntro = "Here are the details of a bus pass:\n"
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, messageIntro + selectedBusPass.toString())
                        putExtra(
                            Intent.EXTRA_SUBJECT,
                            "Bus Pass#" + selectedBusPass.id + " for " + selectedBusPass.name
                        )

                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                },
                onChatClick = {
                    navController.navigate(Screen.ChatPage.route)
                },
                windowSize = windowSize

            )
        }

        // Add bus pass screen composable
        composable(Screen.AddBusPass.route) {
            AddBusPassScreen(
                bussPass = busPass, // Pass the currently selected bus pass for editing
//                onBusPassAdded = onBusPassAdded,
                onBusPassAdded = { newBusPass ->
                    if (busPass!!.id == 0) {
                        // Add a new bus pass
                        busPassViewModel.insert(newBusPass)
                        busPassViewModel.setReminderCalendarEvent(newBusPass)
                    } else {
                        // Update existing bus pass
                        busPassViewModel.update(newBusPass)
                    }

                    //Stop the service
                    busPassViewModel.playMusic(false)

                    // Send a notification
                    notificationBO.// Sends a notification when a new bus pass is added.
                    sendNotification("Bus Pass", "Bus Pass updates.", "bus_pass_notifications")


                    // Reset the selected bus pass after operation
                    busPass = BusPass()
                    // Navigate back to the main screen
                    navController.navigateUp()
                },
                back = {
                    busPassViewModel.playMusic(false)
                    navController.navigateUp()
                }
            )
        }
        // Add bus pass screen composable
        composable(Screen.QrCodeBusPass.route) {
            QrCodeBusPassScreen(
                busPass = busPass,
                back = {
                    busPassViewModel.playMusic(false)
                    navController.navigateUp()
                }
            )
        }

        // Chat page composable
        composable(Screen.ChatPage.route) {

            ChatPageScreen(
                back = {
                    navController.navigateUp()
                },
                chatViewModel
            )
        }

    }
}
