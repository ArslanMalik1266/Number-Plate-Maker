package com.platepk.maker.util

sealed class ExportResult {
    data object Loading : ExportResult()
    data class Success(val filePath: String) : ExportResult()
    data class Error(val message: String) : ExportResult()
}