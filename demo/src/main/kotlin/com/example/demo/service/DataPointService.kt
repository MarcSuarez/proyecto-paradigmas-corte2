package com.example.demo.service

import com.example.demo.dto.DataPointResponse
import com.example.demo.model.DataPoint
import com.example.demo.repository.DataPointRepository
import com.example.demo.repository.DatasetRepository
import org.springframework.stereotype.Service

@Service
class DataPointService(
    private val dataPointRepository: DataPointRepository,
    private val dataSetRepository: DatasetRepository,
    private val regresionService: RegresionService
) {

    fun create(dataPoint: DataPoint): DataPointResponse {
        validateDataPoint(dataPoint)
        
        // Si el dataset solo tiene ID, obtener el dataset completo de la base de datos
        val savedPoint = if (dataPoint.dataset.name.isEmpty() && dataPoint.dataset.id > 0) {
            val existingDataset = dataSetRepository.findById(dataPoint.dataset.id).orElseThrow {
                NoSuchElementException("Dataset con id ${dataPoint.dataset.id} no encontrado")
            }
            val dataPointWithFullDataset = DataPoint(
                id = dataPoint.id,
                dataset = existingDataset,
                x = dataPoint.x,
                y = dataPoint.y
            )
            dataPointRepository.save(dataPointWithFullDataset)
        } else {
            dataPointRepository.save(dataPoint)
        }
        
        // Recalcular la regresión si existe y obtenerla
        val updatedRegression = recalculateRegressionIfExists(dataPoint.dataset.id)
        
        return DataPointResponse(savedPoint, updatedRegression)
    }

    fun getAll(): List<DataPoint> {
        return dataPointRepository.findAll()
    }

    fun getById(id: Long): DataPoint {
        return dataPointRepository.findById(id).orElseThrow {
            NoSuchElementException("DataPoint con id $id no encontrado")
        }
    }

    fun getByDatasetId(datasetId: Long): List<DataPoint> {
        return dataPointRepository.findAll().filter { it.dataset.id == datasetId }
    }

    fun update(id: Long, newDataPoint: DataPoint): DataPointResponse {
        val existing = dataPointRepository.findById(id).orElseThrow {
            NoSuchElementException("DataPoint con id $id no existe")
        }

        validateDataPoint(newDataPoint)
        
        existing.x = newDataPoint.x
        existing.y = newDataPoint.y
        val updated = dataPointRepository.save(existing)
        
        // Recalcular la regresión si existe y obtenerla
        val updatedRegression = recalculateRegressionIfExists(existing.dataset.id)
        
        return DataPointResponse(updated, updatedRegression)
    }

    fun delete(id: Long): DataPointResponse? {
        val dataPoint = dataPointRepository.findById(id).orElseThrow {
            throw NoSuchElementException("DataPoint con id $id no existe")
        }
        val datasetId = dataPoint.dataset.id
        
        dataPointRepository.deleteById(id)
        
        // Recalcular la regresión si existe y obtenerla
        val updatedRegression = recalculateRegressionIfExists(datasetId)
        
        // Devolver null para el punto (fue eliminado) pero incluir la regresión actualizada
        return updatedRegression?.let { DataPointResponse(dataPoint, it) }
    }

    fun deleteByDatasetId(datasetId: Long) {
        val dataPoints = getByDatasetId(datasetId)
        dataPointRepository.deleteAll(dataPoints)
    }

    private fun validateDataPoint(dataPoint: DataPoint) {
        if (dataPoint.dataset.id == 0L) {
            throw IllegalArgumentException("El dataset del DataPoint debe estar especificado")
        }
        if (dataPoint.x.isNaN() || dataPoint.x.isInfinite()) {
            throw IllegalArgumentException("El valor de x debe ser un número válido")
        }
        if (dataPoint.y.isNaN() || dataPoint.y.isInfinite()) {
            throw IllegalArgumentException("El valor de y debe ser un número válido")
        }
    }
    
    /**
     * Recalcula la regresión lineal si existe una para el dataset dado.
     * Actualiza automáticamente los valores de la regresión existente.
     * @return La regresión actualizada o null si no existe o no se pudo calcular
     */
    private fun recalculateRegressionIfExists(datasetId: Long): com.example.demo.model.Regresion? {
        return try {
            // Verificar si existe una regresión para este dataset
            val existingRegression = regresionService.getByDatasetId(datasetId)
            
            if (existingRegression != null) {
                // Verificar que haya suficientes puntos para recalcular
                val dataPoints = dataPointRepository.findByDatasetId(datasetId)
                if (dataPoints.size >= 2) {
                    // El método create ahora actualiza automáticamente si ya existe
                    val updated = regresionService.create(datasetId)
                    println("✅ Regresión recalculada para dataset $datasetId")
                    updated
                } else {
                    // Si hay menos de 2 puntos, eliminar la regresión
                    regresionService.deleteByDatasetId(datasetId)
                    println("⚠️ Regresión eliminada: no hay suficientes puntos para dataset $datasetId")
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            // Si hay algún error al recalcular, solo lo registramos pero no fallar la operación principal
            println("⚠️ Error al recalcular regresión: ${e.message}")
            null
        }
    }
}
