package com.yourpackage.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object VoskHelper {
    // In VoskHelper.kt
    fun extractModel(context: Context, modelName: String): String {
        val modelDir = File(context.filesDir, modelName)
        if (!modelDir.exists()) {
            modelDir.mkdirs()
            // Add verification
            val assets = context.assets.list(modelName)
                ?: throw IOException("Model '$modelName' not found in assets") as Throwable

            assets.forEach { asset ->
                File(modelDir, asset).outputStream().use { output ->
                    context.assets.open("$modelName/$asset").use { input ->
                        input.copyTo(output)
                    }
                }
            }
        }
        return modelDir.absolutePath
    }
}