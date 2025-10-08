package com.example.demo.model

import jakarta.persistence.*

@Entity
@Table(name = "data_point")
data class DataPoint(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "dataset_id", nullable = false)
    val dataset: Dataset,

    @Column(nullable = false)
    var x: Double,

    @Column(nullable = false)
    var y: Double
) {
    // Constructor para deserialización JSON con solo ID del dataset
    constructor(datasetId: Long, x: Double, y: Double) : this(0, Dataset(datasetId), x, y)
}

