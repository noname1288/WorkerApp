package com.example.workerapp.utils.ext

import java.text.NumberFormat
import java.util.Locale

fun Double.toVND() : String{
    return try{
        val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        formatter.format(this)
    } catch (e: Exception){
        "Invalid Number"
    }
}