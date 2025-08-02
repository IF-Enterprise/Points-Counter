package app.point_counter.model

class Score {
    var player1Pts = 0
    var player2Pts = 0
    var setPlayer1 = 0
    var setPlayer2 = 0

    fun addPts(player: Int, pts: Int = 1) {
        if (player == 1) player1Pts += pts else player2Pts += pts
    }

    fun subPts(player: Int, pts: Int = 1) {
        if (player == 1 && player1Pts > 0) player1Pts -= pts
        if (player == 2 && player2Pts > 0) player2Pts -= pts
    }

    fun addSet(player: Int) {
        if (player == 1) setPlayer1++ else setPlayer2++
        resetPts()
    }

    fun subSet(player: Int) {
        if (player == 1 && setPlayer1 > 0) setPlayer1--
        if (player == 2 && setPlayer2 > 0) setPlayer2--
    }

    fun resetPts() {
        player1Pts = 0
        player2Pts = 0
    }

    fun resetAll() {
        player1Pts = 0
        player2Pts = 0
        setPlayer1 = 0
        setPlayer2 = 0
    }

    fun getScore(): String = "$player1Pts - $player2Pts"

    fun getSets(): String = "$setPlayer1 - $setPlayer2"

    fun getScorePlayer(player: Int): String {
        return if (player == 1) "$player1Pts (sets: $setPlayer1)" else "$player2Pts (sets: $setPlayer2)"
    }
}
