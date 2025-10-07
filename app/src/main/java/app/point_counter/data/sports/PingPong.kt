package app.point_counter.data.sports

import app.point_counter.data.Score
import app.point_counter.data.Sport

class PingPong() : Sport() {
    override val rules: SportRules = SportRules(
        setsToWin = 3,
        pointsPerSet = 6,
        hasTieBreak = true,
        tieBreakPoints = 7,
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

/*
REGLAMENTO
En el ping pong, un juego se gana al alcanzar 11 puntos, siempre y cuando haya una diferencia de al menos dos puntos sobre el oponente. Si el marcador llega a 10-10, el juego continúa hasta que un jugador obtenga una ventaja de dos puntos.
Detalles de la puntuación en tenis de mesa:
11 puntos para ganar: Un juego se gana cuando un jugador llega a 11 puntos primero.
Ventaja de dos puntos: Si ambos jugadores alcanzan 10 puntos, el juego continúa hasta que uno de ellos logre una ventaja de dos puntos (por ejemplo, 12-10, 13-11).
Saques alternados: Los jugadores sirven dos veces seguidas, cambiando el saque después de cada dos puntos, hasta que se llega al 10-10. A partir de ahí, se alternan los saques cada punto.
Partidos: Los partidos generalmente se juegan al mejor de 3, 5 o 7 sets.
Puntos: Un punto se gana cuando el oponente no logra devolver la pelota correctamente, ya sea porque la pelota rebota dos veces en su lado, no cruza la red, o sale de la mesa.


 SERVING
 Los cambios de saque cambian cada 2 pts pero cuando llega a 10-10 cambia a cada 1. Cuando un set es ganado cambia automaticamente el que ha ganado
 */