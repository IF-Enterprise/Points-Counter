package app.point_counter.data

import java.lang.Math.random
import kotlin.random.Random

/*
    Sport.kt
    Mother Class of all Sports, has basic function which are common with all the available sports.
*/
abstract class Sport {
    val score = Score() // Calls the new class Score

    abstract val rules: SportRules?

    // Rules data class
    data class SportRules(
        var setsToWin: Int = 0,
        val pointsPerGames: Int = 0,
        val maxPoints: Int = 0,
        val gamesPerSet: Int = 0,
        val pointsPerSet: Int = 0,
        val diff2Pts: Boolean = false,
        val diff2Games: Boolean = false,
        val diff2Sets: Boolean = false,
        val hasTieBreak: Boolean = false,
        val tieBreakPoints: Int = 0,
        val tieBreak2Diff: Boolean = false,
        val playerServing: Int = Random.nextInt(1, 3),
        val duration: Int = 0
        val
    )

    /* -------- SPORT SPECIFIC METHODS -------- */

    abstract fun addPointToPlayer(player: Int)
    abstract fun substractPointToPlayer(player: Int)
    abstract fun checkWin(): Int
    abstract fun getSport(): String

    abstract fun getServingPlayer(): Int

    /* -------- GETTERS -------- */

    fun getSetsToWin(): Int {
        return rules!!.setsToWin
    }
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

    open fun getGamesPlayer(player: Int):Int {
        if (player == 1) return score.player1Games
        if (player == 2) return score.player2Games
        return -1
    }

    /* -------- SETTERS -------- */

    open fun setScore(player1Pts: Int, player2Pts: Int, player1Sets: Int, player2Sets: Int) {
        score.player1Pts = player1Pts
        score.player2Pts = player2Pts
        score.player1Sets = player1Sets
        score.player2Sets = player2Sets
    }

    /* -------- SCORE FUNCTIONS -------- */
    open fun toStringPlayer(player : Int): String {
        return score.toStringPlayer(player)
    }

    open fun resetScore() = score.resetAll()
}