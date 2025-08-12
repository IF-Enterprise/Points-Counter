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
            // 1. Eliminar directorio existente si está corrupto
            if (modelDir.exists()) {
                modelDir.deleteRecursively()
                Log.d("VOSK", "Deleted existing model directory")
            }

            // 2. Crear directorio nuevo
            if (!modelDir.mkdirs()) {
                throw IOException("Failed to create model directory")
            }
            Log.d("VOSK", "Created model directory at ${modelDir.absolutePath}")

            // 3. Verificar assets
            val assetList = context.assets.list(modelName)
                ?: throw IOException("Model '$modelName' not found in assets")

            if (assetList.isEmpty()) {
                throw IOException("Model directory '$modelName' is empty in assets")
            }
            Log.d("VOSK", "Found ${assetList.size} files in assets")

            // 4. Copiar archivos recursivamente
            assetList.forEach { asset ->
                val assetPath = "$modelName/$asset"
                val destFile = File(modelDir, asset)

                if (isAssetDirectory(context, assetPath)) {
                    // Si es directorio, crear subdirectorio
                    if (!destFile.mkdirs()) {
                        throw IOException("Failed to create subdirectory $asset")
                    }
                    Log.d("VOSK", "Created subdirectory $asset")
                } else {
                    // Si es archivo, copiar
                    context.assets.open(assetPath).use { input ->
                        FileOutputStream(destFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    Log.d("VOSK", "Copied $asset (${destFile.length()} bytes)")
                }
            }

            // 5. Verificación estricta de archivos requeridos
            val requiredFiles = listOf(
                "conf/mfcc.conf",
                "conf/model.conf",
                "graph/HCLr.fst",
                "graph/gr/Gr.fst",
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
            // Limpieza en caso de error
            modelDir.deleteRecursively()
            Log.e("VOSK", "Error extracting model", e)
            throw e
        }
    }

    private fun isAssetDirectory(context: Context, path: String): Boolean {
        return try {
            context.assets.list(path)?.isNotEmpty() ?: false
        } catch (e: IOException) {
            false
        }
    }
}