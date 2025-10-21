package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "regresion")
data class Regresion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @OneToOne
    @JoinColumn(name = "dataset_id", nullable = false, unique = true)
    @JsonBackReference("dataset-regression")
    val dataset: Dataset,

    @Column(nullable = false)
    var m: Double,   // pendiente

    @Column(nullable = false)
    var b: Double,  // intercepto

    @Column(nullable = true)
    var r2: Double? = null  // coeficiente de determinación opcional
)