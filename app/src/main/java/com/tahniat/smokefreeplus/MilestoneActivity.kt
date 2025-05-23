package com.tahniat.smokefreeplus

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MilestoneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Your Milestones"

        val prefs = getSharedPreferences("smokefree", MODE_PRIVATE)
        val currentStreak = prefs.getInt("streak", 0)
        val milestones = listOf(1, 3, 7, 14, 30)
        val badgeList = milestones.map { day ->
            if (currentStreak >= day)
                "✅ Day $day Milestone Achieved!"
            else
                "🔒 Day $day Milestone Locked"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, badgeList)
        findViewById<ListView>(R.id.milestoneListView).adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


}
