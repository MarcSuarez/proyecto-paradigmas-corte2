package com.example.demo.service

import com.example.demo.model.DataPoint
import com.example.demo.repository.DataPointRepository
import com.example.demo.repository.DatasetRepository
import org.springframework.stereotype.Service

@Service
class DataPointService(
    private val dataPointRepository: DataPointRepository,
    private val dataSetRepository: DatasetRepository
) {

    fun create(dataPoint: DataPoint): DataPoint {
        validateDataPoint(dataPoint)
        
        // Si el dataset solo tiene ID, obtener el dataset completo de la base de datos
        if (dataPoint.dataset.name.isEmpty() && dataPoint.dataset.id > 0) {
            val existingDataset = dataSetRepository.findById(dataPoint.dataset.id).orElseThrow {
                NoSuchElementException("Dataset con id ${dataPoint.dataset.id} no encontrado")
            }
            val dataPointWithFullDataset = DataPoint(
                id = dataPoint.id,
                dataset = existingDataset,
                x = dataPoint.x,
                y = dataPoint.y
            )
            return dataPointRepository.save(dataPointWithFullDataset)
        }
        
        return dataPointRepository.save(dataPoint)
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

    fun update(id: Long, newDataPoint: DataPoint): DataPoint {
        val existing = dataPointRepository.findById(id).orElseThrow {
            NoSuchElementException("DataPoint con id $id no existe")
        }

        validateDataPoint(newDataPoint)
        
        existing.x = newDataPoint.x
        existing.y = newDataPoint.y
        return dataPointRepository.save(existing)
    }

    fun delete(id: Long) {
        if (!dataPointRepository.existsById(id)) {
            throw NoSuchElementException("DataPoint con id $id no existe")
        }
        dataPointRepository.deleteById(id)
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
}
