package ca.hccis.recordstore.widget

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
import ca.hccis.recordstore.MainActivity
import ca.hccis.recordstore.db.AlbumDatabase
import ca.hccis.recordstore.utility.CisUtility

class RecordStoreWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // 1. Initialize ONLY the database (skip the Repository)
        val database = AlbumDatabase.getInstance(context)

        // 2. Ask the local database directly! No network calls, so it loads instantly.
        val localAlbumsList = database.albumDao.getAllAlbumsList()
        val count = localAlbumsList.size

        CisUtility.log("BJTEST", "Widget loaded locally. Count: $count")

        provideContent {
            // 3. Pass the count directly into your Composable
            MyContent(count)
        }
    }

    @Composable
    private fun MyContent(count: Int) {
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Total Albums: $count", modifier = GlanceModifier.padding(12.dp))
            Row(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    text = "Open Store",
                    onClick = actionStartActivity<MainActivity>()
                )
            }
        }
    }
}