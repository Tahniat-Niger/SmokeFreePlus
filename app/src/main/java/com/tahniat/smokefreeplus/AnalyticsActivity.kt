package com.tahniat.smokefreeplus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONObject

class AnalyticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        val chart = findViewById<BarChart>(R.id.barChart)
        val logPrefs = getSharedPreferences("dailyLogs", MODE_PRIVATE)
        val logData = logPrefs.getString("logData", "{}")
        val logJson = JSONObject(logData)

        val entries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()
        var index = 0

        val keys = logJson.keys().asSequence().sorted()
        for (key in keys) {
            val entry = logJson.getJSONObject(key)
            val smoked = if (entry.getBoolean("smoked")) 1f else 0f
            entries.add(BarEntry(index.toFloat(), smoked))
            labels.add(key.substring(5)) // Show MM-DD
            index++
        }

        val dataSet = BarDataSet(entries, "Smoking Log (0=No, 1=Yes)")
        val barData = BarData(dataSet)
        chart.data = barData

        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.granularity = 1f
        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.axisMaximum = 1.2f
        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.invalidate()
    }
}
