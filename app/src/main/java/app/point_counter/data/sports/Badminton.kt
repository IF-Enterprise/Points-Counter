package app.point_counter.data.sports

import app.point_counter.data.Sport

class Badminton : Sport() {
    override val rules: SportRules = SportRules(
        setsToWin = 2, // 3 | 5 Grand Slams
        diff2Pts = true,
        pointsPerSet = 21,
        maxPoints = 30,
        )

    override fun addPointToPlayer(player: Int) {
        if (player == 1) {
            score.addPts(1)
            if (score.player1Pts >= 21 && score.player1Pts - score.player2Pts >= 2) {
                score.addSet(1)
                score.resetPts()
            }
        } else if (player == 2) {
            score.addPts(2)
            if (score.player2Pts >= 21 && score.player2Pts - score.player1Pts >= 2) {
                score.addSet(2)
                score.resetPts()
            }
        }
    }

    override fun substractPointToPlayer(player: Int) {
        if (player == 1) {
            if (score.player1Pts > 0) {
                score.subPts(1)
            }
        } else if (player == 2) {
            if (score.player2Pts > 0) {
                score.subPts(2)
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

    override fun getSport(): String = "Badminton"
}

/*
[BÁDMINTON]
[PUNTUACIÓN]
- Juego (Game): Los puntos se cuentan de forma simple: 1, 2, 3...
- Se juega a 21 puntos.
- Gana el juego el primero en llegar a 21 con diferencia de 2.
  - Ejemplo: 21-18.
- Si hay empate a 20-20: Se debe ganar por diferencia de 2.
- Límite: el juego termina en 30 puntos (30-29 es el máximo posible).

[SET]
- Un set equivale a un juego ganado a 21 (con las condiciones anteriores).

[PARTIDO]
- Se juega al mejor de 3 sets (gana quien gane 2 sets).
*/
