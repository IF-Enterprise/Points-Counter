import androidx.lifecycle.ViewModel

class ScoreViewModel: ViewModel() {
    private lateinit var sport: Sport

    fun setSport(typeSport: Sport) {
        sport = typeSport
    }

    fun getSport(): String = sport.getSport()

    fun addPointToPlayer(player: Int) = sport.addPointToPlayer(player)

    fun substractPointToPlayer(player: Int) = sport.substractPointToPlayer(player)

    fun getScore(): String = sport.getScore()

    fun getScorePlayer(player: Int): String = sport.getScorePlayer(player)

    fun resetScore() = sport.resetScore()

    fun setScore(player1Pts: Int, player2Pts: Int) = sport.setScore(player1Pts, player2Pts)

    fun setToWin(setToWin: Int) = sport.setToWin(setToWin)

    fun checkWin(): Int = sport.checkWin()

    fun getSetsPlayer(player: Int): Int = sport.getSetsPlayer(player)
}
