package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.automirrored.filled.ShortText
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

data class ToolDefinition(
    val title: (AppLanguage) -> String,
    val description: (AppLanguage) -> String,
    val icon: ImageVector,
    val iconColor: Color,
    val containerColor: Color,
    val screen: AppScreen,
    val testTag: String
)

@Composable
fun ToolsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsStateWithLifecycle()

    val communicationTools = listOf(
        ToolDefinition(
            title = { AppStrings.toolChat(it) },
            description = { AppStrings.toolChatDesc(it) },
            icon = Icons.Default.ChatBubbleOutline,
            iconColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            screen = AppScreen.Chat,
            testTag = "tool_item_chat"
        ),
        ToolDefinition(
            title = { AppStrings.toolWriter(it) },
            description = { AppStrings.toolWriterDesc(it) },
            icon = Icons.Default.EditNote,
            iconColor = MaterialTheme.colorScheme.secondary,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            screen = AppScreen.Writer,
            testTag = "tool_item_writer"
        ),
        ToolDefinition(
            title = { AppStrings.toolTranslator(it) },
            description = { AppStrings.toolTranslatorDesc(it) },
            icon = Icons.Default.Translate,
            iconColor = Color(0xFF0284C7),
            containerColor = Color(0xFFE0F2FE),
            screen = AppScreen.Translator,
            testTag = "tool_item_translator"
        )
    )

    val intelligenceTools = listOf(
        ToolDefinition(
            title = { AppStrings.toolSummarizer(it) },
            description = { AppStrings.toolSummarizerDesc(it) },
            icon = Icons.AutoMirrored.Filled.ShortText,
            iconColor = MaterialTheme.colorScheme.tertiary,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            screen = AppScreen.Summarizer,
            testTag = "tool_item_summarizer"
        ),
        ToolDefinition(
            title = { AppStrings.toolScanText(it) },
            description = { AppStrings.toolScanTextDesc(it) },
            icon = Icons.AutoMirrored.Filled.TextSnippet,
            iconColor = Color(0xFF7E22CE),
            containerColor = Color(0xFFF3E8FF),
            screen = AppScreen.ScanText,
            testTag = "tool_item_scan"
        ),
        ToolDefinition(
            title = { AppStrings.toolVoiceNotes(it) },
            description = { AppStrings.toolVoiceNotesDesc(it) },
            icon = Icons.Default.Mic,
            iconColor = Color(0xFFEA580C),
            containerColor = Color(0xFFFFEDD5),
            screen = AppScreen.VoiceNotes,
            testTag = "tool_item_voice"
        ),
        ToolDefinition(
            title = { AppStrings.toolDocument(it) },
            description = { AppStrings.toolDocumentDesc(it) },
            icon = Icons.Default.AutoAwesome,
            iconColor = Color(0xFFCA8A04),
            containerColor = Color(0xFFFEF9C3),
            screen = AppScreen.DocumentHelper,
            testTag = "tool_item_document"
        )
    )

    val organizationTools = listOf(
        ToolDefinition(
            title = { AppStrings.toolSmartNotes(it) },
            description = { AppStrings.toolSmartNotesDesc(it) },
            icon = Icons.Default.Description,
            iconColor = Color(0xFF16A34A),
            containerColor = Color(0xFFDCFCE7),
            screen = AppScreen.SmartNotes,
            testTag = "tool_item_notes"
        ),
        ToolDefinition(
            title = { AppStrings.toolToDo(it) },
            description = { AppStrings.toolToDoDesc(it) },
            icon = Icons.Default.CheckCircleOutline,
            iconColor = Color(0xFFDB2777),
            containerColor = Color(0xFFFCE7F3),
            screen = AppScreen.ToDo,
            testTag = "tool_item_todo"
        ),
        ToolDefinition(
            title = { AppStrings.toolPlanner(it) },
            description = { AppStrings.toolPlannerDesc(it) },
            icon = Icons.Default.CalendarToday,
            iconColor = Color(0xFF4F46E5),
            containerColor = Color(0xFFEEF2FF),
            screen = AppScreen.DailyPlanner,
            testTag = "tool_item_planner"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("tools_screen_scroll"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "AI Suite for Daily Life",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Select any tool below to draft, translate, summarize, extract text or organize your day.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Category 1: Communication
        item {
            ToolCategoryHeader(title = "Language & Communication")
        }
        items(communicationTools.size) { index ->
            ToolRowItem(tool = communicationTools[index], language = language, onSelect = { viewModel.navigateTo(it) })
        }

        // Category 2: AI Intelligence & Recognition
        item {
            ToolCategoryHeader(title = "AI Reading & Voice Intelligence")
        }
        items(intelligenceTools.size) { index ->
            ToolRowItem(tool = intelligenceTools[index], language = language, onSelect = { viewModel.navigateTo(it) })
        }

        // Category 3: Planning & Organization
        item {
            ToolCategoryHeader(title = "Planning & Organization")
        }
        items(organizationTools.size) { index ->
            ToolRowItem(tool = organizationTools[index], language = language, onSelect = { viewModel.navigateTo(it) })
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ToolCategoryHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun ToolRowItem(
    tool: ToolDefinition,
    language: AppLanguage,
    onSelect: (AppScreen) -> Unit
) {
    ElevatedCard(
        onClick = { onSelect(tool.screen) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tool.testTag)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(tool.containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = tool.icon,
                    contentDescription = null,
                    tint = tool.iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tool.title(language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tool.description(language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
