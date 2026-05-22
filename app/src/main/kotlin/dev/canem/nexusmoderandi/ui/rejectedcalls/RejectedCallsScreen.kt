package dev.canem.nexusmoderandi.ui.rejectedcalls

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RejectedCallsScreen(
    viewModel: RejectedCallsViewModel = hiltViewModel()
) {
    val calls by viewModel.rejectedCalls.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Rejected Calls") },
            actions = {
                if (calls.isNotEmpty()) {
                    IconButton(onClick = viewModel::clearAll) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Clear all")
                    }
                }
            }
        )

        if (calls.isEmpty()) {
            Text(
                text = "No rejected calls yet.",
                modifier = Modifier.padding(32.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn {
                items(calls, key = { it.id }) { call ->
                    ListItem(
                        headlineContent = {
                            Text(call.phoneNumber.ifBlank { "Unknown" })
                        },
                        supportingContent = {
                            Text(
                                DateUtils.getRelativeTimeSpanString(
                                    call.timestamp,
                                    System.currentTimeMillis(),
                                    DateUtils.MINUTE_IN_MILLIS
                                ).toString()
                            )
                        }
                    )
                }
            }
        }
    }
}
