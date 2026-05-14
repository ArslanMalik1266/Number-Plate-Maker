package com.webscare.numberplatemaker.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.webscare.numberplatemaker.domain.models.ExportFormat
import kotlin.use

actual class PlatformExportHelper(private val context: Context) {

    actual suspend fun saveImage(
        frontData: ByteArray,
        backData: ByteArray,
        format: ExportFormat
    ): String {
        val front = BitmapFactory.decodeByteArray(frontData, 0, frontData.size)
        val back = BitmapFactory.decodeByteArray(backData, 0, backData.size)

        val combined = Bitmap.createBitmap(
            maxOf(front.width, back.width),
            front.height + back.height + 40,
            Bitmap.Config.ARGB_8888
        )
        android.graphics.Canvas(combined).apply {
            drawColor(android.graphics.Color.WHITE)
            drawBitmap(front, 0f, 0f, null)
            drawBitmap(back, 0f, (front.height + 40).toFloat(), null)
        }

        val (ext, mime, compressFormat) = when (format) {
            ExportFormat.PNG -> Triple("png", "image/png", Bitmap.CompressFormat.PNG)
            else -> Triple("jpg", "image/jpeg", Bitmap.CompressFormat.JPEG)
        }

        val filename = "plate_${System.currentTimeMillis()}.$ext"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, mime)
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/NumberPlates")
        }
        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        )!!
        context.contentResolver.openOutputStream(uri)!!.use {
            combined.compress(compressFormat, 100, it)
        }
        return uri.toString()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    actual suspend fun savePdf(
        frontData: ByteArray,
        backData: ByteArray
    ): String {
        val front = BitmapFactory.decodeByteArray(frontData, 0, frontData.size)
        val back = BitmapFactory.decodeByteArray(backData, 0, backData.size)

        val filename = "plate_${System.currentTimeMillis()}.pdf"
        val contentValues = ContentValues().apply {
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, filename)
            put(MediaStore.Files.FileColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.Files.FileColumns.RELATIVE_PATH, "Documents/NumberPlates")
        }
        val uri = context.contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues
        )!!
        val document = PdfDocument()
        listOf(front to 1, back to 2).forEach { (bitmap, pageNum) ->
            val page = document.startPage(
                PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, pageNum).create()
            )
            page.canvas.drawBitmap(bitmap, 0f, 0f, null)
            document.finishPage(page)
        }
        context.contentResolver.openOutputStream(uri)!!.use { document.writeTo(it) }
        document.close()
        return uri.toString()
    }
}