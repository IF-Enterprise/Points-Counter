package app.point_counter.view

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.point_counter.R
import app.point_counter.model.PingPong
import app.point_counter.model.Sport
import app.point_counter.viewmodel.ScoreViewModel
import android.widget.ImageButton
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.speech.RecognitionListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.core.app.ActivityCompat
import app.point_counter.view.ScoreboardActivity
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

open class MainActivity : AppCompatActivity() {
    val scoreManager = ScoreViewModel()  // ahora es solo un objeto normal
    private lateinit var myButton: ImageButton

    private lateinit var mediaPlayer: MediaPlayer//Music

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myButton = findViewById(R.id.my_image_button)

        setupHoverEffect()
        setupClickAction()

        // 🎶 Cargar la música desde res/raw
        mediaPlayer = MediaPlayer.create(this, R.raw.lofisweetsong265674)
        mediaPlayer.isLooping = true   // 🔁 Que suene en bucle
        mediaPlayer.start()
    }

    //-----------------------------------------BUTTONS-----------------------------------------
    private fun setupHoverEffect() {
        myButton.setOnHoverListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_HOVER_ENTER -> animateScale(v, 1.0f, 1.15f)
                MotionEvent.ACTION_HOVER_EXIT -> animateScale(v, 1.15f, 1.0f)
            }
            false
        }
    }

    private fun setupClickAction() {
        myButton.setOnClickListener { v ->
            // Animación de rebote al hacer click
            animateClickBounce(v)

            // Cambiar deporte y navegar
            scoreManager.setSport(PingPong())
            startActivity(Intent(this, ScoreboardActivity::class.java).apply {
                putExtra("sportType", "pingpong")
            })
        }
    }

    //-----------------------------------------Animations-----------------------------------------
    // Animación fluida de escala
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
