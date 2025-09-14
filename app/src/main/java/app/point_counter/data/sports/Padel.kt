package app.point_counter.data.sports

import app.point_counter.data.Sport

/*
* Class Padel child from app.point_counter.data.Sport
*
* Points:   [00/15/30/40/AV] // AV = 50
* Games:
* Sets
 */
class Padel : Sport() {
    override val rules: SportRules = SportRules(
        setsToWin = 3, // 3
        gamesPerSet = 6, // diff 2
        diff2Sets = true,
        pointsPerGames = 4, // 0, 15, 30, 40, AV, Game
        hasTieBreak = true,
        tieBreakPoints = 7,
        tieBreak2Diff = true,
    )

    var tieBreak = false

    override fun addPointToPlayer(player: Int) {
        if (!tieBreak) {
            if (player == 1) {
                when (score.player1Pts) {
                    0, 15 -> score.addPts(1, 15)
                    30 -> score.addPts(1, 10) // 50 MEANS AV !!!
                    40 -> {
                        if (score.player2Pts == 50)
                            score.addPts(2, -10) // Both go back to 40
                        else
                            score.addPts(1, 10) // AV to Plr 1
                    }
                    50 ->
                    {
                        score.resetPts()
                        addGamesToPlayer(1)
                    }
                }
            } else if (player == 2) {
                when (score.player2Pts) {
                    0, 15 -> score.addPts(2, 15)
                    30 -> score.addPts(2, 10) // 50 MEANS AV !!!
                    40 -> {
                        if (score.player1Pts == 50)
                            score.addPts(1, -10) // Both go back to 40
                        else
                            score.addPts(2, 10) // AV to Plr 2
                    }
                    50 -> {
                        score.resetPts()
                        addGamesToPlayer(2)
                    }
                }
            }
        } else {
            addPointToPlayerTieBreak(player)
        }
    }

    fun addPointToPlayerTieBreak(player: Int) {
        score.addPts(player)
        if (player == 1) {
            if (score.player1Pts >= 7 && score.player1Pts - score.player2Pts >= 2) {
                score.addSet(1)
                score.resetGames()
                tieBreak = false
            }
        } else if (player == 2) {
            if (score.player2Pts >= 7 && score.player2Pts - score.player1Pts >= 2) {
                score.addSet(2)
                score.resetGames()
                tieBreak = false
            }
        }
    }

    override fun substractPointToPlayer(player: Int) {
        if (player == 1)
        {
            if (score.player1Pts > 0) {
                when (score.player1Pts) {
                    15, 30 -> score.subPts(1, 15)
                    40, 50 -> score.subPts(1, 10)
                }
            }
        }
        if (player == 2)
        {
            if (score.player2Pts > 0) {
                when (score.player2Pts) {
                    15, 30 -> score.subPts(2, 15)
                    40, 50 -> score.subPts(2, 10)
                }
            }
        }
    }

    fun addGamesToPlayer(player: Int) {
        if (player == 1) {
            score.addGames(1)
            if (score.player1Games == rules.gamesPerSet && score.player1Games - score.player2Games >= 2)
            {
                score.resetGames()
                if (checkWin() != 1)
                    score.addSet(1)
            }
        } else if (player == 2) {
            score.addGames(2)
            if (score.player2Games == rules.gamesPerSet && score.player2Games - score.player1Games >= 2)
            {
                score.resetGames()
                if (checkWin() != 2)
                    score.addSet(2)
            }
        }
        if (score.player1Games == 6 && score.player2Games == 6) {
            tieBreak = true
        }
    }

    override fun checkWin(): Int {
        if (score.player1Sets == rules.setsToWin && score.player1Games - score.player2Games >= 2)
            return 1
        else if (score.player2Sets == rules.setsToWin && score.player2Games - score.player1Games >= 2)
            return 2
        else
            return 0
    }

    override fun getSport(): String = "Padel"
}