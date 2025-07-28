package app.point_counter.view

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import app.point_counter.view.MainActivity
import app.point_counter.R
import android.util.Log
import app.point_counter.model.PingPong
import app.point_counter.view.ScoreboardActivity

class ScoreboardActivity : MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sportType = intent.getStringExtra("sportType")
        if (sportType == "pingpong") {
            scoreManager.setSport(PingPong())
        }

        setContentView(R.layout.activity_scoreboard)

        // References to the buttons and boxes
        val redBox: LinearLayout = findViewById(R.id.red_box)
        val blueBox: LinearLayout = findViewById(R.id.blue_box)

        val redScore: TextView = findViewById(R.id.red_score)
        val blueScore: TextView = findViewById(R.id.blue_score)

        val redPlus: Button = findViewById(R.id.red_plus)
        val redMinus: Button = findViewById(R.id.red_minus)
        val bluePlus: Button = findViewById(R.id.blue_plus)
        val blueMinus: Button = findViewById(R.id.blue_minus)

        // We add or substract points with the buttons and actualizes the scores.text
        redPlus.setOnClickListener {
            scoreManager.addPointToPlayer(1)
            redScore.text=scoreManager.getScorePlayer(1)
            blueScore.text=scoreManager.getScorePlayer(2)
        }

        redMinus.setOnClickListener {
            scoreManager.substractPointToPlayer(1)
            blueScore.text=scoreManager.getScorePlayer(2)
            redScore.text=scoreManager.getScorePlayer(1)
        }

        bluePlus.setOnClickListener {
            scoreManager.addPointToPlayer(2)
            redScore.text = scoreManager.getScorePlayer(1)
            blueScore.text = scoreManager.getScorePlayer(2)
        }

        blueMinus.setOnClickListener {
            scoreManager.substractPointToPlayer(2)
            redScore.text = scoreManager.getScorePlayer(1)
            blueScore.text = scoreManager.getScorePlayer(2)
        }
    }
}