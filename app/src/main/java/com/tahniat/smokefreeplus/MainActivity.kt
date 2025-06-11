package com.tahniat.smokefreeplus

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var streakText: TextView
    private lateinit var dateText: TextView
    private lateinit var startDateText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("smokefree", MODE_PRIVATE)

        val onboardingComplete = prefs.getBoolean("onboarding_complete", false)
        if (!onboardingComplete) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val userName = prefs.getString("username", "User")
        findViewById<TextView>(R.id.titleText).text = "Welcome, $userName!\nDid you smoke today?"

        val btnYes = findViewById<Button>(R.id.btnYes)
        val btnNo = findViewById<Button>(R.id.btnNo)
        resultText = findViewById(R.id.resultText)
        streakText = findViewById(R.id.streakText)
        dateText = findViewById(R.id.dateText)
        startDateText = findViewById(R.id.startDateText)
        val badgeText = findViewById<TextView>(R.id.badgeText)
        badgeText.visibility = View.GONE

        val displayFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        val displayToday = displayFormat.format(Calendar.getInstance().time)
        dateText.text = "Today: $displayToday"

        val startDateKey = "startDate"
        val savedStartDate = prefs.getString(startDateKey, null)
        val startDateDisplay = savedStartDate ?: displayToday.also {
            prefs.edit().putString(startDateKey, it).apply()
        }
        startDateText.text = "Tracking Since: $startDateDisplay"

        val dateFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val startDate = dateFormatter.parse(startDateDisplay)
        val daysSinceStart = ((Calendar.getInstance().time.time - startDate.time) / (1000 * 60 * 60 * 24)).toInt() + 1

        var currentStreak = prefs.getInt("streak", 0)
        if (currentStreak > daysSinceStart) {
            currentStreak = daysSinceStart
        }
        streakText.text = "Current Smoke-Free Streak: $currentStreak day(s)"

        btnYes.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirm Smoking")
                .setMessage("Are you sure you smoked today? Your streak will reset to 0.")
                .setPositiveButton("Yes, I did") { _, _ ->
                    prefs.edit().putInt("streak", 0).putString("lastLogDate", today).apply()
                    saveDailyLog(today, true, 0, prefs)
                    streakText.text = "Current Smoke-Free Streak: 0 day(s)"
                    resultText.text = "Thanks for logging. Try again tomorrow!"
                    badgeText.text = "🔓 Keep going! Unlock your first badge after 1 smoke-free day!"
                    badgeText.visibility = View.VISIBLE
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        btnNo.setOnClickListener {
            val lastLoggedDate = prefs.getString("lastLogDate", null)

            if (today == lastLoggedDate) {
                resultText.text = "You've already logged today!"
            } else {
                val newStreak = currentStreak + 1
                prefs.edit().putInt("streak", newStreak).putString("lastLogDate", today).apply()
                saveDailyLog(today, false, newStreak, prefs)

                val cigsPerDay = prefs.getInt("cigsPerDay", 0)
                val pricePerCig = prefs.getFloat("pricePerCig", 0f)
                val moneySaved = cigsPerDay * pricePerCig

                streakText.text = "Current Smoke-Free Streak: $newStreak day(s)"
                resultText.text = "Great job! You’re one step closer to quitting!\nYou saved ${"%.2f".format(moneySaved)} 💰"

                when (newStreak) {
                    1 -> badgeText.text = "🎯 Day 1: Great start to your smoke-free journey!"
                    3 -> badgeText.text = "🎖️ 3-Day Milestone: You're gaining momentum!"
                    7 -> badgeText.text = "🏅 1 Week Smoke-Free: Amazing progress!"
                    14 -> badgeText.text = "🥇 2 Weeks: You're becoming unstoppable!"
                    else -> badgeText.text = ""
                }
                badgeText.visibility = if (badgeText.text.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }

        val viewHistoryBtn = findViewById<Button>(R.id.viewHistoryBtn)
        viewHistoryBtn.setOnClickListener {
            val logPrefs = getSharedPreferences("dailyLogs", MODE_PRIVATE)
            val logData = logPrefs.getString("logData", "{}")
            val logJson = JSONObject(logData)
            val logList = mutableListOf<String>()

            val keys = logJson.keys()
            while (keys.hasNext()) {
                val date = keys.next()
                val entry = logJson.getJSONObject(date)
                val smoked = entry.getBoolean("smoked")
                val streak = entry.getInt("streak")
                logList.add("$date – Smoked: ${if (smoked) "Yes" else "No"}, Streak: $streak")
            }

            val message = if (logList.isEmpty()) "No log data found." else logList.sorted().joinToString("\n")

            AlertDialog.Builder(this)
                .setTitle("Log History")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show()
        }

        // Show progress summary
        val summaryText = findViewById<TextView>(R.id.summaryText)
        val totalDays = daysSinceStart
        val streak = prefs.getInt("streak", 0)
        val cigsPerDay = prefs.getInt("cigsPerDay", 0)
        val pricePerCig = prefs.getFloat("pricePerCig", 0f)

        val totalCigsAvoided = streak * cigsPerDay
        val moneySaved = totalCigsAvoided * pricePerCig

        summaryText.text = "Total Days Tracked: $totalDays\n" +
                "Cigarettes Avoided: $totalCigsAvoided\n" +
                "Money Saved: ${"%.2f".format(moneySaved)} 💰"

        // badges and milestone intent

        findViewById<Button>(R.id.viewMilestonesBtn).setOnClickListener {
            startActivity(Intent(this, MilestoneActivity::class.java))
        }
        findViewById<Button>(R.id.viewAnalyticsBtn).setOnClickListener {
            startActivity(Intent(this, AnalyticsActivity::class.java))
        }




    }

    private fun saveDailyLog(date: String, smoked: Boolean, streak: Int, prefs: android.content.SharedPreferences) {
        val logPrefs = getSharedPreferences("dailyLogs", MODE_PRIVATE)
        val allLogs = logPrefs.getString("logData", "{}")
        val logJson = JSONObject(allLogs)

        val todayLog = JSONObject()
        todayLog.put("smoked", smoked)
        todayLog.put("streak", streak)

        logJson.put(date, todayLog)
        logPrefs.edit().putString("logData", logJson.toString()).apply()
    }
}
