package app.point_counter.model

class PingPong: Sport() {

    //--------------------------------------------Pts--------------------------------------------
    override fun addPointToPlayer(player: Int) {
        if (player == 1){
            player1Pts++
        } else{
            player2Pts++
        }
        //If the player has 11 or more points with a difference of 2 wins the set and reset the score
        if (player1Pts >= 11 && player1Pts - player2Pts >= 2){
            addSetToPlayer(1)
            resetAllPts()
        }
        if (player2Pts >= 11 && player2Pts - player1Pts >= 2){
            addSetToPlayer(2)
            resetAllPts()
        }
    }

    override fun substractPointToPlayer(player: Int) {
        if (player == 1){
            if (player1Pts > 1) {
                player1Pts--
            }
        } else {
            if (player2Pts > 1) {
                player2Pts--
            }
        }
    }
    fun resetAllPts(){
        player1Pts = 0
        player2Pts = 0
    }

    //--------------------------------------------Sets--------------------------------------------

    override fun addSetToPlayer(player: Int) {
        if (player == 1){
            setPlayer1++
        } else{
            setPlayer2++
        }
    }

    override fun substractSetToPlayer(player: Int) {
        if (player == 1){
            if (setPlayer1 > 1)
                setPlayer1--
        } else{
            if (setPlayer1 > 1)
                setPlayer2--
        }
    }

    override fun getSets(): String {
        return "$setPlayer1 - $setPlayer2"
    }

    override fun getSetsPlayer(player: Int): Int {
        return if (player == 1){
            setPlayer1
        } else{
            setPlayer2
        }
    }

    //--------------------------------------------Score--------------------------------------------
    override fun resetScore() {
        player1Pts = 0
        player2Pts = 0
        setPlayer1 = 0
        setPlayer2 = 0
    }

    override fun setScore(player1Pts: Int,player2Pts: Int) {
        this.player1Pts=player1Pts
        this.player2Pts=player2Pts
    }

    override fun getScore(): String {
        return "$player1Pts - $player2Pts"
    }

    override fun getScorePlayer(player: Int): String {
        return if (player == 1){
            "$player1Pts set $setPlayer1"
        } else{
            "$player2Pts set $setPlayer2"
        }
    }

    override fun getSport(): String = "Ping Pong"

    //------------------------------------------nºsets to win--------------------------------------------

    override fun setToWin(setToWin: Int) {
        this.setToWin = setToWin
    }

    // 0 - No one wins / 1 - P1 wins / 2 - P2 wins
    override fun checkWin(): Int {
        if (setToWin == setPlayer1) {
            return 1
        } else if (setToWin == setPlayer2) {
            return 2
        } else {
            return 0
        }
    }
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