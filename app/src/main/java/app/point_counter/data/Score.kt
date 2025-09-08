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
    var player2Sets: Int = 0,
    var player1Games: Int = 0,
    var player2Games: Int = 0
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

    fun getGamesPlayer(player: Int): Int {
        if (player == 1) return player1Games
        if (player == 2) return player2Games
        return -1
    }

    /* -------- SETTERS -------- */
    fun setSetsPlayer(player: Int, sets: Int) {
        if (player == 1) player1Sets = sets
        if (player == 2) player2Sets = sets
    }

    fun setPtsPlayer(player: Int, pts: Int) {
        if (player == 1) player1Pts = pts
        if (player == 2) player2Pts = pts
    }

    fun setGamesPlayer(player: Int, games: Int) {
        if (player == 1) player1Games = games
        if (player == 2) player2Games = games
    }

    /* -------- ADD & SUB -------- */
    fun addPts(player: Int, pts: Int = 1) {
        if (player == 1 ) player1Pts += pts
        if (player == 2) player2Pts += pts
    }

    fun subPts(player: Int, pts: Int = 1) {
        if (player == 1 && player1Pts > 0) player1Pts -= pts
        if (player == 2 && player2Pts > 0) player2Pts -= pts
    }

    fun addSet(player: Int) {
        if (player == 1) player1Sets++
        if (player == 2) player2Sets++
        resetPts()
    }

    fun subSet(player: Int) {
        if (player == 1 && player1Sets > 0) player1Sets--
        if (player == 2 && player2Sets > 0) player2Sets--
    }

    fun addGames(player: Int) {
        if (player == 1) player1Games++
        if (player == 2) player2Games++
    }

    fun subGames(player: Int) {
        if (player == 1 && player1Games > 0) player1Games--
        if (player == 2 && player2Games > 0) player2Games--
    }

    /* -------- RESET -------- */
    fun resetPts() {
        player1Pts = 0
        player2Pts = 0
    }

    fun resetGames() {
        player1Pts = 0
        player2Pts = 0
        player1Games = 0
        player2Games = 0
    }

    fun resetAll() {
        player1Pts = 0
        player2Pts = 0
        player1Sets = 0
        player2Sets = 0
        player1Games = 0
        player2Games = 0
    }

    /* -------- MISC -------- */
    fun toStringPlayer(player: Int): String {
        if (player == 1){
            return "Pts: $player1Pts Games: $player1Games Set: $player1Sets"
        }else{
            return "Pts: $player2Pts Games: $player2Games Set: $player2Sets"
        }
    }
}
