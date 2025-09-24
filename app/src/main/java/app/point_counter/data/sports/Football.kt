package app.point_counter.data.sports

import app.point_counter.data.Sport

class Football: Sport() {
    override val rules: SportRules = SportRules(

    )
    private var servingPlayer: Int = rules.playerServing //gets the player who initially serves
    private var timesServed:Int=0

    override fun addPointToPlayer(player: Int) {
        score.addPts(player)

        //The changes Serving its usually each 2 serves but when its over 10-10 it changes every point
        if (score.player1Pts >= 10 && score.player2Pts >= 10) {
            // Empate a 10 o más → saque cada punto
            changeServingPlayerAt(1)
        } else {
            // Antes del 10-10 → saque cada dos puntos
            changeServingPlayerAt(2)
        }

        // Regla de ping pong: si un jugador tiene 11 y diferencia de 2 → gana el set
        if (score.player1Pts >= 11 && score.player1Pts - score.player2Pts >= 2) {
            score.addSet(1)
            reverseServingPlayer()
        }
        if (score.player2Pts >= 11 && score.player2Pts - score.player1Pts >= 2) {
            score.addSet(2)
            reverseServingPlayer()
        }
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
    fun changeServingPlayerAt(remainingServes:Int) {
        timesServed++
        if (timesServed==remainingServes){
            reverseServingPlayer()
            timesServed=0
        }
    }
    fun reverseServingPlayer() {
        if (servingPlayer == 1){
            servingPlayer= 2
        }else{
            servingPlayer=1
        }
    }

    override fun getSport(): String = "Ping Pong"

    override fun getServingPlayer(): Int = servingPlayer
}