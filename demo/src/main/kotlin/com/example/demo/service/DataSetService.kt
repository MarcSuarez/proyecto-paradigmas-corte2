package com.example.demo.service

import com.example.demo.model.Dataset
import com.example.demo.repository.DatasetRepository
import org.springframework.stereotype.Service

@Service
class DataSetService(private val datasetRepository: DatasetRepository) {

    fun create(dataset: Dataset): Dataset {
        if (dataset.name.isBlank()) {
            throw IllegalArgumentException("El nombre del dataset no puede estar vacío")
        }
        return datasetRepository.save(dataset)
    }

    fun getAll(): List<Dataset> {
        return datasetRepository.findAll()
    }

    fun getById(id: Long): Dataset {
        return datasetRepository.findById(id).orElseThrow {
            NoSuchElementException("Dataset con id $id no encontrado")
        }
    }

    fun update(id: Long, newDataset: Dataset): Dataset {
        val existing = datasetRepository.findById(id).orElseThrow {
            NoSuchElementException("Dataset con id $id no existe")
        }

        if (newDataset.name.isBlank()) {
            throw IllegalArgumentException("El nombre del dataset no puede estar vacío")
        }

        existing.name = newDataset.name
        return datasetRepository.save(existing)
    }

    fun delete(id: Long) {
        if (!datasetRepository.existsById(id)) {
            throw NoSuchElementException("Dataset con id $id no existe")
        }
        datasetRepository.deleteById(id)
    }
}