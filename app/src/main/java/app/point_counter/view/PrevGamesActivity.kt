package app.point_counter.view

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import app.point_counter.R
import app.point_counter.model.Score

class PrevGamesActivity : MainActivity() {

    private lateinit var containerGames: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prevgames)

        containerGames = findViewById(R.id.containerGames)

        scoreManager.loadGames(this)
        val games = scoreManager.getGames()

        for ((index, game) in games.withIndex()) {
            addGameView(game, index)
        }
    }

    private fun addGameView(game: Score, index: Int) {
        // Inflamos el layout personalizado
        val gameView = layoutInflater.inflate(R.layout.item_partida, containerGames, false)

        // Referenciamos los TextView para rellenarlos
        val tvTitulo = gameView.findViewById<TextView>(R.id.tvTituloPartida)
        val tvDetalle = gameView.findViewById<TextView>(R.id.tvDetallePartida)

        tvTitulo.text = "Partida ${index + 1}"
        // Ajusta aquí la forma de obtener los sets y puntos desde Score
        tvDetalle.text = game.toStringPlayer(1)+" - "+game.toStringPlayer(2)

        // Opcional: añadir listener para clicks
        gameView.setOnClickListener {
            //scoreManager.setScore(game.player1Pts, game.player2Pts, game.setPlayer1, game.setPlayer2)
            //startActivity(Intent(this, ScoreboardActivity::class.java))
        }

        val btnEliminar = gameView.findViewById<TextView>(R.id.btnDeleteGame)
        btnEliminar.setOnClickListener {
            scoreManager.deleteGame(this, game)
            containerGames.removeView(gameView)
        }

        val btnClear = findViewById<TextView>(R.id.btnClearGames)
        btnClear.setOnClickListener {
            scoreManager.clearGames(this)
            containerGames.removeAllViews()
        }


        // Añadimos la vista al contenedor
        containerGames.addView(gameView)
    }
}
