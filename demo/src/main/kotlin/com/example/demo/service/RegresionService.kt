package com.example.demo.service

import com.example.demo.model.Regresion
import com.example.demo.repository.RegresionRepository
import com.example.demo.repository.DataPointRepository
import com.example.demo.repository.DatasetRepository
import org.springframework.stereotype.Service

@Service
class RegresionService(
    private val regresionRepository: RegresionRepository,
    private val dataPointRepository: DataPointRepository,
    private val datasetRepository: DatasetRepository
) {

    fun create(datasetId: Long): Regresion {
        // 1. Validar que el dataset exista
        val dataset = datasetRepository.findById(datasetId).orElseThrow {
            NoSuchElementException("El dataset con id $datasetId no existe")
        }

        // 2️ Verificar si ya existe una regresión para este dataset
        val existingRegression = regresionRepository.findAll().find { it.dataset.id == datasetId }
        
        // 3️ Obtener los data points del dataset
        val dataPoints = dataPointRepository.findByDatasetId(datasetId)
        if (dataPoints.isEmpty()) {
            throw IllegalArgumentException("No hay data points para calcular la regresión del dataset $datasetId")
        }
        if (dataPoints.size < 2) {
            throw IllegalArgumentException("Se necesitan al menos 2 data points para calcular la regresión lineal")
        }

        // 4️ Calcular sumatorias
        val n = dataPoints.size.toDouble()
        val sumX = dataPoints.sumOf { it.x }
        val sumY = dataPoints.sumOf { it.y }
        val sumXY = dataPoints.sumOf { it.x * it.y }
        val sumXX = dataPoints.sumOf { it.x * it.x }

        val denominator = n * sumXX - sumX * sumX
        if (denominator == 0.0) {
            throw IllegalArgumentException("Los puntos están alineados verticalmente. No se puede calcular la pendiente.")
        }

        // 5️ Calcular m, b y R²
        val m = (n * sumXY - sumX * sumY) / denominator
        val b = (sumY - m * sumX) / n
        val meanY = sumY / n
        val ssRes = dataPoints.sumOf { (it.y - (m * it.x + b)).let { r -> r * r } }
        val ssTot = dataPoints.sumOf { (it.y - meanY).let { d -> d * d } }
        val r2 = if (ssTot == 0.0) 1.0 else 1.0 - (ssRes / ssTot)

        // 6️ Crear o actualizar la regresión
        val regresion = if (existingRegression != null) {
            // Actualizar regresión existente
            existingRegression.m = m
            existingRegression.b = b
            existingRegression.r2 = r2
            println(" Actualizando regresión existente para dataset $datasetId")
            existingRegression
        } else {
            // Crear nueva regresión
            println("✨ Creando nueva regresión para dataset $datasetId")
            Regresion(
                dataset = dataset,
                m = m,
                b = b,
                r2 = r2
            )
        }

        return regresionRepository.save(regresion)
    }

    fun getAll(): List<Regresion> = regresionRepository.findAll()

    fun getById(id: Long): Regresion = regresionRepository.findById(id).orElseThrow {
        NoSuchElementException("Regresión con id $id no encontrada")
    }

    fun getByDatasetId(datasetId: Long): Regresion? =
        regresionRepository.findAll().find { it.dataset.id == datasetId }

    fun delete(id: Long) {
        if (!regresionRepository.existsById(id)) {
            throw NoSuchElementException("Regresión con id $id no existe")
        }
        regresionRepository.deleteById(id)
    }

    fun deleteByDatasetId(datasetId: Long) {
        val regresion = getByDatasetId(datasetId)
        if (regresion != null) {
            regresionRepository.deleteById(regresion.id)
        }
    }
}
