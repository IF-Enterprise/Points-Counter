import app.point_counter.model.Score

abstract class Sport {
    var setToWin = 3
    val score = Score() //Calls the new class Score

    //Specifical methods for each sport
    abstract fun addPointToPlayer(player: Int)
    abstract fun substractPointToPlayer(player: Int)
    abstract fun checkWin(): Int
    abstract fun getSport(): String

    // Commun methods for all sports
    open fun resetScore() = score.resetAll()

    open fun setScore(player1Pts: Int, player2Pts: Int, setPlayer1: Int, setPlayer2: Int) {
        score.player1Pts = player1Pts
        score.player2Pts = player2Pts
        score.setPlayer1 = setPlayer1
        score.setPlayer2 = setPlayer2
    }

    open fun getPtsPlayer(player: Int): Int {
        return if (player == 1) score.player1Pts else score.player2Pts
    }

    open fun toStringPlayer(player : Int): String {
        return score.toStringPlayer(player)
    }


    open fun getSetsPlayer(player: Int): Int {
        return if (player == 1) score.setPlayer1 else score.setPlayer2
    }

    fun setToWin(setToWin: Int) {
        this.setToWin = setToWin
    }
}
