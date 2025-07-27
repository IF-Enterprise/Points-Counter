package app.point_counter.view

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
import app.point_counter.view.ScoreboardActivity

open class MainActivity : AppCompatActivity() {

    val scoreManager = ScoreViewModel()  // ahora es solo un objeto normal

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Sets the layout
        setContentView(R.layout.activity_main)

        //Find the button at activity_main.xml
        val button = findViewById<Button>(R.id.myButton)
        val ImageButton = findViewById<ImageButton>(R.id.my_image_button)

        button.setOnClickListener {
            scoreManager.setSport(PingPong())
            Toast.makeText(this, "¡Juego iniciado!", Toast.LENGTH_SHORT).show()
        }

        ImageButton.setOnClickListener {
            scoreManager.setSport(PingPong())
            val intent = Intent(this, ScoreboardActivity::class.java)
            intent.putExtra("sportType", "pingpong")//Its like passing the class PingPong() but with a string
            startActivity(intent)
        }
        /*
        ImageButton.setOnClickListener {
            scoreManager.setSport(Tenis())
            val intent = Intent(this, ScoreboardActivity::class.java)
            intent.putExtra("sportType", "tenis")
            startActivity(intent)
        }
        ImageButton.setOnClickListener {
            scoreManager.setSport(Padel())
            val intent = Intent(this, ScoreboardActivity::class.java)
            intent.putExtra("sportType", "padel")
            startActivity(intent)
        }

         */
    }
}
