import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import ca.hccis.buspass.MainActivity
import ca.hccis.buspass.db.BusPassDatabase
import ca.hccis.buspass.db.BusPassRepository
import ca.hccis.buspass.utility.CisUtility

class BusPassWidget : GlanceAppWidget() {

    var count:Int = 0

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        //*****************************************************************
        //TODO Widget consistency update enhancement
        //https://stackoverflow.com/questions/77088363/android-jetpack-glance-1-0-0-problems-updating-widget
        //*****************************************************************

        // Initialize database and repository
        val database = BusPassDatabase.getInstance(context)
        val repository = BusPassRepository(database.busPassDao)
        count = repository.countBusPasses()
        CisUtility.log("BJTEST","In provideGlance, set the count to:"+count)

        provideContent {
            // create your AppWidget here
            MyContent(context)
        }
    }

    @Composable
    private fun MyContent(context: Context) {

        //***************************************************************************
        CisUtility.log("BJTEST count in widget","Count-->"+count);

        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = ""+count, modifier = GlanceModifier.padding(12.dp))
            Row(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    text = "Go",
                    onClick = actionStartActivity<MainActivity>()
                )
            }
        }
    }
}