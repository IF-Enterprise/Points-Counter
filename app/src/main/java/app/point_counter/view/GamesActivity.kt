package app.point_counter.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import app.point_counter.R
import app.point_counter.model.Score

class GamesActivity : MainActivity() {

    private lateinit var containerGames: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

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
        tvDetalle.text = game.toString()

        // Opcional: añadir listener para clicks
        gameView.setOnClickListener {
            // Aquí, por ejemplo, abrir detalles de la partida
        }

        // Añadimos la vista al contenedor
        containerGames.addView(gameView)
    }
}
