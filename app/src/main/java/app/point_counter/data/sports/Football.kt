package app.point_counter.data.sports

import android.os.CountDownTimer
import android.util.Log
import app.point_counter.data.Sport


class Football: Sport() {
    override val rules: SportRules = SportRules(
        duration = 90,
        halfDuration = 45,
        setsToWin = 0,
        pointsPerGames = 1
    )
    private var servingPlayer: Int = rules.playerServing //gets the player who initially serves

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




    override fun getServingPlayer(): Int = servingPlayer

    override fun getSport(): String = "Football"
}

