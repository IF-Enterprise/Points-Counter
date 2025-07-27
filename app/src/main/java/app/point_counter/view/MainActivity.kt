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

class MainActivity : AppCompatActivity() {

    private val scoreViewModel: ScoreViewModel by viewModels() //Initializes ViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Sets the layout
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Find the button at activity_main.xml
        val button = findViewById<Button>(R.id.myButton)
        val ImageButton = findViewById<ImageButton>(R.id.my_image_button)

        button.setOnClickListener {
            scoreViewModel.setSport(PingPong())
            Toast.makeText(this, "¡Juego iniciado!", Toast.LENGTH_SHORT).show()
        }

        ImageButton.setOnClickListener {
            scoreViewModel.setSport(PingPong())
            val intent = Intent(this, ScoreboardActivity::class.java)
            startActivity(intent)
        }
    }
}
