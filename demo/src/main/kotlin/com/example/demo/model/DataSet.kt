package com.example.demo.model

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
    var regression: Regresion? = null
)