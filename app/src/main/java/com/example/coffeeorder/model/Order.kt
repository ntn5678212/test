package com.example.coffeeorder.model

import java.io.Serializable

data class Order(
    val id: String,
    val customerName: String,
    val productName: String,
    val quantity: Int,
    val price: Double
) : Serializable {

    val totalPrice: Double
        get() = quantity * price
}