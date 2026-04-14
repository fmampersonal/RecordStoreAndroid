package ca.hccis.buspass.viewmodel

import BusPassWidget
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ca.hccis.buspass.bo.BusPassBO
import ca.hccis.buspass.db.BusPassRepository
import ca.hccis.buspass.entity.BusPass
import ca.hccis.buspass.service.BackgroundMusicService
import ca.hccis.buspass.utility.CisUtility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing bus passes.
 * Provides a layer between the UI and the repository to manage the app's data.
 *
 * @property repository The BusPassRepository that handles data operations.
 */
class BusPassViewModel(private val repository: BusPassRepository) : ViewModel() {


    var isInAirplaneMode: Boolean = false //Defaulting to not in airplane mode.
    lateinit var contextForToast: Context

    fun setIsInAirplaneMode(isEnabled: Boolean) {
        CisUtility.log("BJTEST", "Setting the airplane mode to " + isEnabled)
        isInAirplaneMode = isEnabled
    }


    fun playMusic(start: Boolean) {
        val intent = Intent(contextForToast, BackgroundMusicService::class.java)
        if (start) {
            contextForToast?.startService(intent)
        }else{
            contextForToast?.stopService(intent)
        }
    }


    // Flow to observe all bus passes from the Room database
    val allBusPasses: Flow<List<BusPass>> = repository.allBusPasses

    /**
     * Insert a new bus pass into the database.
     * @param busPass The BusPass entity to insert.
     */
    fun insert(busPass: BusPass) = viewModelScope.launch {

        //TODO 2 - Add to the api as well.  Post the new bus pass to the api.  (Add/Update)

        repository.insert(busPass)
    }

    /**
     * Update an existing bus pass in the database.
     * @param busPass The BusPass entity to update.
     */
    fun update(busPass: BusPass) = viewModelScope.launch {

        //TODO 3 - Sync the update to the api as well.

        repository.update(busPass)
    }

    /**
     * Delete a bus pass from the database.
     * @param busPass The BusPass entity to delete.
     */
    fun delete(busPass: BusPass) = viewModelScope.launch {

        //TODO 3 - Sync the delete to the api as well.

        repository.delete(busPass)
    }

    /**
     * Sync bus passes from the API and save them to the local database.
     * This method fetches data from the REST API and updates the Room database.
     */
    fun syncWithApi(context: Context) = viewModelScope.launch {
        CisUtility.log("BJTEST", "STarting syncWithApi airplane enabled: " + isInAirplaneMode)
        try {
            if (!isInAirplaneMode) {
                CisUtility.log("BJTEST", "syncing with api")
                repository.fetchAndSaveBusPassesFromApi()
                Toast.makeText(contextForToast, "synced", Toast.LENGTH_LONG).show()
            } else {
                CisUtility.log("BJTEST", "can not sync in airplane mode")
                Toast.makeText(
                    contextForToast,
                    "Can not sync while in airplane mode",
                    Toast.LENGTH_LONG
                ).show()
            }

            //Widget triggered to update.
            BusPassWidget().updateAll(context)

        } catch (e: Exception) {
            // Log or handle errors here
            CisUtility.log("BJTEST", "Exception sync with api")
            Toast.makeText(contextForToast, "Exception occurred during sync", Toast.LENGTH_LONG)
                .show()
            e.printStackTrace()
        }
    }


    /**
     * Adds a bus pass reminder to the Google Calendar if the required permission is granted.
     * This function ensures that calendar events can only be added if WRITE_CALENDAR permission is available.
     *
     * @param busPass The bus pass entity containing details such as date, time, and user information.
     */
    public fun setReminderCalendarEvent(busPass: BusPass) {

        if (ContextCompat.checkSelfPermission(
                contextForToast,
                Manifest.permission.WRITE_CALENDAR
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Add the bus pass event to the calendar
            BusPassBO.addBusPassReminder(contextForToast, busPass)
            Toast.makeText(
                contextForToast,
                "Bus Pass Reminder Set in Google Calendar",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Log.e("Permissions", "Cannot add reminder - Calendar permissions not granted.")
            Toast.makeText(
                contextForToast,
                "Cannot add reminder - Calendar permissions not granted.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}

/**
 * Factory for creating a BusPassViewModel instance.
 *
 * @param repository The BusPassRepository instance to inject into the ViewModel.
 */
class BusPassViewModelFactory(private val repository: BusPassRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusPassViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusPassViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
