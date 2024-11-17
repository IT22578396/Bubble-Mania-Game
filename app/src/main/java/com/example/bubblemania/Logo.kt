package com.example.bubblemania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Logo : AppCompatActivity() {
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)

        handler.postDelayed({
            val intent = Intent(this, GameRegister::class.java)
            startActivity(intent)
            finish()
        }, 2000L)
    }
}