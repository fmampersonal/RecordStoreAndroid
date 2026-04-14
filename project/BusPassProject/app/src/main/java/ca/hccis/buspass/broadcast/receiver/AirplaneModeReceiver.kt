package ca.hccis.buspass.broadcast.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import ca.hccis.buspass.viewmodel.BusPassViewModel
import ca.hccis.buspass.utility.CisUtility

/**
 * A BroadcastReceiver that listens for changes in the airplane mode state.
 * This receiver is triggered when the airplane mode is enabled or disabled on the device.
 * Author: Mike Tamatey
 * Date: 2025-01-11
 */

class AirplaneModeReceiver :BroadcastReceiver() {

    private var busPassViewModel: BusPassViewModel? = null
    fun setViewModel(viewModelIn: BusPassViewModel){
        CisUtility.log("BJTEST","Setting the view model")
        busPassViewModel = viewModelIn
    }

    // this function will be executed when the user changes his airplane mode
    override fun onReceive(context: Context?, intent: Intent?) {

        //if getBooleanExtra contains null value,it will directly return back
        val isAirplaneModeEnabled = intent?.getBooleanExtra("state", false) ?: return

        //checking whether airplane mode is enabled or not
        CisUtility.log("BJTEST","setting is in airplane mode from receiver: "+isAirplaneModeEnabled)
        if (isAirplaneModeEnabled) {
            //showing the toast message if airplane mode is enabled
            busPassViewModel?.setIsInAirplaneMode(isAirplaneModeEnabled)
            Toast.makeText(context, "Airplane Mode Enabled", Toast.LENGTH_LONG).show()
        } else {
            //showing the toast message if airplane mode is disabled
            busPassViewModel?.setIsInAirplaneMode(isAirplaneModeEnabled)
            Toast.makeText(context, "Airplane Mode Disabled", Toast.LENGTH_LONG).show()
        }
    }
}