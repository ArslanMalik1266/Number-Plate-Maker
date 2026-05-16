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

            // 1. Front Plate Save Karein
            val frontName = "Plate_Front_$timestamp"
            saveToGallery(frontData, frontName, format)

            // 2. Back Plate Save Karein (Dono alag files hongi)
            val backName = "Plate_Back_$timestamp"
            saveToGallery(backData, backName, format)

            "Success: Both plates saved to Gallery"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private fun saveToGallery(data: ByteArray, fileName: String, format: ExportFormat) {
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
        }
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

            // Final Name Example: Plate_LEA_1234_PRIVATE_CAR_1715843459.pdf
            val fileName = "Plate_${cleanRegNo}_${cleanVehicleType}_$timestamp.pdf"

            // 1. PdfDocument Create karein
            val pdfDocument = PdfDocument()
            val paint = Paint()

            // Byte array ko Bitmaps mein convert karein
            val frontBitmap = BitmapFactory.decodeByteArray(frontData, 0, frontData.size)
            val backBitmap = BitmapFactory.decodeByteArray(backData, 0, backData.size)

            // 2. Front Plate Page (Page 1)
            // Hum Bitmap ke size ke mutabiq page create karenge
            val frontPageInfo = PdfDocument.PageInfo.Builder(frontBitmap.width, frontBitmap.height, 1).create()
            val frontPage = pdfDocument.startPage(frontPageInfo)
            frontPage.canvas.drawBitmap(frontBitmap, 0f, 0f, paint)
            pdfDocument.finishPage(frontPage)

            // 3. Back Plate Page (Page 2)
            val backPageInfo = PdfDocument.PageInfo.Builder(backBitmap.width, backBitmap.height, 2).create()
            val backPage = pdfDocument.startPage(backPageInfo)
            backPage.canvas.drawBitmap(backBitmap, 0f, 0f, paint)
            pdfDocument.finishPage(backPage)

            // 4. MediaStore ke zariye save karein (Scoped Storage)
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/NumberPlateMaker")
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
            }

            pdfDocument.close()
            frontBitmap.recycle()
            backBitmap.recycle()

            "Success: PDF saved to Documents"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}