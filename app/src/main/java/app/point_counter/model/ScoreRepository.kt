package app.point_counter.data

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import app.point_counter.model.Score
import java.io.File

object ScoreRepository {

    private const val FILE_NAME = "games.json"

    /**
     * Guarda una partida en el archivo JSON.
     */
    fun saveGame(context: Context, score: Score) {
        val file = File(context.filesDir, FILE_NAME)

        // ✅ Si el archivo no existe, lo creamos vacío con []
        if (!file.exists()) {
            file.writeText("[]")
        }

        // ✅ Leemos el contenido actual
        val json = file.readText()
        val list = try {
            Json.decodeFromString<MutableList<Score>>(json)
        } catch (e: Exception) {
            mutableListOf() // Si el JSON está corrupto, reiniciamos
        }

        // ✅ Añadimos la nueva partida
        list.add(score)

        // ✅ Guardamos de nuevo
        file.writeText(Json.encodeToString(list))
    }

    /**
     * Carga todas las partidas guardadas en el JSON.
     */
    fun loadGames(context: Context): MutableList<Score> {
        val file = File(context.filesDir, FILE_NAME)

        // ✅ Si no existe el archivo, creamos uno vacío y devolvemos lista vacía
        if (!file.exists()) {
            file.writeText("[]")
            return mutableListOf()
        }

        val json = file.readText()
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    /**
     * Borra todas las partidas (útil para debug o reinicio).
     */
    fun clearGames(context: Context) {
        val file = File(context.filesDir, FILE_NAME)
        if (file.exists()) {
            file.writeText("[]")
        }
    }
}
