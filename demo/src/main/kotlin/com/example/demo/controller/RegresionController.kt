package com.example.demo.controller

import com.example.demo.model.Regresion
import com.example.demo.service.RegresionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/regresiones")
class RegresionController(private val regresionService: RegresionService) {

    @PostMapping("/dataset/{datasetId}")
    fun create(@PathVariable datasetId: Long): ResponseEntity<Any> {
        return try {
            val created = regresionService.create(datasetId)
            ResponseEntity.status(HttpStatus.CREATED).body(created)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        }
    }

    @GetMapping
    fun index(): ResponseEntity<Any> {
        val regresiones = regresionService.getAll()
        return if (regresiones.isEmpty()) {
            ResponseEntity.status(HttpStatus.OK).body(mapOf("message" to "No hay regresiones registradas"))
        } else {
            ResponseEntity.ok(regresiones)
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(regresionService.getById(id))
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/dataset/{datasetId}")
    fun getByDatasetId(@PathVariable datasetId: Long): ResponseEntity<Any> {
        val regresion = regresionService.getByDatasetId(datasetId)
        return if (regresion == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("message" to "No hay regresión para el dataset $datasetId"))
        } else {
            ResponseEntity.ok(regresion)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            regresionService.delete(id)
            ResponseEntity.ok(mapOf("message" to "Regresión eliminada correctamente"))
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/dataset/{datasetId}")
    fun deleteByDatasetId(@PathVariable datasetId: Long): ResponseEntity<Any> {
        val regresion = regresionService.getByDatasetId(datasetId)
        return if (regresion == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("message" to "No hay regresión para el dataset $datasetId"))
        } else {
            regresionService.deleteByDatasetId(datasetId)
            ResponseEntity.ok(mapOf("message" to "Regresión del dataset $datasetId eliminada correctamente"))
        }
    }
}
