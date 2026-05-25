package com.platepk.maker.domain.usecases

import com.platepk.maker.domain.models.PlateInputConfig

class EnforcePlateInputUseCase {
    fun enforceLetters(input: String, config: PlateInputConfig): String =
        input.filter { it.isLetter() }.uppercase().take(config.maxLetterCount)

    fun enforceNumbers(input: String, config: PlateInputConfig): String =
        input.filter { it.isDigit() }.take(config.maxNumberCount)
}