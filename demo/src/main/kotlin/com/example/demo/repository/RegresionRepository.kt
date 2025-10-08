package com.example.demo.repository

import com.example.demo.model.Regresion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RegresionRepository : JpaRepository<Regresion, Long>