package com.webscare.numberplatemaker.util

import androidx.compose.ui.graphics.ImageBitmap
import com.webscare.numberplatemaker.domain.models.ExportFormat

expect suspend fun ImageBitmap.toByteArray(format: ExportFormat): ByteArray