package com.seng303.assignment1.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun PlayerNameDialog(
    onConfirmation: (playerName: String) -> Unit,
//    onDismissRequest: () -> Unit,
) {
    var playerName by rememberSaveable {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = { /*Intentionally Blank*/}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                disabledContentColor = Color.LightGray,
                disabledContainerColor = Color.LightGray
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.Default.Face, contentDescription = "Face Icon", Modifier.padding(8.dp))
                Text(text = "Please enter your name.", color = MaterialTheme.colorScheme.onErrorContainer)

                OutlinedTextField(
                    value = playerName,
                    onValueChange = {playerName=it},
                    modifier = Modifier
                        .height(70.dp)
                        .padding(8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = { onConfirmation(playerName) }, modifier = Modifier.padding(bottom = 8.dp)) {
                        Text(text = "Confirm Name")
                    }
                }
            }
        }
    }
}