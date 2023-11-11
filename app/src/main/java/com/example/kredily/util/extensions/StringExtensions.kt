package com.example.kredily.util.extensions

fun String.maskEmail(): String {
    val parts = split('@')
    val username = parts[0][0] + "*".repeat(parts[0].length - 1)
    return "$username@${parts[1]}"
}

fun String.maskPhone(): String {
    val targetLength = length - 3
    return "*".repeat(targetLength) + substring(targetLength)
}