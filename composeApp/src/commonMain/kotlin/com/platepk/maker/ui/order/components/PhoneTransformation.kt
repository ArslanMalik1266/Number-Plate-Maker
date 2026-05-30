package com.platepk.maker.ui.order.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length > 10) text.text.substring(0, 10) else text.text
        val formatted = if (trimmed.startsWith("0")) trimmed.substring(1) else trimmed
        val out = "+92 $formatted"
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset + 4
            }
            override fun transformedToOriginal(offset: Int): Int {
                return if (offset > 4) offset - 4 else 0
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}