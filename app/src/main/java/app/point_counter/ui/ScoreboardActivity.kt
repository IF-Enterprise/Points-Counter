package app.point_counter.ui

import SportTimer
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
import kotlinx.coroutines.delay

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

    private var timer: TextView? = null
    private var sportTimer = SportTimer(90, 45)

    private var serveRed: ImageView?=null
    private var serveBlue: ImageView?=null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Usamos la función en vez de código duplicado
        checkAudioPermission()

        // Configuración inicial del juego
        val sportType = intent.getStringExtra("sportType")

        val setsToWin = intent.getIntExtra("sets", 3)
        val gamesToWin = intent.getIntExtra("games", 3)
        val pointsToWin = intent.getIntExtra("points", 21)
        val timeToWin= intent.getIntExtra("time", 90)

        /* Falta implemnetar estos modificadores en el scoreManager -> Sport
        scoreManager.setSetsToWin(setsToWin)
        scoreManager.setGamesToWin(gamesToWin)
        scoreManager.setPointsToWin(pointsToWin)
        scoreManager.setTimeToWin(timeToWin)
         */

        if (sportType == "pingpong") {
            scoreManager.setSport(PingPong())
            setContentView(R.layout.activity_scoreboard_pingpong)
            //scoreManager.setToWin(setsToWin)
        }else if(sportType == "tennis"){
            setContentView(R.layout.activity_scoreboard_tennis)
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
            setContentView(R.layout.activity_scoreboard_football)
            scoreManager.setSport(Football())
            sportTimer.startTimer()
        }else if (sportType== "basketball"){
            setContentView(R.layout.activity_scoreboard_basket)
            scoreManager.setSport(Tennis())
            sportTimer.startTimer()
            //scoreManager.setSport(Basketball())
        }

        // Inicializar TEXTS VIEWS
        redScorePts = findViewById(R.id.red_pts)
        blueScorePts = findViewById(R.id.blue_pts)

        redScoreSets = findViewById(R.id.red_sets)
        blueScoreSets = findViewById(R.id.blue_sets)

        redScoreGames = findViewById(R.id.red_games)
        blueScoreGames = findViewById(R.id.blue_games)

        timer=findViewById(R.id.timer)

        //unstop the currency of the code
        lifecycleScope.launch {
            while (true) {
                updateTimer()
                delay(1000) // 1 segundo
            }
        }

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
    private fun updateTimer(){
        timer?.text = sportTimer.getTime()
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
                Log.d("VOSK", "Starting model extraction...")

                // Extraer modelo
                val modelPath = VoskHelper.extractModel(this@ScoreboardActivity, "vosk-model-small-es-0.42")
                Log.d("VOSK", "Model extracted to: $modelPath")

                // Verificar que el directorio existe y tiene archivos
                val modelDir = File(modelPath)
                if (!modelDir.exists() || modelDir.listFiles().isNullOrEmpty()) {
                    throw IOException("Model directory is empty or doesn't exist")
                }

                // Inicializar modelo con timeout
                model = Model(modelPath).also {
                    if (it.pointer == null) throw IOException("Model initialization failed")
                }

                recognizer = Recognizer(model, 16000.0f).also {
                    if (it.pointer == null) throw IOException("Recognizer initialization failed")
                }

                withContext(Dispatchers.Main) {
                    try {
                        speechService = SpeechService(recognizer, 16000.0f)
                        speechService?.startListening(recognitionListener)

                        Toast.makeText(
                            this@ScoreboardActivity,
                            "Reconocimiento de voz activado",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("VOSK", "Speech service started successfully")
                    } catch (e: Exception) {
                        Log.e("VOSK", "Failed to start speech service", e)
                        Toast.makeText(
                            this@ScoreboardActivity,
                            "Error starting voice recognition",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {
                Log.e("VOSK", "Vosk initialization failed", e)
                withContext(Dispatchers.Main) {
                    val errorMsg = when (e) {
                        is IOException -> "Error cargando modelo de voz: ${e.message}"
                        else -> "Error inesperado: ${e.message}"
                    }
                    Toast.makeText(this@ScoreboardActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    // Listener correctamente implementado para Vosk
    private val recognitionListener = object : RecognitionListener {
        override fun onResult(result: String) {
            try {
                val jsonResult = JSONObject(result)
                val recognizedText = jsonResult.getString("text").lowercase()
                Log.d("VOSK", "Texto reconocido: $recognizedText")
                handleVoiceCommand(recognizedText)
            } catch (e: Exception) {
                Log.e("VOSK", "Error procesando resultado", e)
            }
        }

        override fun onFinalResult(hypothesis: String?) {
            Log.d("VOSK", "Resultado final: $hypothesis")
            hypothesis?.let {
                Toast.makeText(this@ScoreboardActivity, "Reconocido: $it", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onPartialResult(partialResult: String) {
            // Procesar resultados parciales si lo deseas
        }

        override fun onError(exception: Exception) {
            Log.e("VOSK", "Error en reconocimiento", exception)
            runOnUiThread {
                Toast.makeText(this@ScoreboardActivity, "Error de voz: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onTimeout() {
            Log.d("VOSK", "Timeout de reconocimiento")
        }
    }

    private fun handleVoiceCommand(command: String) {
        Log.d("VOSK", "Procesando comando: $command")

        when {
            // Comandos en español
            command.contains("añadir") || command.contains("sumar") || command.contains("punto rojo") -> {
                scoreManager.addPointToPlayer(1)
                updateScore()
                Toast.makeText(this, "✅ Punto añadido - Rojo", Toast.LENGTH_SHORT).show()
            }
            command.contains("añadir azul") || command.contains("sumar azul") || command.contains("punto azul") -> {
                scoreManager.addPointToPlayer(2)
                updateScore()
                Toast.makeText(this, "✅ Punto añadido - Azul", Toast.LENGTH_SHORT).show()
            }
            command.contains("quitar") || command.contains("restar") || command.contains("quitar rojo") -> {
                scoreManager.subPointToPlayer(1)
                updateScore()
                Toast.makeText(this, "❌ Punto quitado - Rojo", Toast.LENGTH_SHORT).show()
            }
            command.contains("quitar azul") || command.contains("restar azul") -> {
                scoreManager.subPointToPlayer(2)
                updateScore()
                Toast.makeText(this, "❌ Punto quitado - Azul", Toast.LENGTH_SHORT).show()
            }
            command.contains("reiniciar") || command.contains("reset") -> {
                scoreManager.resetScore()
                updateScore()
                Toast.makeText(this, "🔄 Marcador reiniciado", Toast.LENGTH_SHORT).show()
            }

            // Comandos en inglés (por si usas vosk_en)
            command.contains("add") || command.contains("add red") -> {
                scoreManager.addPointToPlayer(1)
                updateScore()
                Toast.makeText(this, "✅ Point added - Red", Toast.LENGTH_SHORT).show()
            }
            command.contains("add blue") -> {
                scoreManager.addPointToPlayer(2)
                updateScore()
                Toast.makeText(this, "✅ Point added - Blue", Toast.LENGTH_SHORT).show()
            }
            command.contains("subtract") || command.contains("remove") -> {
                scoreManager.subPointToPlayer(1)
                updateScore()
                Toast.makeText(this, "❌ Point removed - Red", Toast.LENGTH_SHORT).show()
            }

            else -> {
                Log.d("VOSK", "Comando no reconocido: $command")
            }
        }
    }



    //---------------------------------------------------------------------------------------------
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
            // Verificar qué modelos están disponibles
            listAvailableModels()
            // Usar el modelo español que tienes
            initializeVosk("vosk_es")
        }
    }

    private fun listAvailableModels() {
        try {
            Log.d("VOSK", "=== VERIFICACIÓN RÁPIDA DE MODELOS ===")

            // Verificar directamente los modelos que esperas
            val expectedModels = listOf("vosk_es", "vosk_en")

            expectedModels.forEach { modelName ->
                try {
                    val exists = assets.list(modelName) != null
                    if (exists) {
                        Log.d("VOSK", "✅ MODELO ENCONTRADO: $modelName")
                        val contents = assets.list(modelName)
                        Log.d("VOSK", "   Carpetas/archivos: ${contents?.joinToString()}")
                    } else {
                        Log.d("VOSK", "❌ MODELO NO ENCONTRADO: $modelName")
                    }
                } catch (e: Exception) {
                    Log.d("VOSK", "❌ MODELO NO ENCONTRADO: $modelName - ${e.message}")
                }
            }

        } catch (e: Exception) {
            Log.e("VOSK", "Error verificando modelos: ${e.message}")
        }
    }
    private fun initializeVosk(modelName: String = "vosk_es") {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d("VOSK", "=== INICIANDO VOSK CON MODELO: $modelName ===")

                // Extraer modelo
                val modelPath = VoskHelper.extractModel(this@ScoreboardActivity, modelName)
                Log.d("VOSK", "Modelo extraído a: $modelPath")

                // Inicializar modelo
                model = Model(modelPath).also {
                    if (it.pointer == null) throw IOException("Model initialization failed")
                }
                Log.d("VOSK", "Modelo Vosk inicializado correctamente")

                recognizer = Recognizer(model, 16000.0f).also {
                    if (it.pointer == null) throw IOException("Recognizer initialization failed")
                }
                Log.d("VOSK", "Reconocedor inicializado correctamente")

                withContext(Dispatchers.Main) {
                    try {
                        speechService = SpeechService(recognizer, 16000.0f)
                        speechService?.startListening(recognitionListener)

                        Toast.makeText(
                            this@ScoreboardActivity,
                            "Voz activada - Modelo: ${modelName.uppercase()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("VOSK", "Servicio de voz iniciado")
                    } catch (e: Exception) {
                        Log.e("VOSK", "Error iniciando servicio", e)
                        Toast.makeText(
                            this@ScoreboardActivity,
                            "Error iniciando voz",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {
                Log.e("VOSK", "❌ INICIALIZACIÓN FALLIDA", e)
                withContext(Dispatchers.Main) {
                    val errorMsg = when (e) {
                        is IOException -> "Error con modelo '$modelName': ${e.message}"
                        else -> "Error: ${e.message}"
                    }
                    Toast.makeText(this@ScoreboardActivity, errorMsg, Toast.LENGTH_LONG).show()

                    // Opcional: intentar con el otro modelo
                    if (modelName == "vosk_es") {
                        Toast.makeText(this@ScoreboardActivity, "Intentando con modelo inglés...", Toast.LENGTH_SHORT).show()
                        initializeVosk("vosk_en")
                    }
                }
            }
        }
    }

}
