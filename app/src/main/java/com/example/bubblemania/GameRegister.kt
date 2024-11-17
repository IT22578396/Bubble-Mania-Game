package com.example.bubblemania

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Button
import android.widget.EditText


class GameRegister : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_register)

     editText = findViewById(R.id.editTextText45)
     val continueBtn = findViewById<Button>(R.id.continueBtn)


        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        continueBtn.setOnClickListener {
            val playerName = editText.text.toString()
            savePlayerName(playerName)
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun savePlayerName(playerName: String) {
        val editor = sharedPreferences.edit()
        editor.putString("playerName", playerName)
        editor.apply()
    }
}