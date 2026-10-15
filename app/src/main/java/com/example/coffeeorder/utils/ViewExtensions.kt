package com.example.coffeeorder.utils

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import java.text.NumberFormat
import java.util.Locale

fun EditText.trimmedText(): String =
    text.toString().trim()

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Double.toVnd(): String {
    val format = NumberFormat.getNumberInstance(
        Locale.forLanguageTag("vi-VN")
    )
    return "${format.format(this)} VNĐ"
}