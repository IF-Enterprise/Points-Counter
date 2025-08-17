package app.point_counter.data

import kotlinx.serialization.Serializable

/*
    Score
    Class that manages the score of the game
*/
@Serializable
class Score(
    var player1Pts: Int = 0,
    var player2Pts: Int = 0,
    var player1Sets: Int = 0,
    var player2Sets: Int = 0
    ) {

    /* -------- GETTERS -------- */

    fun getSetsPlayer(player: Int): Int {
        if (player == 1) return player1Sets
        if (player == 2) return player2Sets
        return -1
    }

    fun getPtsPlayer(player: Int): Int {
        if (player == 1) return player1Pts
        if (player == 2) return player2Sets
        return -1
    }

    /* -------- SETTERS -------- */
    fun setSetsPlayer(player: Int, sets: Int) {
        if (player == 1) player1Sets = sets
        if (player == 2) player2Sets = sets
    }

    fun setPtsPlayer(player: Int, pts: Int) {
        if (player == 1) player1Sets = pts
        if (player == 2) player2Sets = pts
    }

    // addPts | add Points to x player default 1 pt
    fun addPts(player: Int, pts: Int = 1) {
        if (player == 1) player1Pts += pts
        if (player == 2) player2Pts += pts
    }

    // subPts | sub Points to x player default 1 pt
    fun subPts(player: Int, pts: Int = 1) {
        if (player == 1 && player1Pts > 0) player1Pts -= pts
        if (player == 2 && player2Pts > 0) player2Pts -= pts
    }

    // addSet | add set to x player and reset the points
    fun addSet(player: Int) {
        if (player == 1) player1Sets++
        if (player == 2) player2Sets++
        resetPts()
    }

    // subSet | sub set to x player
    fun subSet(player: Int) {
        if (player == 1 && player1Sets > 0) player1Sets--
        if (player == 2 && player2Sets > 0) player2Sets--
    }

    // resetPts | Set points to 0
    fun resetPts() {
        player1Pts = 0
        player2Pts = 0
    }

    // resetAll | Resets points and sets
    fun resetAll() {
        player1Pts = 0
        player2Pts = 0
        player1Sets = 0
        player2Sets = 0
    }

    // toStringPlayer | Return in terminal the pts and sets
    fun toStringPlayer(player: Int): String {
        if (player == 1){
            return "Pts: $player1Pts Set: $player1Sets"
        }else{
            return "Pts: $player2Pts Set: $player2Sets"
        }
    }
}
