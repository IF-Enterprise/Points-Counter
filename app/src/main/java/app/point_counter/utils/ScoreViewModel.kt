import android.content.Context
import androidx.lifecycle.ViewModel
import app.point_counter.data.ScoreRepository
import app.point_counter.data.Score
import app.point_counter.data.Sport

class ScoreViewModel: ViewModel() {
    // Actual app.point_counter.data.Sport
    private lateinit var sport: Sport
    // List of past games Score
    private val games = mutableListOf<Score>()

    /* -------- SETTERS -------- */
    fun setSport(typeSport: Sport) {
        sport = typeSport
    }

    fun setScore(player1Pts: Int, player2Pts: Int, setPlayer1:Int, setPlayer2:Int) {
        sport.setScore(player1Pts, player2Pts,setPlayer1, setPlayer2)
    }

    /* -------- GETTERS -------- */
    fun getSport(): String = sport.getSport()

    fun getPtsPlayer(player: Int): Int = sport.getPtsPlayer(player)

    fun getSetsPlayer(player: Int): Int = sport.getSetsPlayer(player)

    fun getGamesPlayer(player: Int): Int = sport.getGamesPlayer(player)

    fun getGames(): List<Score> = games

    /* -------- OTHER METHODS -------- */

    fun addPointToPlayer(player: Int) = sport.addPointToPlayer(player)

    fun subPointToPlayer(player: Int) = sport.substractPointToPlayer(player)

    fun toStringPlayer(player: Int): String = sport.toStringPlayer(player)

    fun resetScore() = sport.resetScore()

    fun checkWin(): Int = sport.checkWin()

    /* -------- GAMES MANAGER -------- */

    fun loadGames(context: Context) {
        games.clear()
        games.addAll(ScoreRepository.loadGames(context))
    }

    //Save a new game
    fun saveGame(context: Context, score: Score) {
        ScoreRepository.saveGame(context, score)
        games.add(score) // actualizar memoria
    }

    fun clearGames(context: Context) {
        ScoreRepository.clearGames(context)
        games.clear()
    }

    fun deleteGame(context: Context, score: Score) {
        ScoreRepository.deleteGame(context, score)
        games.remove(score) // actualizar memoria
    }
}