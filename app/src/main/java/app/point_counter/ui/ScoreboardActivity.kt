package app.point_counter.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import app.point_counter.R
import app.point_counter.data.PingPong
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

open class ScoreboardActivity : MainActivity() {

    private lateinit var model: Model
    private lateinit var recognizer: Recognizer
    private lateinit var speechService: SpeechService
    private lateinit var redScore: TextView
    private lateinit var blueScore: TextView

    // Listener correctamente implementado para Vosk
    private val recognitionListener = object : RecognitionListener {
        override fun onResult(result: String) {
            try {
                val jsonResult = JSONObject(result)
                val recognizedText = jsonResult.getString("text").toLowerCase()
                handleVoiceCommand(recognizedText)
            } catch (e: Exception) {
                Log.e("VOSK", "Error processing voice result", e)
            }

        }

        override fun onFinalResult(hypothesis: String?) {
            TODO("Not yet implemented")
        }

        override fun onPartialResult(partialResult: String) {
            // Opcional: manejar resultados parciales
        }

        override fun onError(exception: Exception) {
            runOnUiThread {
                Toast.makeText(this@ScoreboardActivity,
                    "Error en reconocimiento: ${exception.message}",
                    Toast.LENGTH_SHORT).show()
            }
        }

        override fun onTimeout() {
            runOnUiThread {
                Toast.makeText(this@ScoreboardActivity,
                    "Tiempo de espera agotado",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        } else {
            initializeVosk()
        }


        // Configuración inicial del juego
        val sportType = intent.getStringExtra("sportType")
        val setsToWin = intent.getIntExtra("sets", 3)
        if (sportType == "pingpong") {
            scoreManager.setSport(PingPong())
            scoreManager.setToWin(setsToWin)
        }

        setContentView(R.layout.activity_scoreboard)

        // Inicializar UI
        redScore = findViewById(R.id.red_score)
        blueScore = findViewById(R.id.blue_score)

        // Configurar botones
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeVosk()
        } else {
            Toast.makeText(this, "Permiso de micrófono requerido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeVosk() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Extraer modelo con verificación mejorada
                val modelPath = VoskHelper.extractModel(this@ScoreboardActivity, "vosk_es")
                Log.d("VOSK", "Model extracted to: $modelPath")

                // Verificar contenido del directorio (solo debug)
                val modelDir = File(modelPath)
                val files = modelDir.walk().toList()
                Log.d("VOSK", "Model contents: ${files.joinToString("\n") { it.absolutePath }}")

                // Inicializar modelo
                model = Model(modelPath).also {
                    if (it.pointer == null) throw IOException("Model initialization failed")
                }

                recognizer = Recognizer(model, 16000.0f).also {
                    if (it.pointer == null) throw IOException("Recognizer initialization failed")
                }

                withContext(Dispatchers.Main) {
                    speechService = SpeechService(recognizer, 16000.0f)
                    speechService.startListening(recognitionListener)
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

                    Toast.makeText(
                        this@ScoreboardActivity,
                        errorMsg,
                        Toast.LENGTH_LONG
                    ).show()

                    Log.e("VOSK", "Full initialization error", e)
                }
            }
        }
    }

    // ... (resto de los métodos permanecen igual)

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
            else -> {
                runOnUiThread {
                    Toast.makeText(
                        this@ScoreboardActivity,
                        "Comando no reconocido: $command",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateScore() {
        redScore.text = scoreManager.toStringPlayer(1)
        blueScore.text = scoreManager.toStringPlayer(2)

        if (scoreManager.checkWin() == 1) {
            val winDialog = WinDialog.newInstance("Player 1")
            winDialog.show(supportFragmentManager, "WinDialog")
            saveGameScore()
            speechService.stop() // Detener reconocimiento al ganar
        } else if (scoreManager.checkWin() == 2) {
            val winDialog = WinDialog.newInstance("Player 2")
            winDialog.show(supportFragmentManager, "WinDialog")
            saveGameScore()
            speechService.stop() // Detener reconocimiento al ganar
        }
    }

    override fun onPause() {
        super.onPause()
        runCatching { speechService.stop() }
    }

    override fun onResume() {
        super.onResume()
        runCatching { speechService.startListening(recognitionListener as RecognitionListener?) }
    }

    override fun onDestroy() {
        super.onDestroy()
        runCatching { speechService.stop() }
        runCatching { recognizer.close() }
        runCatching { model.close() }
    }
}