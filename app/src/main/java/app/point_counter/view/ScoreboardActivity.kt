package app.point_counter.view

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import app.point_counter.view.MainActivity
import app.point_counter.R
import android.util.Log

class ScoreboardActivity : MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)
        Log.d("DEBUG", "ScoreboardActivity cargada")

        // Referencias a vistas
        val redBox: LinearLayout = findViewById(R.id.red_box)
        val blueBox: LinearLayout = findViewById(R.id.blue_box)

        val redScore: TextView = findViewById(R.id.red_score)
        val blueScore: TextView = findViewById(R.id.blue_score)

        val redPlus: Button = findViewById(R.id.red_plus)
        val redMinus: Button = findViewById(R.id.red_minus)
        val bluePlus: Button = findViewById(R.id.blue_plus)
        val blueMinus: Button = findViewById(R.id.blue_minus)

        // Lógica para sumar y restar puntos
        redPlus.setOnClickListener {
            redScore.text = (redScore.text.toString().toInt() + 1).toString()
        }

        redMinus.setOnClickListener {
            redScore.text = (redScore.text.toString().toInt() - 1).toString()
        }

        bluePlus.setOnClickListener {
            blueScore.text = (blueScore.text.toString().toInt() + 1).toString()
        }

        blueMinus.setOnClickListener {
            blueScore.text = (blueScore.text.toString().toInt() - 1).toString()
        }
    }
}