package ca.hccis.buspass.bo

import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ca.hccis.buspass.MainActivity
import ca.hccis.buspass.utility.CisUtility

object SplashScreenBO {

    fun handleSplashScreen(mainActivity: MainActivity) {

        val splashScreen =
            mainActivity.installSplashScreen() //built-in method from API for installing the splash screen
        var keepSplashScreenOn = true


        CisUtility.log("BJTEST", "Starting splash screen")
        //using built-in keepScreenOnCondition API method to maintain splash screen visibility
        splashScreen.setKeepOnScreenCondition {
            keepSplashScreenOn
        }

        //loop the splash screen message on the main thread
        val handler = Handler(Looper.getMainLooper())

        //splash screen message is never missed and runs for 3 seconds
        handler.postDelayed({
            keepSplashScreenOn = false //splash screen disappears after 3 seconds
            CisUtility.log("BJTEST", "End splash screen")
        }, 5000)

    }
}