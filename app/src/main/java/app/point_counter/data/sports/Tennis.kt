package app.point_counter.data.sports

import app.point_counter.data.Sport


/*
* Class Tennis child from app.point_counter.data.Sport
*
* Points:   [00/15/30/40/AV] // AV = 50
* Games:
* Sets
 */
class Tennis : Sport() {
    override val rules: SportRules = SportRules(
        setsToWin = 3, // 3 | 5 Grand Slams
        pointsPerSet = 6,
        hasTieBreak = true,
        tieBreakPoints = 7,
        maxSets = 3
    )

    override fun addPointToPlayer(player: Int) {
        if (player == 1) {
            when (score.player1Pts) {
                0, 15 -> score.addPts(1, 15)
                30 -> score.addPts(1, 10) // 50 MEANS AV !!!
                40 ->
                    if (score.player2Pts == 50)
                        score.addPts(2, -10); // Both go back to 40
                    else
                        score.addPts(1, 10); // AV to Plr 1
                50 ->
                    score.addGames(1)
            }
        } else if (player == 2) {
            when (score.player2Pts) {
                0, 15 -> score.addPts(2, 15)
                30 -> score.addPts(2, 10) // 50 MEANS AV !!!
                40 ->
                    if (score.player1Pts == 50)
                        score.addPts(1, -10); // Both go back to 40
                    else
                        score.addPts(2, 10); // AV to Plr 2
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
    override fun setToWin(toWin: Int): Any {
        TODO("Not yet implemented")
    }
}

/*
[TENIS]
[PUNTUACIÓN]
- Juego (Game): 0 (Love) → 15 → 30 → 40 → Juego.
- Ventaja (AD): Solo si hay 40-40 ("Deuce").
  - Si el jugador con AD gana el punto: Gana el juego.
  - Si pierde: Vuelve a 40-40.

[SET]
- Gana el set el primero en llegar a 6 juegos con diferencia de 2.
  - Ejemplo: 6-4, 7-5.
- Si hay empate 6-6: Se juega tiebreak.

[TIEBREAK]
- Puntos normales (1, 2, 3...).
- Gana el primero en llegar a 7 puntos con diferencia de 2.
  - Ejemplo: 7-5, 8-6.

[PARTIDO]
- Mejor de 3 sets (gana quien gane 2 sets).
- En Grand Slams masculinos: Mejor de 5 sets (gana quien gane 3 sets).

[SUPER TIEBREAK (OPCIONAL)]
- Algunos torneos usan Super TieBreak en lugar del 3er set.
- Gana el primero en llegar a 10 puntos con diferencia de 2.
  - Ejemplo: 10-8.
 */