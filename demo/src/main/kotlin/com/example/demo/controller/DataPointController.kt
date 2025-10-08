package com.example.demo.controller

import com.example.demo.model.DataPoint
import com.example.demo.service.DataPointService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/datapoints")
class DataPointController(private val dataPointService: DataPointService) {

    @PostMapping
    fun create(@RequestBody dataPoint: DataPoint): ResponseEntity<Any> {
        return try {
            val created = dataPointService.create(dataPoint)
            ResponseEntity.status(HttpStatus.CREATED).body(created)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/dataset/{id}")
    fun getDataPointsByDatasetID(@PathVariable id: Long): ResponseEntity<Any> {
        val dataPoints = dataPointService.getByDatasetId(id)

        return if (dataPoints.isEmpty()) {
            ResponseEntity.status(HttpStatus.OK).body(mapOf("message" to "No hay data points registrados para el dataset con id $id"))
        } else {
            ResponseEntity.ok(dataPoints)
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(dataPointService.getById(id))
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/dataset/{datasetId}")
    fun getByDatasetId(@PathVariable datasetId: Long): ResponseEntity<Any> {
        return try {
            val dataPoints = dataPointService.getByDatasetId(datasetId)
            return if (dataPoints.isEmpty()) {
                ResponseEntity.status(HttpStatus.OK).body(mapOf("message" to "No hay data points para el dataset $datasetId"))
            } else {
                ResponseEntity.ok(dataPoints)
            }
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dataPoint: DataPoint): ResponseEntity<Any> {
        return try {
            val updated = dataPointService.update(id, dataPoint)
            ResponseEntity.ok(updated)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            dataPointService.delete(id)
            ResponseEntity.ok(mapOf("message" to "DataPoint eliminado correctamente"))
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/dataset/{datasetId}")
    fun deleteByDatasetId(@PathVariable datasetId: Long): ResponseEntity<Any> {
        return try {
            dataPointService.deleteByDatasetId(datasetId)
            ResponseEntity.ok(mapOf("message" to "DataPoints del dataset $datasetId eliminados correctamente"))
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }
}
