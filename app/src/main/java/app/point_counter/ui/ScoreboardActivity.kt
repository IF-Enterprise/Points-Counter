package app.point_counter.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import app.point_counter.R
import app.point_counter.data.sports.Badminton
import app.point_counter.data.sports.PingPong
import app.point_counter.data.sports.Tennis
import app.point_counter.data.sports.Padel
import app.point_counter.data.sports.Voley
import app.point_counter.data.sports.Football
import com.yourpackage.utils.VoskHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.SpeechService
import org.vosk.android.RecognitionListener
import java.io.File
import java.io.IOException
import android.widget.ImageView

open class ScoreboardActivity : MainActivity() {

    private lateinit var model: Model
    private lateinit var recognizer: Recognizer

    private var speechService: SpeechService? = null

    //make them null in case we don't use them
    private var redScorePts: TextView? = null
    private var blueScorePts: TextView? = null

    private var redScoreSets: TextView? = null
    private var blueScoreSets: TextView? = null

    private var redScoreGames: TextView? = null
    private var blueScoreGames: TextView? = null

    private var serveRed: ImageView?=null
    private var serveBlue: ImageView?=null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Usamos la función en vez de código duplicado
        //checkAudioPermission()

        // Configuración inicial del juego
        val sportType = intent.getStringExtra("sportType")
        // val setsToWin = intent.getIntExtra("sets", 3) NO INTENTAR ENLAZARLO CON RULES

        if (sportType == "pingpong") {
            scoreManager.setSport(PingPong())
            setContentView(R.layout.activity_scoreboard_badminton)
            //scoreManager.setToWin(setsToWin)
        }else if(sportType == "tennis"){
            setContentView(R.layout.activity_scoreboard2)
            scoreManager.setSport(Tennis())
            //val gamesToWin = intent.getIntExtra("games", 0)
        }else if(sportType== "padel"){
            setContentView(R.layout.activity_scoreboard2)
            scoreManager.setSport(Padel())
        }else if (sportType == "badminton") {
            setContentView(R.layout.activity_scoreboard_badminton)
            scoreManager.setSport(Badminton())
        }else if (sportType == "voley") {
            setContentView(R.layout.activity_scoreboard2)
            scoreManager.setSport(Voley())
        }else if (sportType == "football"){
            setContentView(R.layout.activity_scoreboard)
            scoreManager.setSport(Football())
        }else if (sportType== "basketball"){
            setContentView(R.layout.activity_scoreboard2)
            //scoreManager.setSport(Basketball())
        }

        // Inicializar TEXTS VIEWS
        redScorePts = findViewById(R.id.red_pts)
        blueScorePts = findViewById(R.id.blue_pts)

        redScoreSets = findViewById(R.id.red_sets)
        blueScoreSets = findViewById(R.id.blue_sets)

        redScoreGames = findViewById(R.id.red_games)
        blueScoreGames = findViewById(R.id.blue_games)

        updateService()

        // Setup buttons ADD SUBSTRACT pts
        setupButtons()
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.red_plus).setOnClickListener {
            scoreManager.addPointToPlayer(1)
            updateScore()
        }
        findViewById<Button>(R.id.red_minus).setOnClickListener {
            scoreManager.subPointToPlayer(1)
            updateScore()
        }
        findViewById<Button>(R.id.blue_plus).setOnClickListener {
            scoreManager.addPointToPlayer(2)
            updateScore()
        }
        findViewById<Button>(R.id.blue_minus).setOnClickListener {
            scoreManager.subPointToPlayer(2)
            updateScore()
        }
    }

    private fun updateScore() {
        //updates the score (pts, game, sets)
        redScorePts?.text = scoreManager.getPtsPlayerString(1)
        blueScorePts?.text = scoreManager.getPtsPlayerString(2)

        redScoreSets?.text = scoreManager.getSetsPlayer(1).toString()
        blueScoreSets?.text = scoreManager.getSetsPlayer(2).toString()

        redScoreGames?.text = scoreManager.getGamesPlayer(1).toString()
        blueScoreGames?.text = scoreManager.getGamesPlayer(2).toString()


        updateService()

        // When the score is updated we check if someone won
        if (scoreManager.checkWin() == 1) {
            WinDialog.newInstance("Player 1").show(supportFragmentManager, "WinDialog")
            saveGameScore()
            speechService?.stop()
        } else if (scoreManager.checkWin() == 2) {
            WinDialog.newInstance("Player 2").show(supportFragmentManager, "WinDialog")
            saveGameScore()
            speechService?.stop()
        }

    }

    private fun updateService(){

        val serveRed = findViewById<ImageView>(R.id.serve_red)
        val serveBlue = findViewById<ImageView>(R.id.serve_blue)

        val serving = scoreManager.getServingPlayer()
        Log.d("Serving", "Serving player: $serving")

        // Reset de visibilidad
        serveRed.visibility = View.INVISIBLE
        serveBlue.visibility = View.INVISIBLE

        // Mostrar la bola en el jugador que saca
        if (serving == 1) {
            serveRed.visibility = View.VISIBLE
        } else if (serving == 2) {
            serveBlue.visibility = View.VISIBLE
        }
    }


    // ⚠️//CAUTION//⚠️                                             ⚠️//CAUTION//⚠️
    //////////////////////////////////////////VOSK///////////////////////////////////////////////////
    companion object {
        private const val RECORD_AUDIO_REQUEST_CODE = 1
    }

    // ✅ Nueva función para comprobar permisos
    private fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_REQUEST_CODE
            )
        } else {
            initializeVosk()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            initializeVosk()
        } else {
            Toast.makeText(this, "Permiso de micrófono requerido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeVosk() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Extraer modelo (ya con tu VoskHelper)
                val modelPath = VoskHelper.extractModel(this@ScoreboardActivity, "vosk_es")
                Log.d("VOSK", "Model extracted to: $modelPath")

                // Inicializar modelo
                model = Model(modelPath).also {
                    if (it.pointer == null) throw IOException("Model initialization failed")
                }

                recognizer = Recognizer(model, 16000.0f).also {
                    if (it.pointer == null) throw IOException("Recognizer initialization failed")
                }

                withContext(Dispatchers.Main) {
                    // ✅ Usar el constructor simple que solo necesita Recognizer y sampleRate
                    speechService = SpeechService(recognizer, 16000.0f)

                    // Iniciar reconocimiento
                    speechService?.startListening(recognitionListener)
                    Toast.makeText(
                        this@ScoreboardActivity,
                        "Voice recognition enabled",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val errorMsg = when (e) {
                        is IOException -> "Failed to load voice model: ${e.message}"
                        else -> "Unexpected error: ${e.message}"
                    }
                    Toast.makeText(this@ScoreboardActivity, errorMsg, Toast.LENGTH_LONG).show()
                    Log.e("VOSK", "Full initialization error", e)
                }
            }
        }
    }


    private fun handleVoiceCommand(command: String) {
        Log.d("VOSK", "Comando recibido: $command")
        when {
            command.contains("añadir") || command.contains("sumar") || command.contains("add") -> {
                scoreManager.addPointToPlayer(1)
                updateScore()
            }
            command.contains("quitar") || command.contains("restar") || command.contains("subtract") -> {
                scoreManager.subPointToPlayer(1)
                updateScore()
            }
            command.contains("reset") || command.contains("reiniciar") -> {
                scoreManager.resetScore()
                updateScore()
            }
            else -> runOnUiThread {
                Toast.makeText(this@ScoreboardActivity, "Comando no reconocido: $command", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Listener correctamente implementado para Vosk
    private val recognitionListener = object : RecognitionListener {
        override fun onResult(result: String) {
            try {
                val jsonResult = JSONObject(result)
                val recognizedText = jsonResult.getString("text").lowercase()
                handleVoiceCommand(recognizedText)
            } catch (e: Exception) {
                Log.e("VOSK", "Error processing voice result", e)
            }
        }

        override fun onFinalResult(hypothesis: String?) {
            Log.d("Vosk", "Resultado final: $hypothesis")
            Toast.makeText(this@ScoreboardActivity, "Reconocido: $hypothesis", Toast.LENGTH_SHORT).show()
        }

        override fun onPartialResult(partialResult: String) { }
        override fun onError(exception: Exception) {
            runOnUiThread {
                Toast.makeText(this@ScoreboardActivity, "Error en reconocimiento: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onTimeout() {
            runOnUiThread {
                Toast.makeText(this@ScoreboardActivity, "Tiempo de espera agotado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        runCatching { speechService?.stop() }
    }

    override fun onResume() {
        super.onResume()
        runCatching { speechService?.startListening(recognitionListener) }
    }

    override fun onDestroy() {
        super.onDestroy()
        runCatching { speechService?.stop() }
        runCatching { recognizer.close() }
        runCatching { model.close() }
    }


}
