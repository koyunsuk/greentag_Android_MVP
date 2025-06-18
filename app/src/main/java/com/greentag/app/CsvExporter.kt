package com.greentag.app

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object CsvExporter {

    fun export(context: Context, data: List<CupReturn>): File? {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val fileName = "cups_${dateFormat.format(Date())}.csv"
        val exportDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "GreenTag")

        if (!exportDir.exists()) exportDir.mkdirs()

        val file = File(exportDir, fileName)

        try {
            FileWriter(file).use { writer ->
                writer.appendLine("UID,Alias,Status,Location,Size,Client,Timestamp")
                data.forEach {
                    writer.appendLine("${it.uid},${it.alias},${it.status},${it.location},${it.size},${it.customer},${it.timestamp}")
                }
            }
            Log.d("CSV_EXPORT", "✅ CSV 내보내기 완료: ${file.absolutePath}")
            return file
        } catch (e: Exception) {
            Log.e("CSV_EXPORT", "❌ CSV 내보내기 실패", e)
            return null
        }
    }
}
