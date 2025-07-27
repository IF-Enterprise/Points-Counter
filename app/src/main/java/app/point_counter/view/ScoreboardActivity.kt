package app.point_counter.view

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.point_counter.R

class ScoreboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

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