import android.content.Context
import androidx.lifecycle.ViewModel
import app.point_counter.data.ScoreRepository
import app.point_counter.model.Score
class ScoreViewModel: ViewModel() {
    private lateinit var sport: Sport
    private val games = mutableListOf<Score>()//We create a list of games

    fun setSport(typeSport: Sport) {
        sport = typeSport
    }

    fun getSport(): String = sport.getSport()

    fun addPointToPlayer(player: Int) = sport.addPointToPlayer(player)

    fun substractPointToPlayer(player: Int) = sport.substractPointToPlayer(player)

    fun getPtsPlayer(player: Int): Int = sport.getPtsPlayer(player)

    fun getSetsPlayer(player: Int): Int = sport.getSetsPlayer(player)

    fun toStringPlayer(player: Int): String = sport.toStringPlayer(player)

    fun resetScore() = sport.resetScore()

    fun setScore(player1Pts: Int, player2Pts: Int) = sport.setScore(player1Pts, player2Pts)

    fun setToWin(setToWin: Int) = sport.setToWin(setToWin)

    fun checkWin(): Int = sport.checkWin()

    // --- Save and load games -----------------------

    //Load all games
    fun loadGames(context: Context) {
        games.clear()
        games.addAll(ScoreRepository.loadGames(context))
    }

    //Returns the list of games
    fun getGames(): List<Score> = games

    //Save a new game
    fun saveGame(context: Context, score: Score) {
        ScoreRepository.saveGame(context, score)
        games.add(score) // actualizar memoria
    }
}

