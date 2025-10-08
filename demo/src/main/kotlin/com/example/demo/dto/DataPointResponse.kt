package com.example.demo.dto

import com.example.demo.model.DataPoint
import com.example.demo.model.Regresion

/**
 * DTO para la respuesta de operaciones CRUD de DataPoint.
 * Incluye el punto modificado y la regresión actualizada (si existe).
 */
data class DataPointResponse(
    val dataPoint: DataPoint,
    val regression: Regresion? = null
)
