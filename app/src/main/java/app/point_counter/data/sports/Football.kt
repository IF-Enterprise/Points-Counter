package app.point_counter.data.sports

import android.os.CountDownTimer
import app.point_counter.data.Sport

class Football: Sport() {
    override val rules: SportRules = SportRules(
        duration = 90
    )
    private var servingPlayer: Int = rules.playerServing //gets the player who initially serves
    var timer: CountDownTimer? = null

    override fun addPointToPlayer(player: Int) {
        score.addPts(player)
        if (player == 1)
            servingPlayer = 2
        else if (player == 2)
            servingPlayer = 1
    }

    override fun substractPointToPlayer(player: Int) {
        score.subPts(player)
    }

    override fun checkWin(): Int {
        //if the result its 1 player 1 wins if its 2 player 2 wins else no one wins
        return when {
            score.player1Sets == rules.setsToWin -> 1
            score.player2Sets == rules.setsToWin -> 2
            else -> 0
        }
    }

    fun reverseServingPlayer() {
        if (servingPlayer == 1){
            servingPlayer= 2
        }else{
            servingPlayer=1
        }
    }

    fun startTimer() {
        timer = object : CountDownTimer(halfDuration, 1000) { // tick every 1 second
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                // Half-time reached
                Toast.makeText(context, "Half-time!", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    fun pauseTimer() {
        timer?.cancel() // store remaining time if needed
    }

    fun resetTimer() {
        timer?.cancel()
        timerTextView.text = "45:00"
    }

    override fun getServingPlayer(): Int = servingPlayer

    override fun getSport(): String = "Football"
}