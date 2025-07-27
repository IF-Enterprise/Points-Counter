package app.point_counter.viewmodel

import app.point_counter.model.Sport
import androidx.lifecycle.ViewModel
class ScoreViewModel:ViewModel() {//has to extend ViewModel to connect it to the UI
    private lateinit var sport: Sport

    fun setSport(typeSport: Sport) {
        sport = typeSport
    }

    fun addPointToPlayer(player: Int) {
        sport.addPointToPlayer(player)
    }
}