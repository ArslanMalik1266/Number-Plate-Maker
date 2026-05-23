package com.webscare.numberplatemaker.util

import com.webscare.numberplatemaker.domain.models.PlateConfig

actual fun PlateConfig.toBitmapByteArray(): ByteArray {
    // iOS ka native drawing logic yahan aayega
    // Return: UIImagePNGRepresentation(image)
    return TODO("Provide the return value")
}