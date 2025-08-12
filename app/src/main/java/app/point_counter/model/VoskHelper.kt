package com.yourpackage.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object VoskHelper {

    fun extractModel(context: Context, modelName: String): String {
        val modelDir = File(context.filesDir, modelName)

        try {
            // Eliminar si existe
            if (modelDir.exists()) {
                modelDir.deleteRecursively()
                Log.d("VOSK", "Deleted existing model directory")
            }

            // Crear directorio
            if (!modelDir.mkdirs()) {
                throw IOException("Failed to create model directory")
            }

            // Copiar recursivamente
            copyAssetFolder(context, modelName, modelDir)

            // Verificación de archivos requeridos
            val requiredFiles = listOf(
                "conf/mfcc.conf",
                "conf/model.conf",
                "graph/HCLr.fst",
                "graph/Gr.fst",
                "ivector/final.ie",
                "am/final.mdl"
            )
            requiredFiles.forEach { file ->
                if (!File(modelDir, file).exists()) {
                    throw IOException("Required model file '$file' is missing")
                }
            }

            return modelDir.absolutePath
        } catch (e: Exception) {
            modelDir.deleteRecursively()
            throw e
        }
    }

    private fun copyAssetFolder(context: Context, assetPath: String, destDir: File) {
        val assets = context.assets.list(assetPath) ?: return
        if (assets.isEmpty()) {
            // Es un archivo
            context.assets.open(assetPath).use { input ->
                FileOutputStream(destDir).use { output ->
                    input.copyTo(output)
                }
            }
        } else {
            // Es un directorio
            if (!destDir.exists()) destDir.mkdirs()
            for (file in assets) {
                copyAssetFolder(context, "$assetPath/$file", File(destDir, file))
            }
        }
    }
}
