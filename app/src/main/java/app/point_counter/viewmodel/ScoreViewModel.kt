package app.point_counter.viewmodel

import app.point_counter.model.Sport
import androidx.lifecycle.ViewModel
class ScoreViewModel:ViewModel() {//has to extend ViewModel to connect it to the UI
    private lateinit var sport: Sport

    fun setSport(typeSport: Sport) {
        sport = typeSport
    }

    fun getSport(): String {
        return sport.getSport()
    }


    fun addPointToPlayer(player: Int) {
        sport.addPointToPlayer(player)
    }
    fun substractPointToPlayer(player: Int) {
        sport.substractPointToPlayer(player)
    }

    fun getScore(): String {
        return sport.getScore()
    }

    fun getScorePlayer(player: Int): String {
        return sport.getScorePlayer(player)
    }

    fun resetScore() {
        sport.resetScore()
    }

    fun setScore(player1Pts: Int, player2Pts: Int) {
        sport.setScore(player1Pts, player2Pts)
    }


}