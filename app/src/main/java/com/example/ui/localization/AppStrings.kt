package com.example.ui.localization

import com.example.model.AppLanguage

object AppStrings {
    fun appTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "AI डेली हेल्पर"
        AppLanguage.HINDI -> "AI डेली हेल्पर"
        AppLanguage.ENGLISH -> "AI Daily Helper"
    }

    fun greeting(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "आज मी तुम्हाला कशी मदत करू शकतो?"
        AppLanguage.HINDI -> "आज मैं आपकी क्या सहायता कर सकता हूँ?"
        AppLanguage.ENGLISH -> "How can I help you today?"
    }

    fun navHome(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "मुख्यपृष्ठ"
        AppLanguage.HINDI -> "होम"
        AppLanguage.ENGLISH -> "Home"
    }

    fun navTools(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "साधने"
        AppLanguage.HINDI -> "टूल्स"
        AppLanguage.ENGLISH -> "Tools"
    }

    fun navHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "इतिहास"
        AppLanguage.HINDI -> "इतिहास"
        AppLanguage.ENGLISH -> "History"
    }

    fun navSettings(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "सेटिंग्ज"
        AppLanguage.HINDI -> "सेटिंग्स"
        AppLanguage.ENGLISH -> "Settings"
    }

    // Tools
    fun toolChat(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "AI चॅट"
        AppLanguage.HINDI -> "AI चैट"
        AppLanguage.ENGLISH -> "AI Chat"
    }

    fun toolChatDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "काहीही विचारा किंवा संवाद साधा"
        AppLanguage.HINDI -> "कुछ भी पूछें या बातचीत करें"
        AppLanguage.ENGLISH -> "Ask questions or converse naturally"
    }

    fun toolWriter(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "AI रायटर"
        AppLanguage.HINDI -> "AI राइटर"
        AppLanguage.ENGLISH -> "AI Writer"
    }

    fun toolWriterDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "मैसेज, पत्र व ईमेल तयार करा"
        AppLanguage.HINDI -> "मैसेज, पत्र और ईमेल लिखें"
        AppLanguage.ENGLISH -> "Draft messages, letters & emails"
    }

    fun toolSummarizer(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "मजकूर सारांश"
        AppLanguage.HINDI -> "टेक्स्ट सारांश"
        AppLanguage.ENGLISH -> "Summarizer"
    }

    fun toolSummarizerDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "मोठ्या मजकुराचा झटपट सारांश"
        AppLanguage.HINDI -> "लंबे टेक्स्ट का संक्षिप्त सारांश"
        AppLanguage.ENGLISH -> "Get concise summary & key points"
    }

    fun toolTranslator(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "भाषांतर"
        AppLanguage.HINDI -> "अनुवादक"
        AppLanguage.ENGLISH -> "Translator"
    }

    fun toolTranslatorDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "मराठी, हिंदी व इंग्रजी अनुवाद"
        AppLanguage.HINDI -> "हिंदी, मराठी और अंग्रेजी अनुवाद"
        AppLanguage.ENGLISH -> "Accurate Marathi, Hindi & English"
    }

    fun toolScanText(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "मजकूर स्कॅन (OCR)"
        AppLanguage.HINDI -> "टेक्स्ट स्कैन (OCR)"
        AppLanguage.ENGLISH -> "Scan Text (OCR)"
    }

    fun toolScanTextDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "फोटोमधील अक्षरे ओळखा"
        AppLanguage.HINDI -> "फोटो से टेक्स्ट निकालें"
        AppLanguage.ENGLISH -> "Extract readable text from photos"
    }

    fun toolVoiceNotes(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "व्हॉईस नोट्स"
        AppLanguage.HINDI -> "वॉयस नोट्स"
        AppLanguage.ENGLISH -> "Voice Notes"
    }

    fun toolVoiceNotesDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "बोला आणि लिखाण तयार करा"
        AppLanguage.HINDI -> "बोलकर टेक्स्ट में बदलें"
        AppLanguage.ENGLISH -> "Convert speech to editable text"
    }

    fun toolSmartNotes(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "स्मार्ट नोट्स"
        AppLanguage.HINDI -> "स्मार्ट नोट्स"
        AppLanguage.ENGLISH -> "Smart Notes"
    }

    fun toolSmartNotesDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "AI सह जतन केलेल्या नोंदी"
        AppLanguage.HINDI -> "AI के साथ सुरक्षित टिप्पणियाँ"
        AppLanguage.ENGLISH -> "Organize, rewrite & summarize notes"
    }

    fun toolToDo(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "कामे (To-Do)"
        AppLanguage.HINDI -> "कार्य सूची (To-Do)"
        AppLanguage.ENGLISH -> "To-Do List"
    }

    fun toolToDoDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "दैनंदिन कामांचे व्यवस्थापन"
        AppLanguage.HINDI -> "दैनिक कार्यों का प्रबंधन"
        AppLanguage.ENGLISH -> "Manage tasks with AI Day Planner"
    }

    fun toolPlanner(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "दैनिक नियोजन"
        AppLanguage.HINDI -> "दैनिक योजना"
        AppLanguage.ENGLISH -> "Daily Planner"
    }

    fun toolPlannerDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "सकाळ, दुपार, संध्याकाळ योजना"
        AppLanguage.HINDI -> "सुबह, दोपहर, शाम की योजना"
        AppLanguage.ENGLISH -> "Smart schedule for morning to evening"
    }

    fun toolDocument(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "दस्तऐवज मदतनीस"
        AppLanguage.HINDI -> "दस्तावेज सहायक"
        AppLanguage.ENGLISH -> "Document Helper"
    }

    fun toolDocumentDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "कागदपत्रांचे सोप्या भाषेत विश्लेषण"
        AppLanguage.HINDI -> "दस्तावेजों का सरल विश्लेषण"
        AppLanguage.ENGLISH -> "Explain bills, notices & legal forms"
    }

    // Common Actions
    fun generate(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "तयार करा (Generate)"
        AppLanguage.HINDI -> "बनाएं (Generate)"
        AppLanguage.ENGLISH -> "Generate"
    }

    fun copy(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "कॉपी करा"
        AppLanguage.HINDI -> "कॉपी करें"
        AppLanguage.ENGLISH -> "Copy"
    }

    fun copied(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "कॉपी केले!"
        AppLanguage.HINDI -> "कॉपी हो गया!"
        AppLanguage.ENGLISH -> "Copied to clipboard!"
    }

    fun share(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "शेअर करा"
        AppLanguage.HINDI -> "शेयर करें"
        AppLanguage.ENGLISH -> "Share"
    }

    fun saveAsNote(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "नोट म्हणून सेव्ह करा"
        AppLanguage.HINDI -> "नोट के रूप में सहेजें"
        AppLanguage.ENGLISH -> "Save as Note"
    }

    fun savedSuccessfully(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "यशस्वीरीत्या सेव्ह केले!"
        AppLanguage.HINDI -> "सफलतापूर्वक सहेजा गया!"
        AppLanguage.ENGLISH -> "Saved successfully!"
    }

    fun clear(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "साफ करा"
        AppLanguage.HINDI -> "साफ़ करें"
        AppLanguage.ENGLISH -> "Clear"
    }

    fun delete(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "हटवा"
        AppLanguage.HINDI -> "हटाएं"
        AppLanguage.ENGLISH -> "Delete"
    }

    fun edit(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "संपादन"
        AppLanguage.HINDI -> "संपादित करें"
        AppLanguage.ENGLISH -> "Edit"
    }

    fun cancel(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "रद्द करा"
        AppLanguage.HINDI -> "रद्द करें"
        AppLanguage.ENGLISH -> "Cancel"
    }

    fun save(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "जतन करा"
        AppLanguage.HINDI -> "सहेजें"
        AppLanguage.ENGLISH -> "Save"
    }

    fun regenerate(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "पुन्हा तयार करा"
        AppLanguage.HINDI -> "पुनः उत्पन्न करें"
        AppLanguage.ENGLISH -> "Regenerate"
    }

    // Usage & AdMob
    fun usesRemainingToday(remaining: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "आज $remaining AI वापर शिल्लक आहेत"
        AppLanguage.HINDI -> "आज $remaining AI उपयोग शेष हैं"
        AppLanguage.ENGLISH -> "$remaining AI uses remaining today"
    }

    fun limitReachedTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "दैनंदिन AI मर्यादा संपली"
        AppLanguage.HINDI -> "दैनिक AI सीमा समाप्त"
        AppLanguage.ENGLISH -> "Daily AI Limit Reached"
    }

    fun limitReachedMsg(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "आपण आजची मोफत AI मर्यादा पूर्ण केली आहे. आपण थोड्या वेळाने किंवा उद्या पुन्हा प्रयत्न करू शकता, किंवा जाहिरात पाहून त्वरित ५ अतिरिक्त वापर मिळवू शकता. (आपल्या नोट्स आणि कामे सुरळीत सुरू राहतील)."
        AppLanguage.HINDI -> "आपने आज का दैनिक AI कोटा पूरा कर लिया है। आप कुछ समय बाद या कल पुनः प्रयास कर सकते हैं, अथवा विज्ञापन देखकर तुरंत ५ अतिरिक्त उपयोग प्राप्त कर सकते हैं। (आपके नोट्स और कार्य सुचारू रूप से कार्य करते रहेंगे)।"
        AppLanguage.ENGLISH -> "You have reached your daily AI limit. You can try again later, or watch a short sponsored ad to get +5 bonus uses immediately. (Non-AI tools like Notes and To-Dos continue to work normally)."
    }

    fun watchAdForBonus(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "जाहिरात पाहा (+५ मोफत वापर)"
        AppLanguage.HINDI -> "विज्ञापन देखें (+५ बोनस उपयोग)"
        AppLanguage.ENGLISH -> "Watch Ad (+5 Free Uses)"
    }

    fun watchingAd(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "जाहिरात लोड होत आहे..."
        AppLanguage.HINDI -> "विज्ञापन लोड हो रहा है..."
        AppLanguage.ENGLISH -> "Loading sponsored ad..."
    }

    fun bonusEarned(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "+५ अतिरिक्त वापर जमा झाले आहेत!"
        AppLanguage.HINDI -> "+५ अतिरिक्त उपयोग प्राप्त हुए!"
        AppLanguage.ENGLISH -> "+5 bonus uses credited!"
    }

    // Empty States
    fun emptyNotes(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "तुमच्याकडे अजून कोणत्याही नोट्स नाहीत."
        AppLanguage.HINDI -> "आपके पास अभी कोई नोट्स नहीं हैं।"
        AppLanguage.ENGLISH -> "You don't have any notes yet."
    }

    fun emptyTasks(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "सर्व कामे पूर्ण झाली आहेत! अभिनंदन!"
        AppLanguage.HINDI -> "सभी कार्य पूरे हो चुके हैं! बधाई!"
        AppLanguage.ENGLISH -> "You're all caught up!"
    }

    fun emptyHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "तुमचा अलीकडील इतिहास येथे दिसेल."
        AppLanguage.HINDI -> "आपकी हालिया गतिविधि यहाँ दिखाई देगी।"
        AppLanguage.ENGLISH -> "Your recent activity will appear here."
    }

    // Status & Loading
    fun thinking(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "AI विचार करत आहे..."
        AppLanguage.HINDI -> "AI सोच रहा है..."
        AppLanguage.ENGLISH -> "Thinking..."
    }

    fun generating(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "मजकूर तयार होत आहे..."
        AppLanguage.HINDI -> "तैयार किया जा रहा है..."
        AppLanguage.ENGLISH -> "Generating..."
    }

    fun processingImage(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "प्रतिमेवरील अक्षरे वाचत आहे..."
        AppLanguage.HINDI -> "तस्वीर से टेक्स्ट पढ़ा जा रहा है..."
        AppLanguage.ENGLISH -> "Processing image text..."
    }

    fun listening(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> "ऐकत आहे... कृपया बोला..."
        AppLanguage.HINDI -> "सुन रहा हूँ... कृपया बोलें..."
        AppLanguage.ENGLISH -> "Listening... please speak..."
    }

    // Writing templates
    fun templateName(templateId: String, lang: AppLanguage): String = when (templateId) {
        "whatsapp" -> when (lang) {
            AppLanguage.MARATHI -> "व्हॉट्सॲप संदेश"
            AppLanguage.HINDI -> "व्हाट्सएप मैसेज"
            AppLanguage.ENGLISH -> "WhatsApp Message"
        }
        "email" -> when (lang) {
            AppLanguage.MARATHI -> "ईमेल"
            AppLanguage.HINDI -> "ईमेल"
            AppLanguage.ENGLISH -> "Email"
        }
        "letter" -> when (lang) {
            AppLanguage.MARATHI -> "औपचारिक पत्र"
            AppLanguage.HINDI -> "औपचारिक पत्र"
            AppLanguage.ENGLISH -> "Formal Letter"
        }
        "application" -> when (lang) {
            AppLanguage.MARATHI -> "अर्ज / रजा विनंती"
            AppLanguage.HINDI -> "आवेदन / छुट्टी पत्र"
            AppLanguage.ENGLISH -> "Application"
        }
        "business" -> when (lang) {
            AppLanguage.MARATHI -> "व्यावसायिक संदेश"
            AppLanguage.HINDI -> "व्यावसायिक संदेश"
            AppLanguage.ENGLISH -> "Business Message"
        }
        "social" -> when (lang) {
            AppLanguage.MARATHI -> "सोशल मीडिया कॅप्शन"
            AppLanguage.HINDI -> "सोशल मीडिया कैप्शन"
            AppLanguage.ENGLISH -> "Social Media Caption"
        }
        "thank_you" -> when (lang) {
            AppLanguage.MARATHI -> "आभार संदेश"
            AppLanguage.HINDI -> "धन्यवाद संदेश"
            AppLanguage.ENGLISH -> "Thank You Message"
        }
        else -> when (lang) {
            AppLanguage.MARATHI -> "कस्टम"
            AppLanguage.HINDI -> "कस्टम"
            AppLanguage.ENGLISH -> "Custom"
        }
    }
}
