package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.WritingTemplate
import com.example.model.WritingTone
import com.example.ui.localization.AppStrings
import com.example.ui.viewmodel.MainViewModel

@Composable
fun WriterScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsStateWithLifecycle()
    val topic by viewModel.writerTopic.collectAsStateWithLifecycle()
    val selectedTemplate by viewModel.writerSelectedTemplate.collectAsStateWithLifecycle()
    val selectedTone by viewModel.writerSelectedTone.collectAsStateWithLifecycle()
    val resultText by viewModel.writerResult.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var isEditingOutput by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("writer_screen_scroll"),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section: Template Selector
        item {
            Text(
                text = "1. Select Template",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(WritingTemplate.entries) { template ->
                    FilterChip(
                        selected = selectedTemplate == template,
                        onClick = { viewModel.writerSelectedTemplate.value = template },
                        label = { Text(AppStrings.templateName(template.id, language)) },
                        modifier = Modifier.testTag("template_chip_${template.id}")
                    )
                }
            }
        }

        // Section: Topic & Details
        item {
            Text(
                text = "2. Topic & Content",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = topic,
                onValueChange = { viewModel.writerTopic.value = it },
                placeholder = {
                    Text(
                        text = when (language) {
                            com.example.model.AppLanguage.MARATHI -> "उदा. दोन दिवसांची रजा हवी आहे, किंवा लग्न सोहळ्याचे निमंत्रण..."
                            com.example.model.AppLanguage.HINDI -> "उदा. दो दिन की छुट्टी का आवेदन, या शादी का निमंत्रण..."
                            com.example.model.AppLanguage.ENGLISH -> "e.g. Leave request for 2 days, or meeting follow up..."
                        }
                    )
                },
                minLines = 3,
                maxLines = 6,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("writer_topic_input")
            )
        }

        // Section: Tone Selector
        item {
            Text(
                text = "3. Choose Tone",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(WritingTone.entries) { tone ->
                    FilterChip(
                        selected = selectedTone == tone,
                        onClick = { viewModel.writerSelectedTone.value = tone },
                        label = { Text(tone.id) },
                        modifier = Modifier.testTag("tone_chip_${tone.id}")
                    )
                }
            }
        }

        // Generate Action Button
        item {
            Button(
                onClick = { viewModel.generateWriting() },
                enabled = topic.isNotBlank() && !isLoading,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("writer_generate_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.generate(language),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Generated Output Section
        if (resultText.isNotBlank()) {
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("writer_result_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Generated Content",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { isEditingOutput = !isEditingOutput }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Inline",
                                    tint = if (isEditingOutput) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (isEditingOutput) {
                            OutlinedTextField(
                                value = resultText,
                                onValueChange = { viewModel.writerResult.value = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        } else {
                            Text(
                                text = resultText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Action Bar: Copy, Share, Regenerate, Save as Note
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(
                                    onClick = { viewModel.copyToClipboard(context, resultText) },
                                    modifier = Modifier.testTag("writer_copy_button")
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                                }
                                IconButton(
                                    onClick = { viewModel.shareText(context, resultText, selectedTemplate.name) },
                                    modifier = Modifier.testTag("writer_share_button")
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = "Share")
                                }
                                IconButton(
                                    onClick = { viewModel.generateWriting() },
                                    modifier = Modifier.testTag("writer_regenerate_button")
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Regenerate")
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.saveNote(
                                        title = "${selectedTemplate.name} Draft",
                                        content = resultText
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("writer_save_as_note_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkAdd,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(AppStrings.saveAsNote(language))
                            }
                        }
                    }
                }
            }
        }
    }
}
