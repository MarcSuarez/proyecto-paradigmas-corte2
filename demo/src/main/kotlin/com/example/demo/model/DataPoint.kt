package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "data_point")
data class DataPoint(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "dataset_id", nullable = false)
    @JsonBackReference("dataset-datapoints")
    val dataset: Dataset,

    @Column(nullable = false)
    var x: Double,

    @Column(nullable = false)
    var y: Double
) {
    // Constructor para deserialización JSON con solo ID del dataset
    constructor(datasetId: Long, x: Double, y: Double) : this(0, Dataset(datasetId), x, y)
}