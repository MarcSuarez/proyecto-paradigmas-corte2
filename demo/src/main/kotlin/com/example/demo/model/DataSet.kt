package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
@Table(name = "dataset")
data class Dataset(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var name: String = "",

    @OneToOne(mappedBy = "dataset", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference("dataset-regression")
    var regression: Regresion? = null
) {
    @OneToMany(
        mappedBy = "dataset",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JsonManagedReference("dataset-datapoints")
    var dataPoints: MutableList<DataPoint> = mutableListOf()
}