package com.example.demo.repository

import com.example.demo.model.Dataset
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DatasetRepository : JpaRepository<Dataset, Long>