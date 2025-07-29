package app.point_counter.view

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import app.point_counter.R
import app.point_counter.model.PingPong
import app.point_counter.model.VoiceCommand

class ScoreboardActivity : MainActivity() {

    private lateinit var voiceCommand: VoiceCommand
    private lateinit var redScore: TextView
    private lateinit var blueScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sportType = intent.getStringExtra("sportType")
        if (sportType == "pingpong") {
            scoreManager.setSport(PingPong())
        }

        setContentView(R.layout.activity_scoreboard)

        // ✅ Referencias UI
        val redBox: LinearLayout = findViewById(R.id.red_box)
        val blueBox: LinearLayout = findViewById(R.id.blue_box)

        redScore = findViewById(R.id.red_score)
        blueScore = findViewById(R.id.blue_score)

        val redPlus: Button = findViewById(R.id.red_plus)
        val redMinus: Button = findViewById(R.id.red_minus)
        val bluePlus: Button = findViewById(R.id.blue_plus)
        val blueMinus: Button = findViewById(R.id.blue_minus)

        // ✅ Botones manuales
        redPlus.setOnClickListener {
            scoreManager.addPointToPlayer(1)
            updateScore()
        }

        redMinus.setOnClickListener {
            scoreManager.substractPointToPlayer(1)
            updateScore()
        }

        bluePlus.setOnClickListener {
            scoreManager.addPointToPlayer(2)
            updateScore()
        }

        blueMinus.setOnClickListener {
            scoreManager.substractPointToPlayer(2)
            updateScore()
        }

        // Inicializamos VoiceCommand
        voiceCommand = VoiceCommand(this) {
            commandCode -> handleVoiceCommand(commandCode)
        }
        voiceCommand.startListening()
    }

    private fun handleVoiceCommand(commandCode: Int) {
        when (commandCode) {
            8 -> { // ADD POINT
                scoreManager.addPointToPlayer(1)
                updateScore()
            }
            9 -> { // SUBTRACT POINT
                scoreManager.substractPointToPlayer(1)
                updateScore()
            }
            7 -> {
                scoreManager.resetScore()
                updateScore()
            }
        }
    }

    private fun updateScore() {
        redScore.text = scoreManager.getScorePlayer(1)
        blueScore.text = scoreManager.getScorePlayer(2)
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceCommand.stopListening() // ✅ Importante liberar recursos
    }
}
