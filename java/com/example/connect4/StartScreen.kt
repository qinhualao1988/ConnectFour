package com.example.connect4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_screen)

        val btnStartGame = findViewById<Button>(R.id.btnStartGame)

        btnStartGame.setOnClickListener {
            // When clicked, open MainActivity (the game)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            
            finish()
        }
    }
}