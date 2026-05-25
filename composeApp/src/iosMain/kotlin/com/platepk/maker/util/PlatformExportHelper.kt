package com.platepk.maker.util

import com.platepk.maker.domain.models.ExportFormat
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.cValue
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.NSDate
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.dataWithBytes
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.writeToFile
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsBeginPDFContextToFile
import platform.UIKit.UIGraphicsBeginPDFPageWithInfo
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsEndPDFContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation
import platform.UniformTypeIdentifiers.UTType

@OptIn(ExperimentalForeignApi::class)
actual class PlatformExportHelper {

    private fun makeRect(x: Double, y: Double, w: Double, h: Double) = cValue<CGRect> {
        origin.x = x
        origin.y = y
        size.width = w
        size.height = h
    }

    actual suspend fun saveImage(
        frontData: ByteArray,
        backData: ByteArray,
        format: ExportFormat
    ): String {
        val frontImage = UIImage.imageWithData(frontData.toNSData())!!
        val backImage = UIImage.imageWithData(backData.toNSData())!!

        val frontW = frontImage.size.useContents { width }
        val frontH = frontImage.size.useContents { height }
        val backW = backImage.size.useContents { width }
        val backH = backImage.size.useContents { height }

        val totalHeight = frontH + backH + 40.0
        val width = maxOf(frontW, backW)

        UIGraphicsBeginImageContextWithOptions(CGSizeMake(width, totalHeight), false, 0.0)
        frontImage.drawInRect(makeRect(0.0, 0.0, frontW, frontH))
        backImage.drawInRect(makeRect(0.0, frontH + 40.0, backW, backH))
        val combined = UIGraphicsGetImageFromCurrentImageContext()!!
        UIGraphicsEndImageContext()

        val ext = if (format == ExportFormat.PNG) "png" else "jpg"
        val path = NSTemporaryDirectory() + "plate_${NSDate().timeIntervalSince1970}.$ext"

        val imageData = when (format) {
            ExportFormat.PNG -> UIImagePNGRepresentation(combined)!!
            else -> UIImageJPEGRepresentation(combined, 0.95)!!
        }
        imageData.writeToFile(path, atomically = true)
        return path
    }

    actual suspend fun savePdf(
        frontData: ByteArray,
        backData: ByteArray,
        registrationNumber: String,
        vehicleType: String
    ): String {
        val frontImage = UIImage.imageWithData(frontData.toNSData())!!
        val backImage = UIImage.imageWithData(backData.toNSData())!!

        // --- PROFESSIONAL NAMING LOGIC ---
        val cleanRegNo = registrationNumber.trim().uppercase().replace("\\s+".toRegex(), "_")
        val cleanVehicleType = vehicleType.trim().uppercase()
        val timestamp = (NSDate().timeIntervalSince1970 * 1000).toLong()

        // Example: Plate_LEA_1234_PRIVATE_CAR_1715843459.pdf
        val fileName = "Plate_${cleanRegNo}_${cleanVehicleType}_$timestamp.pdf"
        val path = NSTemporaryDirectory() + fileName
        UIGraphicsBeginPDFContextToFile(path, cValue<CGRect>(), null)

        listOf(frontImage, backImage).forEach { image ->
            val w = image.size.useContents { width }
            val h = image.size.useContents { height }
            UIGraphicsBeginPDFPageWithInfo(makeRect(0.0, 0.0, w, h), null)
            image.drawInRect(makeRect(0.0, 0.0, w, h))
        }
        UIGraphicsEndPDFContext()
        return path
    }

    private fun ByteArray.toNSData(): NSData =
        this.usePinned { pinned ->
            NSData.dataWithBytes(pinned.addressOf(0), this.size.toULong())
        }
}

actual object MimeTypeHelper {
    actual fun getMimeType(filePath: String): String? {
        val url = NSURL.fileURLWithPath(filePath)
        val type = UTType.typeWithFilenameExtension(url.pathExtension ?: "")
        return type?.preferredMIMEType
    }
}