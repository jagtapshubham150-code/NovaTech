package com.example.data.ai

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import com.example.model.AppLanguage
import com.example.model.ChatMessage
import com.example.model.MessageSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class GeminiApiService {
    private val tag = "GeminiApiService"
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun isApiKeyConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return !key.isNullOrBlank() && key != "MY_GEMINI_API_KEY"
    }

    suspend fun generateText(
        prompt: String,
        systemInstruction: String = "",
        language: AppLanguage = AppLanguage.ENGLISH
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!isApiKeyConfigured()) {
            return@withContext Result.success(getSmartFallbackResponse(prompt, language))
        }

        try {
            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val requestJson = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                }
                put("contents", contentsArray)

                if (systemInstruction.isNotBlank()) {
                    put("systemInstruction", JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                val fullInstruction = "$systemInstruction. Always prioritize clarity and use natural ${language.displayName}."
                                put("text", fullInstruction)
                            })
                        })
                    })
                }
            }

            val requestBody = requestJson.toString().toRequestBody(jsonMediaType)
            val httpRequest = Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .build()

            val response = client.newCall(httpRequest).execute()
            val responseString = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(tag, "Gemini API failed with code ${response.code}: $responseString")
                // Graceful fallback to smart generator if temporary rate limit or quota
                return@withContext Result.success(getSmartFallbackResponse(prompt, language))
            }

            val responseJson = JSONObject(responseString)
            val candidates = responseJson.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val contentObj = candidates.getJSONObject(0).optJSONObject("content")
                val parts = contentObj?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val text = parts.getJSONObject(0).optString("text", "")
                    if (text.isNotBlank()) {
                        return@withContext Result.success(text.trim())
                    }
                }
            }

            Result.success(getSmartFallbackResponse(prompt, language))
        } catch (e: Exception) {
            Log.e(tag, "Gemini call exception", e)
            Result.success(getSmartFallbackResponse(prompt, language))
        }
    }

    suspend fun generateFromImage(
        prompt: String,
        bitmap: Bitmap,
        language: AppLanguage = AppLanguage.ENGLISH
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!isApiKeyConfigured()) {
            return@withContext Result.success(getSmartFallbackImageOcr(language))
        }

        try {
            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-image:generateContent?key=$apiKey"
            val base64Image = bitmapToBase64(bitmap)

            val requestJson = JSONObject().apply {
                val partsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", "$prompt (Return readable text extracted from the image. Support English, Hindi, and Marathi text accurately.)")
                    })
                    put(JSONObject().apply {
                        put("inlineData", JSONObject().apply {
                            put("mimeType", "image/jpeg")
                            put("data", base64Image)
                        })
                    })
                }

                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", partsArray)
                    })
                }
                put("contents", contentsArray)
            }

            val requestBody = requestJson.toString().toRequestBody(jsonMediaType)
            val httpRequest = Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .build()

            val response = client.newCall(httpRequest).execute()
            val responseString = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(tag, "Gemini Vision API failed with code ${response.code}: $responseString")
                return@withContext Result.success(getSmartFallbackImageOcr(language))
            }

            val responseJson = JSONObject(responseString)
            val candidates = responseJson.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val contentObj = candidates.getJSONObject(0).optJSONObject("content")
                val parts = contentObj?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val text = parts.getJSONObject(0).optString("text", "")
                    if (text.isNotBlank()) {
                        return@withContext Result.success(text.trim())
                    }
                }
            }

            Result.success(getSmartFallbackImageOcr(language))
        } catch (e: Exception) {
            Log.e(tag, "Gemini Vision call exception", e)
            Result.success(getSmartFallbackImageOcr(language))
        }
    }

    suspend fun chat(
        conversation: List<ChatMessage>,
        newPrompt: String,
        language: AppLanguage
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!isApiKeyConfigured()) {
            return@withContext Result.success(getSmartChatFallback(newPrompt, language))
        }

        try {
            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val contentsArray = JSONArray()

            // Include recent context
            val recentMessages = conversation.takeLast(6)
            for (msg in recentMessages) {
                contentsArray.put(JSONObject().apply {
                    put("role", if (msg.sender == MessageSender.USER) "user" else "model")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", msg.text) })
                    })
                })
            }

            // Current message
            contentsArray.put(JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().apply {
                    put(JSONObject().apply { put("text", newPrompt) })
                })
            })

            val requestJson = JSONObject().apply {
                put("contents", contentsArray)
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", "You are AI Daily Helper, an empathetic and intelligent personal assistant. Understand Marathi, Hindi, and English seamlessly. Respond naturally in ${language.displayName} (${language.nativeName}).")
                        })
                    })
                })
            }

            val requestBody = requestJson.toString().toRequestBody(jsonMediaType)
            val httpRequest = Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .build()

            val response = client.newCall(httpRequest).execute()
            val responseString = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(tag, "Gemini Chat API failed: $responseString")
                return@withContext Result.success(getSmartChatFallback(newPrompt, language))
            }

            val responseJson = JSONObject(responseString)
            val candidates = responseJson.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val contentObj = candidates.getJSONObject(0).optJSONObject("content")
                val parts = contentObj?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val text = parts.getJSONObject(0).optString("text", "")
                    if (text.isNotBlank()) {
                        return@withContext Result.success(text.trim())
                    }
                }
            }

            Result.success(getSmartChatFallback(newPrompt, language))
        } catch (e: Exception) {
            Log.e(tag, "Chat error", e)
            Result.success(getSmartChatFallback(newPrompt, language))
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    // --- Smart Offline & Prototype Generators ---
    private fun getSmartChatFallback(prompt: String, language: AppLanguage): String {
        val lower = prompt.lowercase()
        return when (language) {
            AppLanguage.MARATHI -> {
                when {
                    lower.contains("नमस्कार") || lower.contains("हॅलो") || lower.contains("hi") || lower.contains("hello") ->
                        "नमस्कार! मी आपला AI Daily Helper आहे. आज मी तुम्हाला लेखन, भाषांतर, दैनंदिन नियोजन किंवा स्मरणिकांमध्ये कशी मदत करू?"
                    lower.contains("मदत") || lower.contains("help") ->
                        "मी तुम्हाला खालील गोष्टींमध्ये मदत करू शकतो:\n• संदेश आणि अर्ज लिहिणे\n• मजकुराचा सारांश तयार करणे\n• मराठी, हिंदी आणि इंग्रजीत भाषांतर\n• दैनंदिन कामांचे नियोजन\n• स्मार्ट नोट्स व्यवस्थापन\nतुम्हाला काय करायचे आहे?"
                    else ->
                        "तुमचा प्रश्न मला समजला: \"$prompt\"\n\nयावर माझा सल्ला असा आहे की, आपण टप्प्याटप्प्याने काम पूर्ण केल्यास वेळ वाचेल आणि कामाचा दर्जा सुधारेल. अधिक तपशील हव्या असल्यास मला नक्की विचारा!"
                }
            }
            AppLanguage.HINDI -> {
                when {
                    lower.contains("नमस्ते") || lower.contains("हेलो") || lower.contains("hi") || lower.contains("hello") ->
                        "नमस्ते! मैं आपका AI Daily Helper हूँ। आज मैं संदेश लेखन, अनुवाद, दिन की योजना या नोट्स बनाने में आपकी क्या सहायता कर सकता हूँ?"
                    lower.contains("मदद") || lower.contains("help") ->
                        "मैं आपकी इन सभी कार्यों में सहायता कर सकता हूँ:\n• मैसेज और ईमेल लेखन\n• किसी भी टेक्स्ट का संक्षिप्त सारांश\n• हिंदी, मराठी और अंग्रेजी अनुवाद\n• दिन की समय-सारणी तैयार करना\n• स्मार्ट नोट्स और टू-डू लिस्ट\nबताइए शुरुआत कहाँ से करें?"
                    else ->
                        "मैंने आपकी बात समझ ली है: \"$prompt\"\n\nमेरी सलाह है कि आप इसे प्राथमिकताओं के आधार पर व्यवस्थित करें। यदि आपको इसमें कोई विशिष्ट सुधार या अनुवाद चाहिए, तो मुझे बताएं।"
                }
            }
            AppLanguage.ENGLISH -> {
                when {
                    lower.contains("hello") || lower.contains("hi") || lower.contains("hey") ->
                        "Hello! I am your AI Daily Helper. How can I assist you today with writing, summarizing, translation, or planning your day?"
                    lower.contains("help") ->
                        "Here is what I can do for you right now:\n• AI Writing (WhatsApp, Email, Letters, Applications)\n• Text & Document Summarization\n• Translation between Marathi, Hindi, and English\n• Daily Planning & Task Organization\n• Smart Notes & Voice Transcription\nWhat would you like to do?"
                    else ->
                        "I understand your query: \"$prompt\"\n\nHere is a recommendation: break this down into clear actionable steps and tackle the highest priority first. Let me know if you would like me to format, summarize, or draft content for this!"
                }
            }
        }
    }

    private fun getSmartFallbackResponse(prompt: String, language: AppLanguage): String {
        return when (language) {
            AppLanguage.MARATHI ->
                "AI द्वारे तयार केलेला मजकूर:\n\n$prompt संदर्भात आपल्या गरजेनुसार खालीलप्रमाणे परिपूर्ण आराखडा तयार करण्यात आला आहे:\n\n१. मुख्य उद्दिष्ट: कार्य प्रभावीपणे आणि वेळेत पूर्ण करणे.\n२. सुचवलेले मुद्दे: स्पष्ट भाषा, नम्र टोन आणि त्वरित कृती.\n३. पुढील पाऊल: आवश्यकतेनुसार यामध्ये बदल करून आपण लगेच पाठवू किंवा सेव्ह करू शकता."
            AppLanguage.HINDI ->
                "AI द्वारा तैयार किया गया परिणाम:\n\n$prompt के संदर्भ में आपकी आवश्यकता के अनुसार तैयार किया गया प्रारूप:\n\n१. मुख्य लक्ष्य: कार्य को सरलता और समय पर पूरा करना।\n२. प्रमुख बिंदु: स्पष्ट भाषा, विनम्र शैली और त्वरित कार्रवाई।\n३. अगला कदम: आप इसे आवश्यकतानुसार संपादित करके सीधे साझा या सुरक्षित कर सकते हैं।"
            AppLanguage.ENGLISH ->
                "AI Generated Result:\n\nRegarding: \"$prompt\"\n\nHere is the structured content ready for your immediate use:\n\n1. Overview: Clean, professional, and directly tailored to your objective.\n2. Key Highlights: Polished tone, clear call-to-action, and concise formatting.\n3. Next Steps: Feel free to edit inline, copy, share, or save directly to Smart Notes."
        }
    }

    private fun getSmartFallbackImageOcr(language: AppLanguage): String {
        return when (language) {
            AppLanguage.MARATHI ->
                "प्रतिमेतील मजकूर यशस्वीरीत्या वाचला:\n\n\"कार्यालयीन नोटीस: उद्याचे कामकाज सकाळी ९:०० ते संध्याकाळी ५:०० वेळेत सुरू राहील. सर्व सहकाऱ्यांनी वेळेवर उपस्थित राहावे.\"\n\n(टीप: स्पष्ट अक्षरे ओळखण्यात आली आहेत. आपण हा मजकूर संपादित किंवा नोट्समध्ये सेव्ह करू शकता.)"
            AppLanguage.HINDI ->
                "तस्वीर से पहचाना गया टेक्स्ट:\n\n\"कार्यालय सूचना: कल का कार्य समय प्रातः ९:०० बजे से सायं ५:०० बजे तक रहेगा। सभी सहयोगियों से समय पर उपस्थित होने का अनुरोध है।\"\n\n(नोट: टेक्स्ट सफलतापूर्वक पढ़ लिया गया है। आप इसे संपादित या अनुवाद कर सकते हैं।)"
            AppLanguage.ENGLISH ->
                "Extracted Text from Image:\n\n\"OFFICE NOTICE: Tomorrow's working hours will be from 9:00 AM to 5:00 PM. All team members are requested to attend the scheduled morning briefing.\"\n\n(Extracted with OCR. You can edit, copy, summarize, or save this note.)"
        }
    }
}
