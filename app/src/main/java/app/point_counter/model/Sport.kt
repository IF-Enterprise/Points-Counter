package app.point_counter.model

abstract class Sport() {
    abstract fun addPointToPlayer(player: Int)

    abstract fun substractPointToPlayer(player: Int)

    abstract fun addSetToPlayer(player: Int)

    abstract fun substractSetToPlayer(player: Int)

    abstract fun getSets(): String
    abstract fun getSetsPlayer(player: Int): Int

    abstract fun getScore(): String
    abstract fun resetScore()
    abstract fun setScore(player1Pts: Int,player2Pts: Int)
    abstract fun getSport(): String
    abstract fun getScorePlayer(player: Int): String

    abstract fun setToWin(setToWin: Int)
    abstract fun checkWin(): Boolean
}