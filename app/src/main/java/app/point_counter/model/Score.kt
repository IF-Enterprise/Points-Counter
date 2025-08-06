package app.point_counter.model
import kotlinx.serialization.Serializable
@Serializable
class Score(    var player1Pts: Int = 0,
                var player2Pts: Int = 0,
                var setPlayer1: Int = 0,
                var setPlayer2: Int = 0
    ) {

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

    fun getSetsPlayer(player: Int): Int {
        return if (player == 1) setPlayer1 else setPlayer2
    }

    fun getPtsPlayer(player: Int): Int {
        return if (player == 1) player1Pts else player2Pts
    }

    fun toStringPlayer(player: Int): String {
        if (player == 1){
            return "Pts: $player1Pts Set: $setPlayer1"
        }else{
            return "Pts: $player2Pts Set: $setPlayer2"
        }
    }
}
