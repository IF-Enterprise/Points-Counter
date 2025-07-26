package app.point_counter.viewmodel

import app.point_counter.model.Sport

class ScoreViewModel {
    private lateinit var currentSport: Sport

    fun startGame(sport: Sport) {
        currentSport = sport
    }

    fun addPointTo(player: Int) {
        currentSport.addPointToPlayer(player)
    }
}