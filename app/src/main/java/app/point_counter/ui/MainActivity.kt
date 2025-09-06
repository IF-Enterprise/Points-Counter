package app.point_counter.ui

import ScoreViewModel
import SettingsDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.point_counter.R
import app.point_counter.data.sports.PingPong
import android.widget.ImageButton
import android.content.Intent
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.Button
import app.point_counter.data.ScoreRepository
import kotlin.math.abs
import app.point_counter.data.Score

open class MainActivity : AppCompatActivity() {
    val scoreManager = ScoreViewModel()  // ahora es solo un objeto normal
    private lateinit var imBtnPingPong: ImageButton
    private lateinit var imBtnTennis: ImageButton

    private lateinit var mediaPlayer: MediaPlayer//Music

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Image button that initializes the game (effects at clicking)
        imBtnPingPong = findViewById(R.id.my_image_button)
        setupHoverEffect()
        setupClickAction()

        // 🎶 Cargar la música desde res/raw
        mediaPlayer = MediaPlayer.create(this, R.raw.lofisweetsong265674)
        mediaPlayer.isLooping = true   // 🔁 Que suene en bucle
        mediaPlayer.start()

        movementRightFinger()

        buttonGames()

    }


    fun saveGameScore() {
        val score = Score(
            player1Pts = scoreManager.getPtsPlayer(1),
            player2Pts = scoreManager.getPtsPlayer(2),
            player1Sets = scoreManager.getSetsPlayer(1),
            player2Sets = scoreManager.getSetsPlayer(2),
        )
        print("saved at score: $score")

        // ✅ Llamamos al repositorio para guardarlo en JSON
        ScoreRepository.saveGame(this, score)
        println("Game saved to JSON")
    }

    private fun movementRightFinger() {
        // 👇 CONFIGURAMOS EL DETECTOR DE GESTOS
        gestureDetector = GestureDetector(this, object : SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 100   // distancia mínima para que se considere swipe
            private val SWIPE_VELOCITY_THRESHOLD = 100  // velocidad mínima

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null || e2 == null) return false

                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y

                if (abs(diffX) > abs(diffY) &&
                    abs(diffX) > SWIPE_THRESHOLD &&
                    abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
                ) {
                    if (diffX > 0) {
                        onSwipeRight()
                    }
                    return true
                }
                return false
            }
        })
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
    private fun onSwipeRight() {
        Toast.makeText(this, "¡Deslizado hacia la derecha!", Toast.LENGTH_SHORT).show()

        scoreManager.setSport(PingPong())
        startActivity(Intent(this, MainActivity::class.java).apply {
            //saveGameScore() No beacuse if you acces by the prev games sliding right then is saving the game
        })
    }


    //-----------------------------------------BUTTONS-----------------------------------------
    private fun setupHoverEffect() {
        imBtnPingPong.setOnHoverListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_HOVER_ENTER -> animateScale(v, 1.0f, 1.15f)
                MotionEvent.ACTION_HOVER_EXIT -> animateScale(v, 1.15f, 1.0f)
            }
            false
        }
    }

    private fun setupClickAction() {
        imBtnPingPong.setOnClickListener { v ->
            // Animación de rebote al hacer click
            animateClickBounce(v)

            // Cambiar deporte y navegar

            //Shows pop up of the SettingsDialog
            val settingsDialog = SettingsDialog.newInstance("pingpong")
            settingsDialog.show(supportFragmentManager, "SettingsDialog")
        }
    }

    private fun buttonGames(){
        val buttonGames = findViewById<Button>(R.id.button_games)
        buttonGames.setOnClickListener {
            startActivity(Intent(this, PrevGamesActivity::class.java))
        }
    }
    //-----------------------------------------Animations-----------------------------------------
    // Animación fluida de escala.
    private fun animateScale(view: View, from: Float, to: Float) {
        view.animate()
            .scaleX(to)
            .scaleY(to)
            .setDuration(150)
            .setInterpolator(OvershootInterpolator()) // efecto “spring”
            .start()
    }

    // Animación de rebote cuando clicas
    private fun animateClickBounce(view: View) {
        view.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(80)
            .withEndAction {
                view.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(120)
                    .setInterpolator(OvershootInterpolator())
                    .withEndAction {
                        view.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(100)
                            .start()
                    }
                    .start()
            }
            .start()
    }

    //-----------------------------------------MUSIC-----------------------------------------
    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()   // ⏸ Pausa cuando sales de la pantalla
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()   // ▶ Reanuda cuando vuelves
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // 🗑 Libera recursos cuando la Activity se destruye
    }
}
