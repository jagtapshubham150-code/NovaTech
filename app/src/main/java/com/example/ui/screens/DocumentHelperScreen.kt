package com.example.ui.screens

import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.localization.AppStrings
import com.example.ui.viewmodel.MainViewModel

@Composable
fun DocumentHelperScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsStateWithLifecycle()
    val docText by viewModel.docInputText.collectAsStateWithLifecycle()
    val docResult by viewModel.docResult.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val sampleDocuments = listOf(
        "Rental Agreement" to "Standard 11-month lease agreement. Rent: Rs. 18,000 per month payable by 5th. Security deposit Rs. 50,000 refundable upon notice of 30 days. No structural alterations allowed without written landlord consent.",
        "Electricity Notice" to "MSEDCL Billing Notification: Meter reading dated 15th Sep. Current bill amount: Rs. 2,450. Due date without penalty: 28th Sep. Disconnection notice effective if outstanding balance is unpaid by 10th Oct.",
        "Job Offer Letter" to "Employment Offer: Role Software Engineer, Base Salary Rs. 8,50,000 per annum with standard benefits. Probation period 3 months. Acceptance deadline 15th of next month."
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("document_helper_screen"),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Document Input Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Document Understanding",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Paste any official letter, bill, contract or notice to get a clear breakdown in ${language.nativeName}.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Quick Sample Templates:", style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(sampleDocuments) { (label, content) ->
                            FilterChip(
                                selected = false,
                                onClick = { viewModel.docInputText.value = content },
                                label = { Text(label) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Document Text", style = MaterialTheme.typography.labelMedium)
                        Row {
                            IconButton(
                                onClick = {
                                    val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val text = clip.primaryClip?.getItemAt(0)?.text?.toString() ?: ""
                                    if (text.isNotBlank()) {
                                        viewModel.docInputText.value = text
                                    }
                                },
                                modifier = Modifier.testTag("doc_paste_button")
                            ) {
                                Icon(Icons.Default.ContentPaste, contentDescription = "Paste")
                            }
                            if (docText.isNotEmpty()) {
                                IconButton(
                                    onClick = { viewModel.docInputText.value = "" },
                                    modifier = Modifier.testTag("doc_clear_button")
                                ) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = docText,
                        onValueChange = { viewModel.docInputText.value = it },
                        placeholder = { Text("Paste document or notice text here...") },
                        minLines = 5,
                        maxLines = 10,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("doc_input_field")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.analyzeDocument() },
                        enabled = docText.isNotBlank() && !isLoading,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("doc_analyze_button")
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Analyze Document", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Analysis Output Card
        if (docResult.isNotBlank()) {
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("doc_result_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Analysis & Explanation",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = docResult,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(
                                    onClick = { viewModel.copyToClipboard(context, docResult) },
                                    modifier = Modifier.testTag("doc_copy_button")
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                                }
                                IconButton(
                                    onClick = { viewModel.shareText(context, docResult, "Document Analysis") },
                                    modifier = Modifier.testTag("doc_share_button")
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = "Share")
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.saveNote(
                                        title = "Document Breakdown",
                                        content = docResult
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("doc_save_note_button")
                            ) {
                                Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(AppStrings.saveAsNote(language))
                            }
                        }
                    }
                }
            }
        }
    }
}
