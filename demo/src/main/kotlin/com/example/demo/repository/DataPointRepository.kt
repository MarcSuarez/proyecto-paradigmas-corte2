package com.example.demo.repository

import com.example.demo.model.DataPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DataPointRepository : JpaRepository<DataPoint, Long> {
    fun findByDatasetId(datasetId: Long): List<DataPoint>
}
