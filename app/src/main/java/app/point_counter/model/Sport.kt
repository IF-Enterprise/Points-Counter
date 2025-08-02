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

    open fun setScore(player1Pts: Int, player2Pts: Int) {
        score.player1Pts = player1Pts
        score.player2Pts = player2Pts
    }

    open fun getScore(): String{
        return score.getScore()
    }

    open fun getScorePlayer(player: Int): String{
        return score.getScorePlayer(player)
    }

    open fun getSetsPlayer(player: Int): Int {
        return if (player == 1) score.setPlayer1 else score.setPlayer2
    }

    fun setToWin(setToWin: Int) {
        this.setToWin = setToWin
    }
}
