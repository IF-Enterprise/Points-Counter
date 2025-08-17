package app.point_counter.data

import Sport

class PingPong : Sport() {

    override fun addPointToPlayer(player: Int) {
        score.addPts(player)

        // Regla de ping pong: si un jugador tiene 11 y diferencia de 2 → gana el set
        if (score.player1Pts >= 11 && score.player1Pts - score.player2Pts >= 2) {
            score.addSet(1)
        }
        if (score.player2Pts >= 11 && score.player2Pts - score.player1Pts >= 2) {
            score.addSet(2)
        }
    }

    override fun substractPointToPlayer(player: Int) {
        score.subPts(player)
    }

    override fun checkWin(): Int {
        //if the result its 1 player 1 wins if its 2 player 2 wins else no one wins
        return when {
            score.player1Sets == setToWin -> 1
            score.player2Sets == setToWin -> 2
            else -> 0
        }
    }

    override fun getSport(): String = "Ping Pong"
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
 */