package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AILoadingDialog
import com.example.ui.components.AppBottomBar
import com.example.ui.components.AppTopBar
import com.example.ui.components.LanguagePickerDialog
import com.example.ui.components.NavTab
import com.example.ui.components.UsageLimitDialog
import com.example.ui.localization.AppStrings
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.DailyPlannerScreen
import com.example.ui.screens.DocumentHelperScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.ScanTextScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SummarizerScreen
import com.example.ui.screens.ToDoScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.screens.TranslatorScreen
import com.example.ui.screens.VoiceNotesScreen
import com.example.ui.screens.WriterScreen
import com.example.ui.theme.AIDailyHelperTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val language by viewModel.language.collectAsStateWithLifecycle()
            val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
            val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
            val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
            val loadingMessage by viewModel.loadingMessage.collectAsStateWithLifecycle()
            val showLanguageDialog by viewModel.showLanguageDialog.collectAsStateWithLifecycle()
            val showLimitDialog by viewModel.showLimitDialog.collectAsStateWithLifecycle()
            val userFeedback by viewModel.userFeedbackMessage.collectAsStateWithLifecycle()
            val context = LocalContext.current

            // Toast feedback
            LaunchedEffect(userFeedback) {
                userFeedback?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    viewModel.clearFeedback()
                }
            }

            // Hardware & Gesture Back Button Handling
            BackHandler {
                if (!viewModel.handleBack()) {
                    finish()
                }
            }

            AIDailyHelperTheme(themeMode = themeMode) {
                val isRootTab = currentScreen is AppScreen.Main
                val topBarTitle = when (currentScreen) {
                    is AppScreen.Main -> when (currentTab) {
                        NavTab.HOME -> AppStrings.appTitle(language)
                        NavTab.TOOLS -> AppStrings.navTools(language)
                        NavTab.HISTORY -> AppStrings.navHistory(language)
                        NavTab.SETTINGS -> AppStrings.navSettings(language)
                    }
                    is AppScreen.Chat -> AppStrings.toolChat(language)
                    is AppScreen.Writer -> AppStrings.toolWriter(language)
                    is AppScreen.Summarizer -> AppStrings.toolSummarizer(language)
                    is AppScreen.Translator -> AppStrings.toolTranslator(language)
                    is AppScreen.ScanText -> AppStrings.toolScanText(language)
                    is AppScreen.VoiceNotes -> AppStrings.toolVoiceNotes(language)
                    is AppScreen.SmartNotes -> AppStrings.toolSmartNotes(language)
                    is AppScreen.ToDo -> AppStrings.toolToDo(language)
                    is AppScreen.DailyPlanner -> AppStrings.toolPlanner(language)
                    is AppScreen.DocumentHelper -> AppStrings.toolDocument(language)
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("app_root_scaffold"),
                    topBar = {
                        AppTopBar(
                            title = topBarTitle,
                            currentLanguage = language,
                            onLanguageClick = { viewModel.showLanguagePicker(true) },
                            showBackButton = !isRootTab,
                            onBackClick = { viewModel.handleBack() },
                            actions = {
                                if (isRootTab && currentTab != NavTab.SETTINGS) {
                                    IconButton(
                                        onClick = { viewModel.selectTab(NavTab.SETTINGS) },
                                        modifier = Modifier.testTag("top_bar_settings_button")
                                    ) {
                                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        if (isRootTab) {
                            AppBottomBar(
                                currentTab = currentTab,
                                onTabSelected = { viewModel.selectTab(it) },
                                language = language
                            )
                        }
                    }
                ) { innerPadding ->
                    val screenModifier = Modifier.padding(innerPadding)

                    when (currentScreen) {
                        is AppScreen.Main -> {
                            when (currentTab) {
                                NavTab.HOME -> HomeScreen(viewModel = viewModel, modifier = screenModifier)
                                NavTab.TOOLS -> ToolsScreen(viewModel = viewModel, modifier = screenModifier)
                                NavTab.HISTORY -> HistoryScreen(viewModel = viewModel, modifier = screenModifier)
                                NavTab.SETTINGS -> SettingsScreen(viewModel = viewModel, modifier = screenModifier)
                            }
                        }
                        is AppScreen.Chat -> ChatScreen(viewModel = viewModel, modifier = screenModifier)
                        is AppScreen.Writer -> WriterScreen(viewModel = viewModel, modifier = screenModifier)
                        is AppScreen.Summarizer -> SummarizerScreen(viewModel = viewModel, modifier = screenModifier)
                        is AppScreen.Translator -> TranslatorScreen(viewModel = viewModel, modifier = screenModifier)
                        is AppScreen.ScanText -> ScanTextScreen(viewModel = viewModel, modifier = screenModifier)
                        is AppScreen.VoiceNotes -> VoiceNotesScreen(viewModel = viewModel, modifier = screenModifier)
                        is AppScreen.SmartNotes -> NotesScreen(viewModel = viewModel, modifier = screenModifier)
                        is AppScreen.ToDo -> ToDoScreen(viewModel = viewModel, modifier = screenModifier)
                        is AppScreen.DailyPlanner -> DailyPlannerScreen(viewModel = viewModel, modifier = screenModifier)
                        is AppScreen.DocumentHelper -> DocumentHelperScreen(viewModel = viewModel, modifier = screenModifier)
                    }
                }

                // Global Dialogs
                if (showLanguageDialog) {
                    LanguagePickerDialog(
                        currentLanguage = language,
                        onLanguageSelected = { viewModel.setLanguage(it) },
                        onDismiss = { viewModel.showLanguagePicker(false) }
                    )
                }

                if (showLimitDialog) {
                    UsageLimitDialog(
                        language = language,
                        onWatchAdClick = { viewModel.watchRewardedAd(context) },
                        onDismiss = { viewModel.showLimitModal(false) }
                    )
                }

                if (isLoading) {
                    AILoadingDialog(
                        message = loadingMessage.ifBlank { "Processing..." }
                    )
                }
            }
        }
    }
}
