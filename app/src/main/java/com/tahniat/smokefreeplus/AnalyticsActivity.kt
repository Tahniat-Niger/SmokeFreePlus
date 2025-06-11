package com.tahniat.smokefreeplus

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONObject

class AnalyticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val chart = findViewById<BarChart>(R.id.barChart)
        val streakChart = findViewById<LineChart>(R.id.streakChart)

        val logPrefs = getSharedPreferences("dailyLogs", MODE_PRIVATE)
        val logData = logPrefs.getString("logData", "{}")
        val logJson = JSONObject(logData)

        val sortedKeys = logJson.keys().asSequence().toList().sorted()

        val entries = mutableListOf<BarEntry>()
        val streakEntries = mutableListOf<Entry>()
        val labels = mutableListOf<String>()

        var index = 0
        for (key in sortedKeys) {
            val entry = logJson.getJSONObject(key)
            val smoked = if (entry.getBoolean("smoked")) 1f else 0f
            val streak = entry.getInt("streak")

            entries.add(BarEntry(index.toFloat(), smoked))
            streakEntries.add(Entry(index.toFloat(), streak.toFloat()))
            labels.add(key.substring(5)) // Show MM-DD
            index++
        }

        // Smoking Log Bar Chart
        val barDataSet = BarDataSet(entries, "Smoking Log (0=No, 1=Yes)")
        barDataSet.color = Color.CYAN
        val barData = BarData(barDataSet)
        chart.data = barData
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.granularity = 1f
        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.axisMaximum = 1.2f
        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.invalidate()

        // Streak Progress Line Chart
        val streakDataSet = LineDataSet(streakEntries, "Streak Progress")
        streakDataSet.color = Color.BLUE
        streakDataSet.circleRadius = 4f
        streakDataSet.setDrawValues(false)
        val lineData = LineData(streakDataSet)
        streakChart.data = lineData
        streakChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        streakChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        streakChart.axisRight.isEnabled = false
        streakChart.description.isEnabled = false
        streakChart.invalidate()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
