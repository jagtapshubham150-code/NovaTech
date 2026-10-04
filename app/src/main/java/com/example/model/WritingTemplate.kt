package com.example.model

enum class WritingTemplate(
    val id: String,
    val iconName: String,
    val defaultPromptPrefix: String
) {
    WHATSAPP("whatsapp", "chat", "Write a concise, engaging WhatsApp message for: "),
    EMAIL("email", "mail", "Write a well-structured professional email including a subject line for: "),
    FORMAL_LETTER("letter", "description", "Write a standard formal letter with proper address placeholders and date for: "),
    APPLICATION("application", "assignment", "Write an official leave request or job/college application for: "),
    BUSINESS_MESSAGE("business", "business_center", "Write a polite and effective business inquiry/follow-up message for: "),
    SOCIAL_MEDIA("social", "share", "Write an engaging social media post caption with relevant hashtags for: "),
    THANK_YOU("thank_you", "favorite", "Write a warm, thoughtful thank you message for: "),
    CUSTOM("custom", "edit_note", "Write content following these instructions: ")
}

enum class WritingTone(val id: String) {
    FRIENDLY("Friendly"),
    PROFESSIONAL("Professional"),
    SHORT("Short"),
    POLITE("Polite"),
    FORMAL("Formal")
}
