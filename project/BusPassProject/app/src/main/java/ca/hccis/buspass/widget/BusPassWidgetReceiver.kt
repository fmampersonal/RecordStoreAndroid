package ca.hccis.buspass.widget

import BusPassWidget
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class BusPassWidgetReceiver : GlanceAppWidgetReceiver() {

    // Let MyAppWidgetReceiver know which GlanceAppWidget to use
    override val glanceAppWidget: GlanceAppWidget = BusPassWidget()
}