package com.techafresh.composewidgettest

import android.content.Context
import androidx.glance.Button
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.text.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.BackgroundModifier
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.action
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

object MyAppWidget : GlanceAppWidget() {
    val countKey = intPreferencesKey("count")
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent(countKey)
        }
    }

}


class MyAppWidgetReceiver : GlanceAppWidgetReceiver(){
    override val glanceAppWidget: GlanceAppWidget
        get() = MyAppWidget
}


@Composable
fun WidgetContent(countKey: Preferences.Key<Int>){
    val count = currentState(key = countKey) ?: 0
    Column(
        modifier = GlanceModifier.fillMaxSize().background(Color.DarkGray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            maxLines = 1,
            style = TextStyle(color = ColorProvider(Color.White), fontSize = 26.sp)
        )
        Spacer(modifier = GlanceModifier.height(10.dp))

        Row(modifier = GlanceModifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalAlignment = Alignment.CenterVertically) {
            Button(
                text = "Increase",
                onClick = actionRunCallback(IncrementActionCallback::class.java)
            )

            Spacer(modifier = GlanceModifier.width(15.dp))

            Button(
                text = "Decrease",
                onClick = actionRunCallback(DecrementActionCallback::class.java)
            )
        }
    }
}

class IncrementActionCallback : ActionCallback{
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context , glanceId){ prefs ->
            val currentCount = prefs[MyAppWidget.countKey]
            if (currentCount != null){
                prefs[MyAppWidget.countKey] = currentCount + 1
            }else {
                prefs[MyAppWidget.countKey] = 1
            }
        }

        MyAppWidget.update(context , glanceId)
    }

}

class DecrementActionCallback : ActionCallback{
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context , glanceId){ prefs ->
            val currentCount = prefs[MyAppWidget.countKey]
            if (currentCount != null){
                prefs[MyAppWidget.countKey] = currentCount - 1
            }else{
                prefs[MyAppWidget.countKey] = 0
            }
        }

        MyAppWidget.update(context , glanceId)
    }
}
