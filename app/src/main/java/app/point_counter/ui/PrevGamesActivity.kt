package app.point_counter.ui

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import app.point_counter.R
import app.point_counter.data.Score

class PrevGamesActivity : MainActivity() {

    private lateinit var containerGames: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prevgames)

        //Contains all the games
        containerGames = findViewById(R.id.containerGames)

        setupButtons()

        // Load all the games in the view
        scoreManager.loadGames(this)
        val games = scoreManager.getGames()
        for ((index, game) in games.withIndex()) {
            addGameView(game, index)
        }
    }

    //-----------------------------------------BUTTONS-----------------------------------------
    private fun setupButtons() {
        // Botón Clear Games
        val btnClear = findViewById<TextView>(R.id.btnClearGames)
        btnClear.setOnClickListener {
            scoreManager.clearGames(this)
            containerGames.removeAllViews()
        }

        // Botón Back
        val btnBack = findViewById<TextView>(R.id.button_back_prevgames)
        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Opcional, para cerrar esta actividad
        }

    }
    private fun addGameView(game: Score, index: Int) {
        // Inflamos el layout personalizado
        val gameView = layoutInflater.inflate(R.layout.item_partida, containerGames, false)

        // Referenciamos los TextView para rellenarlos
        val tvTitulo = gameView.findViewById<TextView>(R.id.tvTituloPartida)
        tvTitulo.text = "Partida ${index + 1}"

        val tvDetalle = gameView.findViewById<TextView>(R.id.tvDetallePartida)
        tvDetalle.text = game.toStringPlayer(1)+" - "+game.toStringPlayer(2)

        val btnEliminar = gameView.findViewById<TextView>(R.id.btnDeleteGame)
        btnEliminar.setOnClickListener {
            scoreManager.deleteGame(this, game)
            containerGames.removeView(gameView)
        }


        //retoomar una partida
        ///////////////////////// //resume a game//////////////////////////////////////////
        gameView.setOnClickListener {
            //scoreManager.setScore(game.player1Pts, game.player2Pts, game.setPlayer1, game.setPlayer2)
            //startActivity(Intent(this, ScoreboardActivity::class.java))
        }

        // Añadimos la vista al contenedor
        containerGames.addView(gameView)
    }
}
