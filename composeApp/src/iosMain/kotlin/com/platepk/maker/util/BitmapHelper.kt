package com.platepk.maker.util

import com.platepk.maker.domain.models.PlateConfig

actual fun PlateConfig.toBitmapByteArray(): ByteArray {
    // iOS ka native drawing logic yahan aayega
    // Return: UIImagePNGRepresentation(image)
    return TODO("Provide the return value")
}