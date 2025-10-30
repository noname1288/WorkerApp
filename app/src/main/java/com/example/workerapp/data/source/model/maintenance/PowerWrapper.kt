package com.example.workerapp.data.source.model.maintenance

data class PowerWrapper (
    val uid: String = "",
    val name: String = "",
    val price: Int = 0,
    val priceAction: Int = 0,
    var quantity: Int = 0,
    var quantityAction: Int = 0,
)