package app.point_counter.viewmodel

import app.point_counter.model.Sport

class ScoreViewModel{
    private lateinit var sport: Sport

    fun setSport(typeSport: Sport) {
        sport = typeSport
    }

    fun addPointToPlayer(player: Int) {
        sport.addPointToPlayer(player)
    }
}