package com.webscare.numberplatemaker.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import com.webscare.numberplatemaker.domain.models.ExportFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.graphics.Paint

actual class PlatformExportHelper(private val context: Context) {

    actual suspend fun saveImage(
        frontData: ByteArray,
        backData: ByteArray,
        format: ExportFormat
    ): String = withContext(Dispatchers.IO) {
        try {
            val timestamp = System.currentTimeMillis()

            if (format == ExportFormat.PDF) {
                // Gallery mein save mat karo — sirf app directory mein PNG thumbnails
                val frontThumb = saveToAppDirectory(frontData, "thumb_front_$timestamp")
                val backThumb = saveToAppDirectory(backData, "thumb_back_$timestamp")
                "${frontThumb ?: ""}|${backThumb ?: ""}"
            } else {
                val frontPath = saveToGallery(frontData, "Plate_Front_$timestamp", format)
                val backPath = saveToGallery(backData, "Plate_Back_$timestamp", format)
                val fPath = frontPath ?: ""
                val bPath = backPath ?: ""
                if (fPath.isEmpty() && bPath.isEmpty()) {
                    throw Exception("Failed to save images to gallery")
                }
                "$fPath|$bPath"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private fun saveToGallery(data: ByteArray, fileName: String, format: ExportFormat): String? {
        val extension = if (format == ExportFormat.PNG) "png" else "jpg"
        val mimeType = if (format == ExportFormat.PNG) "image/png" else "image/jpeg"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.$extension")
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/NumberPlateMaker")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { stream ->
                stream.write(data)
            }
            return it.toString() // Yahan URI string return hogi
        }
        return null
    }

    actual suspend fun savePdf(
        frontData: ByteArray,
        backData: ByteArray,
        registrationNumber: String,
        vehicleType: String
    ): String = withContext(Dispatchers.IO) {
        try {
            val cleanRegNo = registrationNumber.trim().uppercase().replace("\\s+".toRegex(), "_")
            val cleanVehicleType = vehicleType.trim().uppercase()
            val timestamp = System.currentTimeMillis()
            val fileName = "Plate_${cleanRegNo}_${cleanVehicleType}_$timestamp.pdf"

            val pdfDocument = PdfDocument()
            val paint = Paint()

            val frontBitmap = BitmapFactory.decodeByteArray(frontData, 0, frontData.size)
            val backBitmap = BitmapFactory.decodeByteArray(backData, 0, backData.size)

            // Pages create karein... (Aapka purana logic yahan rahega)
            val frontPageInfo = PdfDocument.PageInfo.Builder(frontBitmap.width, frontBitmap.height, 1).create()
            val frontPage = pdfDocument.startPage(frontPageInfo)
            frontPage.canvas.drawBitmap(frontBitmap, 0f, 0f, paint)
            pdfDocument.finishPage(frontPage)

            val backPageInfo = PdfDocument.PageInfo.Builder(backBitmap.width, backBitmap.height, 2).create()
            val backPage = pdfDocument.startPage(backPageInfo)
            backPage.canvas.drawBitmap(backBitmap, 0f, 0f, paint)
            pdfDocument.finishPage(backPage)

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/NumberPlateMaker")
                put(MediaStore.MediaColumns.IS_PENDING, 1) // Ye line add karein
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }

                // IS_PENDING hata dein
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(it, contentValues, null, null)

                pdfDocument.close()
                frontBitmap.recycle()
                backBitmap.recycle()

                return@withContext it.toString() // Yahan URI return karein, Success message nahi
            }

            pdfDocument.close()
            throw Exception("Failed to save PDF")
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
    private fun saveToAppDirectory(data: ByteArray, fileName: String): String? {
        return try {
            val dir = java.io.File(context.filesDir, "plate_thumbnails")
            if (!dir.exists()) dir.mkdirs()
            val file = java.io.File(dir, "$fileName.png")
            file.outputStream().use { it.write(data) }
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}