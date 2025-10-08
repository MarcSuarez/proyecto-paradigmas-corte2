package com.example.demo.controller

import com.example.demo.model.Dataset
import com.example.demo.service.DataSetService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/datasets")
class DataSetControllerRestController(private val dataSetService: DataSetService) {

    @PostMapping
    fun create(@RequestBody dataset: Dataset): ResponseEntity<Any> {
        return try {
            val created = dataSetService.create(dataset)
            ResponseEntity.status(HttpStatus.CREATED).body(created)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        }
    }

    @GetMapping
    fun index(): ResponseEntity<Any> {
        val datasets = dataSetService.getAll()
        return if (datasets.isEmpty()) {
            ResponseEntity.status(HttpStatus.OK).body(mapOf("message" to "No hay datasets registrados"))
        } else {
            ResponseEntity.ok(datasets)
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(dataSetService.getById(id))
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dataset: Dataset): ResponseEntity<Any> {
        return try {
            val updated = dataSetService.update(id, dataset)
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
            dataSetService.delete(id)
            ResponseEntity.ok(mapOf("message" to "Dataset eliminado correctamente"))
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }
}