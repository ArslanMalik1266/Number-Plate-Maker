package com.platepk.maker.util

import androidx.compose.ui.graphics.ImageBitmap
import com.platepk.maker.domain.models.ExportFormat

expect suspend fun ImageBitmap.toByteArray(format: ExportFormat): ByteArray