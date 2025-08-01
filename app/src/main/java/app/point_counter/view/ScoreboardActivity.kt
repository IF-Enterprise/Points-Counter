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
        val setsToWin = intent.getIntExtra("sets", 3)
        if (sportType == "pingpong") {
            scoreManager.setSport(PingPong())
            scoreManager.setToWin(setsToWin)
        }

        setContentView(R.layout.activity_scoreboard)

        redScore = findViewById(R.id.red_score)
        blueScore = findViewById(R.id.blue_score)

        val redPlus: Button = findViewById(R.id.red_plus)
        val redMinus: Button = findViewById(R.id.red_minus)
        val bluePlus: Button = findViewById(R.id.blue_plus)
        val blueMinus: Button = findViewById(R.id.blue_minus)

        // Botones manuales
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
        //check if player wins
        //then pop up dialog of win victory try again(show pop up settings) or select another game
        if (scoreManager.checkWin() == 1) {
            val winDialog = WinDialog.newInstance("Player 1")
            winDialog.show(supportFragmentManager, "WinDialog")
        } else if (scoreManager.checkWin() == 2) {
            val winDialog = WinDialog.newInstance("Player 2")
            winDialog.show(supportFragmentManager, "WinDialog")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceCommand.stopListening() // ✅ Importante liberar recursos
    }
}
