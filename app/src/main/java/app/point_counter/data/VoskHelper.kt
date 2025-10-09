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
            // Limpiar directorio existente
            if (modelDir.exists()) {
                modelDir.deleteRecursively()
            }
            modelDir.mkdirs()

            Log.d("VOSK", "Extrayendo modelo: $modelName")

            // Listar y copiar todos los assets del modelo
            val assets = context.assets.list(modelName)
            if (assets.isNullOrEmpty()) {
                throw IOException("No assets found for model: $modelName")
            }

            assets.forEach { asset ->
                copyAssetRecursively(context, "$modelName/$asset", modelDir)
            }

            Log.d("VOSK", "Modelo extraído exitosamente")
            return modelDir.absolutePath

        } catch (e: Exception) {
            modelDir.deleteRecursively()
            throw IOException("Error extrayendo modelo: ${e.message}", e)
        }
    }

    private fun copyAssetRecursively(context: Context, assetPath: String, targetDir: File) {
        return try {
            // Intentar listar como directorio
            val contents = context.assets.list(assetPath)
            if (contents.isNullOrEmpty()) {
                // Es un archivo
                copySingleAsset(context, assetPath, targetDir)
            } else {
                // Es un directorio - crear subdirectorio y copiar contenido
                val subDir = File(targetDir, File(assetPath).name)
                subDir.mkdirs()
                contents.forEach { item ->
                    copyAssetRecursively(context, "$assetPath/$item", subDir)
                }
            }
        } catch (e: Exception) {
            // Si falla, intentar copiar como archivo
            copySingleAsset(context, assetPath, targetDir)
        }
    }

    private fun copySingleAsset(context: Context, assetPath: String, targetDir: File) {
        context.assets.open(assetPath).use { input ->
            val outputFile = File(targetDir, File(assetPath).name)
            outputFile.parentFile?.mkdirs()

            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
                Log.d("VOSK", "Copiado: ${outputFile.name}")
            }
        }
    }

    private fun debugModelStructure(modelPath: String) {
        val modelDir = File(modelPath)
        Log.d("VOSK", "=== ESTRUCTURA DEL MODELO EXTRAÍDO ===")
        Log.d("VOSK", "Directorio: $modelPath")

        if (!modelDir.exists()) {
            Log.e("VOSK", "❌ El directorio no existe")
            return
        }

        modelDir.walk().forEach { file ->
            val indent = "  ".repeat(file.relativeTo(modelDir).path.count { it == File.separatorChar })
            val type = if (file.isDirectory) "📁" else "📄"
            Log.d("VOSK", "$indent$type ${file.name}")
        }
    }

}
