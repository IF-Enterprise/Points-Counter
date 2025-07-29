package app.point_counter.model

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.app.ActivityCompat

class VoiceCommand(
    private val activity: Activity,
    private val onCommandDetected: (Int) -> Unit
) {

    private val permissionsArray = arrayOf(Manifest.permission.RECORD_AUDIO)
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent

    private val commands: HashMap<String, Int> = hashMapOf(
        "SCORE" to 1,
        "SCORE RED" to 2,
        "SCORE BLUE" to 3,
        "SETS" to 4,
        "SETS RED" to 5,
        "SETS BLUE" to 6,
        "RESET" to 7,
        "ADD POINT" to 8,
        "SUBTRACT POINT" to 9,
        "ADD SET" to 10,
        "SUBTRACT SET" to 11
    )

    fun startListening() {
        // ✅ Pedimos permiso si hace falta
        if (ActivityCompat.checkSelfPermission(activity, permissionsArray[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionsArray, 200)
        }

        // ✅ Creamos SpeechRecognizer e intent
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity)
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US") // puedes poner "es-ES" si quieres español
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        // ✅ Listener completo
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d("VoiceCommand", "Listo para escuchar")
            }

            override fun onBeginningOfSpeech() {
                Log.d("VoiceCommand", "Detectando voz...")
            }

            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                Log.d("VoiceCommand", "Fin de la frase")
            }

            override fun onResults(results: Bundle?) {
                val ourResults = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!ourResults.isNullOrEmpty()) {
                    val spokenText = ourResults[0].uppercase()
                    Log.d("VoiceCommand", "Reconocido: $spokenText")

                    val commandCode = commands[spokenText]
                    if (commandCode != null) {
                        onCommandDetected(commandCode)
                    }
                }
                // 🔄 vuelve a escuchar inmediatamente
                speechRecognizer.startListening(speechRecognizerIntent)
            }

            override fun onError(error: Int) {
                Log.e("VoiceCommand", "Error: $error")
                // 🔄 reinicia aunque haya error (por ejemplo, silencio largo)
                speechRecognizer.startListening(speechRecognizerIntent)
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        // ✅ Comienza a escuchar
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    fun stopListening() {
        if (::speechRecognizer.isInitialized) {
            speechRecognizer.stopListening()
            speechRecognizer.destroy()
        }
    }
}
