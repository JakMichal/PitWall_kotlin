package com.example.pitwall.widget

import android.annotation.SuppressLint
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.text.FontWeight
import com.example.pitwall.R
import androidx.glance.unit.ColorProvider

@SuppressLint("RestrictedApi")
class PitWallTop3ConstructorsWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val prefs = context.getSharedPreferences(WidgetConstants.PREFS_NAME, Context.MODE_PRIVATE)

        val title = context.getString(R.string.constructor_championship)

        val top3 = listOf(
            prefs.getString(WidgetConstants.KEY_CONSTRUCTOR1_NAME, "-") to prefs.getInt(WidgetConstants.KEY_CONSTRUCTOR1_POINTS, 0),
            prefs.getString(WidgetConstants.KEY_CONSTRUCTOR2_NAME, "-") to prefs.getInt(WidgetConstants.KEY_CONSTRUCTOR2_POINTS, 0),
            prefs.getString(WidgetConstants.KEY_CONSTRUCTOR3_NAME, "-") to prefs.getInt(WidgetConstants.KEY_CONSTRUCTOR3_POINTS, 0)
        )

        provideContent {
            GlanceTheme {
                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .background(GlanceTheme.colors.background),
                    verticalAlignment = Alignment.Vertical.Top
                ) {
                    Text(
                        text = title,
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFE10600)),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = GlanceModifier.height(6.dp))
                    top3.forEachIndexed { index, (name, points) ->
                        Row(
                            modifier = GlanceModifier.fillMaxWidth().padding(bottom = 8.dp),
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            Column(modifier = GlanceModifier.defaultWeight()) {
                                Text(
                                    text = "${index + 1}. $name",
                                    style = TextStyle(color = ColorProvider(Color.White), fontSize = 17.sp)
                                )
                            }
                            Text(
                                text = "$points pts",
                                style = TextStyle(color = ColorProvider(Color(0xFF999999)), fontSize = 17.sp)
                            )
                        }
                        Spacer(modifier = GlanceModifier.height(3.dp))
                    }
                }
            }
        }
    }
}

class PitWallTop3ConstructorsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PitWallTop3ConstructorsWidget()
}