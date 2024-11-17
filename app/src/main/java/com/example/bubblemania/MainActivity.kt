package com.example.bubblemania

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), GameTask {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rootLayout: LinearLayout
    private lateinit var startBtn: Button
    private lateinit var mGameView: GameView
    private lateinit var score: TextView
    private lateinit var playerNameTextView: TextView
    private var highestScore: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        playerNameTextView = findViewById(R.id.userNameTextView)

        // Retrieve the highest score from SharedPreferences
        highestScore = sharedPreferences.getInt("highestScore", 0)

        // Display the welcome message with placeholders
        val playerName = sharedPreferences.getString("playerName", "")
        val welcomeMessage = getString(R.string.welcome_message, playerName, highestScore)
        playerNameTextView.text = welcomeMessage

        startBtn.setOnClickListener {
            startGame()
        }
    }

    private fun startGame() {
        mGameView = GameView(this, this)
        rootLayout.addView(mGameView)
        startBtn.visibility = View.GONE
        score.visibility = View.GONE
        mGameView.startAnimation()
    }

    override fun closeGame(score: Int) {
        // Update the highest score if the current score is higher
        if (score > highestScore) {
            highestScore = score
            // Save the highest score to SharedPreferences
            sharedPreferences.edit().putInt("highestScore", highestScore).apply()
        }

        // Update the UI with the current score
        this.score.text = "Score : $score"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        this.score.visibility = View.VISIBLE
        mGameView.stopAnimation()
    }
}