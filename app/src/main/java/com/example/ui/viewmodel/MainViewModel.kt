package com.example.ui.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.AIDailyHelperApplication
import com.example.data.ai.AdMobManager
import com.example.data.ai.GeminiApiService
import com.example.data.local.entity.HistoryEntity
import com.example.data.local.entity.NoteEntity
import com.example.data.local.entity.TaskEntity
import com.example.model.AppLanguage
import com.example.model.AppThemeMode
import com.example.model.ChatMessage
import com.example.model.MessageSender
import com.example.model.WritingTemplate
import com.example.model.WritingTone
import com.example.ui.components.NavTab
import com.example.ui.localization.AppStrings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AppScreen {
    data class Main(val tab: NavTab = NavTab.HOME) : AppScreen()
    object Chat : AppScreen()
    object Writer : AppScreen()
    object Summarizer : AppScreen()
    object Translator : AppScreen()
    object ScanText : AppScreen()
    object VoiceNotes : AppScreen()
    object SmartNotes : AppScreen()
    object ToDo : AppScreen()
    object DailyPlanner : AppScreen()
    object DocumentHelper : AppScreen()
}

class MainViewModel : ViewModel() {
    private val app = AIDailyHelperApplication.instance
    private val notesRepo = app.notesRepository
    private val tasksRepo = app.tasksRepository
    private val historyRepo = app.historyRepository
    private val settingsRepo = app.settingsRepository
    private val geminiService = GeminiApiService()
    private val admobManager = AdMobManager()

    // Navigation Stack
    private val screenStack = mutableListOf<AppScreen>(AppScreen.Main(NavTab.HOME))
    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Main(NavTab.HOME))
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Current Tab for bottom nav
    private val _currentTab = MutableStateFlow(NavTab.HOME)
    val currentTab: StateFlow<NavTab> = _currentTab.asStateFlow()

    // Settings State
    val language: StateFlow<AppLanguage> = settingsRepo.language
    val themeMode: StateFlow<AppThemeMode> = settingsRepo.themeMode
    val remainingUses: StateFlow<Int> = settingsRepo.remainingUses

    // Data lists from Room
    val notes: StateFlow<List<NoteEntity>> = notesRepo.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<TaskEntity>> = tasksRepo.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val history: StateFlow<List<HistoryEntity>> = historyRepo.allHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Loading & Dialog States
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _loadingMessage = MutableStateFlow("")
    val loadingMessage: StateFlow<String> = _loadingMessage.asStateFlow()

    private val _showLimitDialog = MutableStateFlow(false)
    val showLimitDialog: StateFlow<Boolean> = _showLimitDialog.asStateFlow()

    private val _showLanguageDialog = MutableStateFlow(false)
    val showLanguageDialog: StateFlow<Boolean> = _showLanguageDialog.asStateFlow()

    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

    // Chat Screen State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = MessageSender.AI,
                text = "Hello! I am your AI Daily Helper. How can I help you today? You can write to me in English, हिंदी, or मराठी."
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Writer Screen State
    val writerTopic = MutableStateFlow("")
    val writerSelectedTemplate = MutableStateFlow(WritingTemplate.WHATSAPP)
    val writerSelectedTone = MutableStateFlow(WritingTone.FRIENDLY)
    val writerResult = MutableStateFlow("")

    // Summarizer Screen State
    val summarizerInput = MutableStateFlow("")
    val summarizerResult = MutableStateFlow("")

    // Translator Screen State
    val translatorInput = MutableStateFlow("")
    val translatorSourceLang = MutableStateFlow(AppLanguage.ENGLISH)
    val translatorTargetLang = MutableStateFlow(AppLanguage.MARATHI)
    val translatorResult = MutableStateFlow("")

    // Scan Text Screen State
    val scanExtractedText = MutableStateFlow("")
    val scanSelectedBitmap = MutableStateFlow<Bitmap?>(null)

    // Voice Notes State
    val voiceTranscript = MutableStateFlow("")
    val isRecordingVoice = MutableStateFlow(false)

    // Daily Planner State
    val plannerTasksInput = MutableStateFlow("")
    val plannerAvailableTime = MutableStateFlow("8 hours")
    val plannerResult = MutableStateFlow("")

    // Document Helper State
    val docInputText = MutableStateFlow("")
    val docResult = MutableStateFlow("")

    // Navigation Methods
    fun navigateTo(screen: AppScreen) {
        if (screen is AppScreen.Main) {
            _currentTab.value = screen.tab
            screenStack.clear()
            screenStack.add(screen)
        } else {
            screenStack.add(screen)
        }
        _currentScreen.value = screen
    }

    fun selectTab(tab: NavTab) {
        _currentTab.value = tab
        val mainScreen = AppScreen.Main(tab)
        screenStack.clear()
        screenStack.add(mainScreen)
        _currentScreen.value = mainScreen
    }

    fun handleBack(): Boolean {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.lastIndex)
            val previous = screenStack.last()
            _currentScreen.value = previous
            if (previous is AppScreen.Main) {
                _currentTab.value = previous.tab
            }
            return true
        } else if (_currentScreen.value !is AppScreen.Main || _currentTab.value != NavTab.HOME) {
            selectTab(NavTab.HOME)
            return true
        }
        return false
    }

    // Settings actions
    fun setLanguage(lang: AppLanguage) {
        settingsRepo.setLanguage(lang)
    }

    fun setThemeMode(mode: AppThemeMode) {
        settingsRepo.setThemeMode(mode)
    }

    fun showLanguagePicker(show: Boolean) {
        _showLanguageDialog.value = show
    }

    fun showLimitModal(show: Boolean) {
        _showLimitDialog.value = show
    }

    fun clearFeedback() {
        _userFeedbackMessage.value = null
    }

    // AdMob Rewarded Action
    fun watchRewardedAd(context: Context) {
        viewModelScope.launch {
            admobManager.showRewardedAd(
                context = context,
                onAdStarted = {
                    _loadingMessage.value = AppStrings.watchingAd(language.value)
                    _isLoading.value = true
                },
                onRewardGranted = { bonusUses ->
                    settingsRepo.addBonusUses(bonusUses)
                    _userFeedbackMessage.value = AppStrings.bonusEarned(language.value)
                },
                onAdDismissed = {
                    _isLoading.value = false
                },
                onError = { err ->
                    _isLoading.value = false
                    _userFeedbackMessage.value = err
                }
            )
        }
    }

    // AI Guard
    private fun checkQuotaOrShowModal(): Boolean {
        if (!settingsRepo.canUseAI()) {
            _showLimitDialog.value = true
            return false
        }
        settingsRepo.consumeAIUse()
        return true
    }

    // --- AI Chat Actions ---
    fun sendChatMessage(text: String) {
        if (text.isBlank() || _isLoading.value) return
        if (!checkQuotaOrShowModal()) return

        val userMsg = ChatMessage(sender = MessageSender.USER, text = text)
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _isLoading.value = true
            _loadingMessage.value = AppStrings.thinking(language.value)

            val result = geminiService.chat(
                conversation = _chatMessages.value,
                newPrompt = text,
                language = language.value
            )

            _isLoading.value = false
            result.onSuccess { reply ->
                val aiMsg = ChatMessage(sender = MessageSender.AI, text = reply)
                _chatMessages.value = _chatMessages.value + aiMsg
                // Record in history
                historyRepo.insertHistory(
                    HistoryEntity(
                        toolType = "CHAT",
                        title = "Chat: " + text.take(25) + if (text.length > 25) "..." else "",
                        inputText = text,
                        outputText = reply
                    )
                )
            }.onFailure { err ->
                val errorMsg = ChatMessage(
                    sender = MessageSender.AI,
                    text = "Sorry, unable to get response: ${err.localizedMessage ?: "Unknown error"}",
                    isError = true
                )
                _chatMessages.value = _chatMessages.value + errorMsg
            }
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                sender = MessageSender.AI,
                text = "Chat cleared. What would you like to explore next?"
            )
        )
    }

    fun regenerateLastChatMessage() {
        val lastUserMessage = _chatMessages.value.lastOrNull { it.sender == MessageSender.USER } ?: return
        sendChatMessage(lastUserMessage.text)
    }

    // --- AI Writer Actions ---
    fun generateWriting() {
        val topic = writerTopic.value
        if (topic.isBlank() || _isLoading.value) return
        if (!checkQuotaOrShowModal()) return

        val template = writerSelectedTemplate.value
        val tone = writerSelectedTone.value
        val lang = language.value

        viewModelScope.launch {
            _isLoading.value = true
            _loadingMessage.value = AppStrings.generating(lang)

            val prompt = "${template.defaultPromptPrefix} $topic. Tone: ${tone.id}. Language: ${lang.displayName}."
            val result = geminiService.generateText(prompt = prompt, language = lang)

            _isLoading.value = false
            result.onSuccess { output ->
                writerResult.value = output
                historyRepo.insertHistory(
                    HistoryEntity(
                        toolType = "WRITER",
                        title = "${template.name}: " + topic.take(25),
                        inputText = topic,
                        outputText = output
                    )
                )
            }.onFailure { err ->
                _userFeedbackMessage.value = "Failed: ${err.localizedMessage}"
            }
        }
    }

    // --- Summarizer Actions ---
    fun generateSummary() {
        val text = summarizerInput.value
        if (text.isBlank() || _isLoading.value) return
        if (!checkQuotaOrShowModal()) return

        val lang = language.value
        viewModelScope.launch {
            _isLoading.value = true
            _loadingMessage.value = AppStrings.generating(lang)

            val prompt = "Provide a clean, structured summary for the following content:\n\n1. Short Summary (2-3 sentences)\n2. Key Points (bullet list)\n3. Action Items (if any)\n\nLanguage to use: ${lang.displayName}.\n\nContent:\n$text"
            val result = geminiService.generateText(prompt = prompt, language = lang)

            _isLoading.value = false
            result.onSuccess { output ->
                summarizerResult.value = output
                historyRepo.insertHistory(
                    HistoryEntity(
                        toolType = "SUMMARIZE",
                        title = "Summary: " + text.take(25),
                        inputText = text,
                        outputText = output
                    )
                )
            }.onFailure { err ->
                _userFeedbackMessage.value = "Failed: ${err.localizedMessage}"
            }
        }
    }

    // --- Translator Actions ---
    fun generateTranslation() {
        val text = translatorInput.value
        if (text.isBlank() || _isLoading.value) return
        if (!checkQuotaOrShowModal()) return

        val src = translatorSourceLang.value
        val target = translatorTargetLang.value

        viewModelScope.launch {
            _isLoading.value = true
            _loadingMessage.value = "Translating..."

            val prompt = "Translate the following text accurately from ${src.displayName} to natural, fluent ${target.displayName}. Preserve original emotion and context:\n\n$text"
            val result = geminiService.generateText(prompt = prompt, language = target)

            _isLoading.value = false
            result.onSuccess { output ->
                translatorResult.value = output
                historyRepo.insertHistory(
                    HistoryEntity(
                        toolType = "TRANSLATE",
                        title = "${src.displayName} -> ${target.displayName}: " + text.take(20),
                        inputText = text,
                        outputText = output
                    )
                )
            }.onFailure { err ->
                _userFeedbackMessage.value = "Translation failed: ${err.localizedMessage}"
            }
        }
    }

    fun swapLanguages() {
        val temp = translatorSourceLang.value
        translatorSourceLang.value = translatorTargetLang.value
        translatorTargetLang.value = temp
        if (translatorResult.value.isNotBlank()) {
            val oldResult = translatorResult.value
            translatorInput.value = oldResult
            translatorResult.value = ""
        }
    }

    // --- Scan Text OCR Actions ---
    fun extractTextFromImage(bitmap: Bitmap) {
        scanSelectedBitmap.value = bitmap
        if (!checkQuotaOrShowModal()) return

        val lang = language.value
        viewModelScope.launch {
            _isLoading.value = true
            _loadingMessage.value = AppStrings.processingImage(lang)

            val prompt = "Carefully extract and transcribe all visible text in this image verbatim. Do not make up text."
            val result = geminiService.generateFromImage(prompt = prompt, bitmap = bitmap, language = lang)

            _isLoading.value = false
            result.onSuccess { extracted ->
                scanExtractedText.value = extracted
                historyRepo.insertHistory(
                    HistoryEntity(
                        toolType = "SCAN_TEXT",
                        title = "OCR: " + extracted.take(25).replace("\n", " "),
                        inputText = "[Image]",
                        outputText = extracted
                    )
                )
            }.onFailure { err ->
                _userFeedbackMessage.value = "OCR Failed: ${err.localizedMessage}"
            }
        }
    }

    // --- Daily Planner Actions ---
    fun planDay() {
        val tasksText = plannerTasksInput.value.ifBlank {
            tasks.value.filter { !it.isCompleted }.joinToString("\n") { "• ${it.title} (Priority: ${it.priority})" }
        }
        if (tasksText.isBlank()) {
            _userFeedbackMessage.value = "Please enter tasks or add To-Dos first."
            return
        }
        if (!checkQuotaOrShowModal()) return

        val lang = language.value
        val hours = plannerAvailableTime.value

        viewModelScope.launch {
            _isLoading.value = true
            _loadingMessage.value = "Planning your day..."

            val prompt = "Create an organized, realistic daily schedule for $hours available time. Divide clearly into:\n• Morning (सकाळ / सुबह)\n• Afternoon (दुपार / दोपहर)\n• Evening / Wrap-up (संध्याकाळ / शाम)\n\nRespond in ${lang.displayName}. Tasks:\n$tasksText"
            val result = geminiService.generateText(prompt = prompt, language = lang)

            _isLoading.value = false
            result.onSuccess { plan ->
                plannerResult.value = plan
                historyRepo.insertHistory(
                    HistoryEntity(
                        toolType = "PLANNER",
                        title = "Daily Plan ($hours)",
                        inputText = tasksText,
                        outputText = plan
                    )
                )
            }.onFailure { err ->
                _userFeedbackMessage.value = "Planning error: ${err.localizedMessage}"
            }
        }
    }

    // --- Document Helper Actions ---
    fun analyzeDocument() {
        val docText = docInputText.value
        if (docText.isBlank() || _isLoading.value) return
        if (!checkQuotaOrShowModal()) return

        val lang = language.value
        viewModelScope.launch {
            _isLoading.value = true
            _loadingMessage.value = "Analyzing document..."

            val prompt = "Analyze this document text and provide:\n1. Simple Plain-Language Explanation\n2. Key Points\n3. Important Dates or Deadlines\n4. Action Items for the User\n\nExplain naturally in ${lang.displayName}.\n\nDocument text:\n$docText"
            val result = geminiService.generateText(prompt = prompt, language = lang)

            _isLoading.value = false
            result.onSuccess { analysis ->
                docResult.value = analysis
                historyRepo.insertHistory(
                    HistoryEntity(
                        toolType = "DOCUMENT",
                        title = "Doc Analysis: " + docText.take(25),
                        inputText = docText,
                        outputText = analysis
                    )
                )
            }.onFailure { err ->
                _userFeedbackMessage.value = "Failed: ${err.localizedMessage}"
            }
        }
    }

    // --- Notes Database Actions ---
    fun saveNote(title: String, content: String, colorHex: String = "#F3F4F6") {
        if (title.isBlank() && content.isBlank()) return
        viewModelScope.launch {
            val note = NoteEntity(
                title = title.ifBlank { "Untitled Note" },
                content = content,
                colorHex = colorHex
            )
            notesRepo.insertNote(note)
            _userFeedbackMessage.value = AppStrings.savedSuccessfully(language.value)
        }
    }

    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            notesRepo.updateNote(note.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    fun togglePinNote(note: NoteEntity) {
        viewModelScope.launch {
            notesRepo.updateNote(note.copy(isPinned = !note.isPinned, updatedAt = System.currentTimeMillis()))
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            notesRepo.deleteNote(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            notesRepo.deleteAllNotes()
            _userFeedbackMessage.value = "All notes deleted."
        }
    }

    // AI Note transformation helpers
    fun summarizeNote(note: NoteEntity) {
        summarizerInput.value = note.content
        navigateTo(AppScreen.Summarizer)
        generateSummary()
    }

    fun translateNote(note: NoteEntity) {
        translatorInput.value = note.content
        navigateTo(AppScreen.Translator)
    }

    // --- Tasks Database Actions ---
    fun addTask(title: String, description: String = "", priority: String = "MEDIUM") {
        if (title.isBlank()) return
        viewModelScope.launch {
            tasksRepo.insertTask(
                TaskEntity(
                    title = title.trim(),
                    description = description.trim(),
                    priority = priority
                )
            )
        }
    }

    fun toggleTaskComplete(task: TaskEntity) {
        viewModelScope.launch {
            tasksRepo.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            tasksRepo.deleteTask(task)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            tasksRepo.deleteAllTasks()
        }
    }

    // --- History Database Actions ---
    fun deleteHistoryItem(item: HistoryEntity) {
        viewModelScope.launch {
            historyRepo.deleteHistory(item)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            historyRepo.deleteAllHistory()
            _userFeedbackMessage.value = "History cleared."
        }
    }

    fun resetAllLocalData() {
        viewModelScope.launch {
            notesRepo.deleteAllNotes()
            tasksRepo.deleteAllTasks()
            historyRepo.deleteAllHistory()
            settingsRepo.resetAllSettings()
            _chatMessages.value = emptyList()
            _userFeedbackMessage.value = "All local data reset."
        }
    }

    // Utility Clipboard & Share
    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("AI Daily Helper", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, AppStrings.copied(language.value), Toast.LENGTH_SHORT).show()
    }

    fun shareText(context: Context, text: String, subject: String = "AI Daily Helper") {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share via")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}
