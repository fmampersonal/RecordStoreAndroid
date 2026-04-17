package ca.hccis.recordstore.screens

import android.annotation.SuppressLint
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.hccis.recordstore.screens.insights.InsightsScreen
import ca.hccis.recordstore.screens.recordlist.HomeScreen
import ca.hccis.recordstore.screens.recordsave.AddAlbumScreen
import ca.hccis.recordstore.entity.Album
import ca.hccis.recordstore.viewmodel.AlbumViewModel
import kotlinx.coroutines.launch

enum class Screen(val route: String) {
    MainScreen("main_screen"),
    AddAlbum("add_album"),
    Chat("chat")
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MainNavigation(viewModel: AlbumViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val albums by viewModel.allAlbums.collectAsState(initial = emptyList())
    var albumToEdit: Album? by remember { mutableStateOf(null) }

    // 👇 Global Snackbar State & Coroutine Scope 👇
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {

        composable(Screen.MainScreen.route) {
            HomeScreen(
                albums = albums,
                snackbarHostState = snackbarHostState, // Pass to HomeScreen
                onAddAlbumClick = {
                    albumToEdit = null
                    navController.navigate(Screen.AddAlbum.route)
                },
                onEdit = { selectedAlbum ->
                    albumToEdit = selectedAlbum
                    navController.navigate(Screen.AddAlbum.route)
                },
                onDelete = { albumToRemove ->
                    viewModel.delete(albumToRemove)
                    // Trigger Delete Notification
                    scope.launch { snackbarHostState.showSnackbar("Sale deleted successfully") }
                },
                onSyncClick = {
                    viewModel.syncWithApi(context)
                },
                onPlayClick = {
                    viewModel.playMusic(true)
                },
                onStopClick = {
                    viewModel.playMusic(false)
                },
                onSetReminder = { selectedAlbum ->
                    viewModel.setReminderCalendarEvent(selectedAlbum)
                },
                onChatClick = {
                    navController.navigate(Screen.Chat.route)
                }
            )
        }

        composable(Screen.AddAlbum.route) {
            AddAlbumScreen(
                album = albumToEdit,
                onAlbumAdded = { newAlbum ->
                    if (albumToEdit == null) {
                        viewModel.insert(newAlbum)
                        // Trigger Add Notification
                        scope.launch { snackbarHostState.showSnackbar("Sale added successfully") }
                    } else {
                        newAlbum.id = albumToEdit!!.id
                        viewModel.update(newAlbum)
                        // Trigger Edit Notification
                        scope.launch { snackbarHostState.showSnackbar("Sale updated successfully") }
                    }
                    albumToEdit = null
                    navController.navigateUp()
                },
                back = {
                    albumToEdit = null
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.Chat.route) {
            InsightsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}