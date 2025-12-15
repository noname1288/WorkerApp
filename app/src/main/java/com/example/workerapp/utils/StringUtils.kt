package com.example.workerapp.utils

object StringUtils {
    fun extractIntroAndOutro(text: String): String {
        val lines = text.split("\n")

        if (lines.size != 1)
            return lines[0] + "\n" + lines[lines.size - 1]
        return lines[0]
    }
}
