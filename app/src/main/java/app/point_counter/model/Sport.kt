package app.point_counter.model

abstract class Sport {
    abstract fun addPointToPlayer(player: Int)
    abstract fun getScore(): String
    abstract fun resetScore()
    abstract fun setScore(player1Pts: Int,player2Pts: Int)
    abstract fun getSport(): String
}