package app.point_counter.data

import Sport

class Tennis : Sport() {
    override fun addPointToPlayer(player: Int) {
        if (player == 1) {
            when (score.player1Pts) {
                0, 15 -> score.addPts(1, 15)
                30, 40 -> score.addPts(1, 10) // 50 MEANS AV !!!
                50 -> score.addSet(1)
            }
        } else if (player == 2) {
            when (score.player2Pts) {
                0, 15 -> score.addPts(2, 15)
                30, 40 -> score.addPts(2, 10) // 50 MEANS AV !!!
                50 -> score.addSet(2)
            }
        }
    }

    override fun substractPointToPlayer(player: Int) {
        TODO("Not yet implemented")
    }

    override fun checkWin(): Int {
        TODO("Not yet implemented")
    }

    override fun getSport(): String = "Tennis"
}