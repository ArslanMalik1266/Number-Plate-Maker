package com.platepk.maker.domain.usecases

import com.platepk.maker.domain.models.PlateInputConfig
import com.platepk.maker.domain.models.Province
import com.platepk.maker.domain.models.VehicleType

class GetPlateInputConfigUseCase {
    operator fun invoke(province: Province, vehicleType: VehicleType): PlateInputConfig {
        return plateInputConfigs[province to vehicleType]
            ?: PlateInputConfig(
                minLetterCount = 2,
                maxLetterCount = 4,
                minNumberCount = 1,
                maxNumberCount = 4,
                formatHint = "AB-ABCD 1-1234"
            )
    }

    private val plateInputConfigs = mapOf(
        // ─── PUNJAB ───────────────────────────────────────────────
        (Province.PUNJAB to VehicleType.MOTORBIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.PUNJAB to VehicleType.PRIVATE_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.PUNJAB to VehicleType.RICKSHAW) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.PUNJAB to VehicleType.HEAVY_TRANSPORT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.PUNJAB to VehicleType.GOVERNMENT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.PUNJAB to VehicleType.COMMERCIAL) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.PUNJAB to VehicleType.ELECTRIC_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.PUNJAB to VehicleType.ELECTRIC_BIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),

        // ─── SINDH ────────────────────────────────────────────────
        (Province.SINDH to VehicleType.MOTORBIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.SINDH to VehicleType.PRIVATE_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.SINDH to VehicleType.RICKSHAW) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.SINDH to VehicleType.HEAVY_TRANSPORT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.SINDH to VehicleType.GOVERNMENT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.SINDH to VehicleType.COMMERCIAL) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.SINDH to VehicleType.ELECTRIC_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.SINDH to VehicleType.ELECTRIC_BIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),

        // ─── ISLAMABAD (ICT) ──────────────────────────────────────
        (Province.ISLAMABAD to VehicleType.MOTORBIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.ISLAMABAD to VehicleType.PRIVATE_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.ISLAMABAD to VehicleType.RICKSHAW) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.ISLAMABAD to VehicleType.HEAVY_TRANSPORT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.ISLAMABAD to VehicleType.GOVERNMENT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.ISLAMABAD to VehicleType.COMMERCIAL) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.ISLAMABAD to VehicleType.ELECTRIC_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.ISLAMABAD to VehicleType.ELECTRIC_BIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.ISLAMABAD to VehicleType.DIPLOMATIC) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),

        // ─── KPK ──────────────────────────────────────────────────
        (Province.KPK to VehicleType.MOTORBIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.KPK to VehicleType.PRIVATE_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.KPK to VehicleType.RICKSHAW) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.KPK to VehicleType.HEAVY_TRANSPORT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.KPK to VehicleType.GOVERNMENT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.KPK to VehicleType.COMMERCIAL) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.KPK to VehicleType.ELECTRIC_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.KPK to VehicleType.ELECTRIC_BIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),

        // ─── BALOCHISTAN ──────────────────────────────────────────
        (Province.BALOCHISTAN to VehicleType.MOTORBIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.BALOCHISTAN to VehicleType.PRIVATE_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.BALOCHISTAN to VehicleType.RICKSHAW) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.BALOCHISTAN to VehicleType.HEAVY_TRANSPORT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.BALOCHISTAN to VehicleType.GOVERNMENT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.BALOCHISTAN to VehicleType.COMMERCIAL) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.BALOCHISTAN to VehicleType.ELECTRIC_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.BALOCHISTAN to VehicleType.ELECTRIC_BIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),

        // ─── AJK ──────────────────────────────────────────────────
        (Province.AJK to VehicleType.MOTORBIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.AJK to VehicleType.PRIVATE_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.AJK to VehicleType.RICKSHAW) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.AJK to VehicleType.HEAVY_TRANSPORT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.AJK to VehicleType.GOVERNMENT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.AJK to VehicleType.COMMERCIAL) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.AJK to VehicleType.ELECTRIC_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.AJK to VehicleType.ELECTRIC_BIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),

        // ─── GILGIT BALTISTAN (GB) ────────────────────────────────
        (Province.GB to VehicleType.MOTORBIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.GB to VehicleType.PRIVATE_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.GB to VehicleType.RICKSHAW) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.GB to VehicleType.HEAVY_TRANSPORT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.GB to VehicleType.GOVERNMENT) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.GB to VehicleType.COMMERCIAL) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.GB to VehicleType.ELECTRIC_CAR) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
        (Province.GB to VehicleType.ELECTRIC_BIKE) to PlateInputConfig(2, 4, 1, 4, "AB-ABCD 1-1234"),
    )
}