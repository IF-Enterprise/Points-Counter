package app.point_counter.data.sports

import app.point_counter.data.Sport

class Voley : Sport() {
    override val rules: SportRules = SportRules(
        setsToWin = 3, // 3 | 5 Grand Slams
        diff2Pts = true,
        pointsPerSet = 25,
    )
    private var servingPlayer: Int = rules.PlayerServing //gets the player who initially serves
    var ptsPerSet: Int = rules.pointsPerSet

    override fun addPointToPlayer(player: Int) {
        if (player == 1) {
            score.addPts(1)
            if (score.player1Pts >= ptsPerSet && score.player1Pts - score.player2Pts >= 2) {
                score.addSet(1)
                score.resetPts()
                servingPlayer=1
            }
        } else if (player == 2) {
            score.addPts(2)
            if (score.player2Pts >= ptsPerSet && score.player2Pts - score.player1Pts >= 2) {
                score.addSet(2)
                score.resetPts()
                servingPlayer=2
            }
        }
        if (score.player1Sets == 2 && score.player2Sets == 2)
            ptsPerSet = 15
    }

    override fun substractPointToPlayer(player: Int) {
        if (player == 1) {
            if (score.player1Pts > 0) {
                score.subPts(1)
                servingPlayer=2
            }
        } else if (player == 2) {
            if (score.player2Pts > 0) {
                score.subPts(2)
                servingPlayer=1
            }
        }
    }

    override fun checkWin(): Int {
        if (score.player1Sets == rules.setsToWin)
            return 1
        else if (score.player2Sets == rules.setsToWin)
            return 2
        else
            return 0
    }

    override fun getSport(): String = "Voley"

    override fun getServingPlayer(): Int = servingPlayer
}

/*
[VOLEIBOL]
[PUNTUACIÓN]
- Juego (Set): Los puntos se cuentan de forma simple: 1, 2, 3...
- Se juega a 25 puntos.
- Gana el set el primero en llegar a 25 con diferencia de 2.
  - Ejemplo: 25-20.
- Si hay empate a 24-24: Se debe ganar por diferencia de 2.
- No hay límite superior (puede acabar, por ejemplo, 32-30).

[SET]
- Cada set se juega a 25 puntos (con diferencia de 2).
- El 5º set (si es necesario) se juega a 15 puntos con diferencia de 2.

[PARTIDO]
- Mejor de 5 sets (gana quien gane 3 sets).
[Saque]
-Debera sacar el equipo que haya ganado el punto anterior


Serving
Rally Point
*/
