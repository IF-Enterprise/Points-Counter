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

class MainActivity : AppCompatActivity() {

    private val scoreViewModel: ScoreViewModel by viewModels() //Initializes ViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Find the button at activity_main.xml
        val button = findViewById<Button>(R.id.myButton)

        // ✅ acción al hacer clic
        button.setOnClickListener {
            val typeSport = PingPong() // ← Aquí puedes crear el deporte por defecto
            scoreViewModel.startGame(typeSport)
            Toast.makeText(this, "¡Juego iniciado!", Toast.LENGTH_SHORT).show()
        }
    }
}
