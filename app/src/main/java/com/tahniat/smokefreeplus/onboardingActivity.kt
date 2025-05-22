package com.tahniat.smokefreeplus

// OnboardingActivity.kt
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val nameInput = findViewById<EditText>(R.id.editName)
        val ageInput = findViewById<EditText>(R.id.editAge)
        val cigsInput = findViewById<EditText>(R.id.editCigsPerDay)
        val priceInput = findViewById<EditText>(R.id.editPricePerCig)
        val goalGroup = findViewById<RadioGroup>(R.id.goalGroup)
        val continueBtn = findViewById<Button>(R.id.btnContinue)

        continueBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val age = ageInput.text.toString()
            val cigs = cigsInput.text.toString()
            val price = priceInput.text.toString()
            val selectedGoalId = goalGroup.checkedRadioButtonId

            if (name.isEmpty() || cigs.isEmpty() || price.isEmpty() || selectedGoalId == -1) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val goal = findViewById<RadioButton>(selectedGoalId).text.toString()

            val prefs = getSharedPreferences("smokefree", MODE_PRIVATE)
            prefs.edit()
                .putString("username", name)
                .putString("goal", goal)
                .putString("age", age)
                .putInt("cigsPerDay", cigs.toInt())
                .putFloat("pricePerCig", price.toFloat())
                .putBoolean("onboarding_complete", true)
                .apply()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
