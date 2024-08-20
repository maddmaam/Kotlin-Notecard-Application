package com.seng303.assignment1.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    alertTitle: String,
    alertText: String,
    dismissText: String,
    confirmText: String,
    icon: ImageVector,
    confirmColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = alertTitle)
        },
        text = {
            Text(text = alertText, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onErrorContainer)
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(confirmText, color = confirmColor)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(dismissText, color = MaterialTheme.colorScheme.onErrorContainer)
            }
        }
    )
}