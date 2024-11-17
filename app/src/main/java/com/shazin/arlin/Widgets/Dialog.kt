package com.shazin.arlin.Widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun DialogBox(
    state :  MutableState<Boolean>,
    background: Color = MaterialTheme.colorScheme.surfaceVariant,
    minHeight: Dp = 130.dp,
    onDismisRequest: ()-> Unit,
    content : @Composable () -> Unit
){
    if (state.value == true) {
        Dialog(
            onDismissRequest = { /*TODO*/ },
            properties = DialogProperties(
                dismissOnBackPress = true,

                ),

            ) {
            Surface (
                modifier = Modifier
                    .background(background, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                color = background
            ) {
                // Your Dialog content here
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .defaultMinSize(minHeight=130.dp)
                ) {
                    content()
                    // Example content
                    // Add your text, buttons, etc. here
                }
            }
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.Red)
//                    .defaultMinSize(minHeight = 130.dp)
//
//            ) {
//                content()
//            }

        }
    }
}

@Composable
fun DialogLoader(state: MutableState<Boolean>){
    DialogBox(state = state, onDismisRequest = { /*TODO*/ }, ) {
        Box(modifier =
        Modifier
            .fillMaxWidth()
            .height(130.dp),
            contentAlignment = Alignment.Center,
        ){
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAlert(
    state : MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    if (state.value) {
        AlertDialog(
            icon = {
                Icon(icon, contentDescription = "Example Icon")
            },
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = dialogText)
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}