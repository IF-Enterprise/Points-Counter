import app.point_counter.data.Score

/*
    Sport.kt
    Mother Class of all Sports, has basic function which are common with all the available sports.
*/
abstract class Sport {
    var setToWin = 3 // !!! Not all sports share that number
    val score = Score() // Calls the new class Score

    /* -------- SPORT SPECIFIC METHODS -------- */

    abstract fun addPointToPlayer(player: Int)
    abstract fun substractPointToPlayer(player: Int)
    abstract fun checkWin(): Int
    abstract fun getSport(): String

    /* -------- GETTERS -------- */

    open fun getPtsPlayer(player: Int): Int {
        if (player == 1) return score.player1Pts
        if (player == 2) return score.player2Pts
        return -1
    }

    open fun getSetsPlayer(player: Int): Int {
        if (player == 1) return score.player1Sets
        if (player == 2) return score.player2Sets
        return -1
    }

    /* -------- SETTERS -------- */

    open fun setScore(player1Pts: Int, player2Pts: Int, player1Sets: Int, player2Sets: Int) {
        score.player1Pts = player1Pts
        score.player2Pts = player2Pts
        score.player1Sets = player1Sets
        score.player2Sets = player2Sets
    }

    fun setToWin(setToWin: Int) {
        this.setToWin = setToWin
    }

    /* -------- SCORE FUNCTIONS -------- */
    open fun toStringPlayer(player : Int): String {
        return score.toStringPlayer(player)
    }

    open fun resetScore() = score.resetAll()
}
