package app.point_counter.model

class PingPong: Sport() {
    private var player1Pts = 0
    private var player2Pts = 0

    override fun addPointToPlayer(player: Int) {
        if (player == 1){
            player1Pts++
        } else{
            player2Pts++
        }
    }

    override fun resetScore() {
        player1Pts = 0
        player2Pts = 0
    }

    override fun setScore(player1Pts: Int,player2Pts: Int) {
        this.player1Pts=player1Pts
        this.player2Pts=player2Pts
    }

    override fun getScore(): String {
        return "$player1Pts - $player2Pts"
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