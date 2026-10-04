package com.example.model

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val speechLocale: String
) {
    ENGLISH("en", "English", "English", "en-IN"),
    HINDI("hi", "Hindi", "हिंदी", "hi-IN"),
    MARATHI("mr", "Marathi", "मराठी", "mr-IN");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}
