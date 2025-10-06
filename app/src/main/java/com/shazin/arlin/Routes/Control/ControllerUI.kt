package com.shazin.arlin.Routes.Control


import android.provider.CalendarContract.Colors
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shazin.arlin.R
import com.shazin.arlin.Utils.calculateCursorMovement
import com.shazin.arlin.ViewModels.ConnectionViewModel


@Composable
fun ControllerUI(connectionViewModel: ConnectionViewModel) {
    val elementGap = 10.dp
    val interactionSource = remember { MutableInteractionSource() }
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, bottom = 30.dp, start = 20.dp, end = 20.dp)
    ) {
        Column(
        ) {
            // Track Pad
            TrackPad(
                modifier = Modifier
                    .clip(RoundedCornerShape(5))
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {
                            connectionViewModel.sendMessage("MOUSE button=left")
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    )
                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(0.5f))
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            val (dx, dy) = calculateCursorMovement(dragAmount.x, dragAmount.y)

                            connectionViewModel.sendMessage("MOVE dx=$dx dy=$dy")
                        }
                    }
            )
            Spacer(modifier = Modifier.height(elementGap))
            // Mouse Buttons
            Row {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                            connectionViewModel.sendMessage("MOUSE button=left")
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                        .weight(1f)
                        .height(48.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.5f)),
                    contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.mouse_left),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                }
                Spacer(modifier = Modifier.width(elementGap))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                            connectionViewModel.sendMessage("MOUSE button=right")
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                        .weight(1f)
                        .height(48.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.mouse_right),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                }
            }
            Spacer(modifier = Modifier.height(elementGap))
            // Area for esc, space and keypad
            Row {
                // space and esc btn
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .clickable {
                                connectionViewModel.sendMessage("PRESS key=esc")
                            }
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.5f)),
                        contentAlignment = Alignment.Center) {
                        Text(
                            text = "ESC",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                    Spacer(modifier = Modifier.height(elementGap))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .clickable { connectionViewModel.sendMessage("PRESS key=space") }
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.5f)),
                        contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.SpaceBar,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                }
                Spacer(modifier = Modifier.width(elementGap))
                // Area for arrow keys
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(15.dp))
                                .clickable { connectionViewModel.sendMessage("PRESS key=left") }
                                .weight(1f)
                                .height(48.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(0.4f)),
                            contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_left_alt),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.surfaceTint
                            )
                        }
                        Spacer(modifier = Modifier.width(elementGap))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(15.dp))
                                .clickable { connectionViewModel.sendMessage("PRESS key=up") }
                                .weight(1f)
                                .height(48.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(0.4f)),
                            contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_upward_alt),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.surfaceTint
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(elementGap))
                    Row {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(15.dp))
                                .clickable { connectionViewModel.sendMessage("PRESS key=down") }
                                .weight(1f)
                                .height(48.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(0.4f)),
                            contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_downward),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.surfaceTint
                            )
                        }
                        Spacer(modifier = Modifier.width(elementGap))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(15.dp))
                                .clickable { connectionViewModel.sendMessage("PRESS key=right") }
                                .weight(1f)
                                .height(48.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(0.4f)),
                            contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_right_alt),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.surfaceTint
                            )
                        }
                    }
                }
            }

            // Remote input sending area
            Spacer(modifier = Modifier.height(elementGap))
            ControllerInputBar(connectionViewModel)

        }
    }
}

// Trackpad
@Composable
fun TrackPad(modifier: Modifier) {

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = "Drag your finger across this area to simulate a trackpad. Click on this area to simulate a click",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(15.dp),
        )
    }
}


// Controller Input Bar
@Composable
fun ControllerInputBar(connectionViewModel: ConnectionViewModel) {
    val inputOpen = remember {
        mutableStateOf(false)
    }
    var inputText by remember {
        mutableStateOf("")
    }

    var isKeyboardVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val imeVisibility = WindowInsets.ime.getBottom(LocalDensity.current) > 0


    LaunchedEffect(imeVisibility) {
        isKeyboardVisible = imeVisibility
        if (!imeVisibility) {
            focusManager.clearFocus()
        }
    }


    Box(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .border(
            width = 1.dp,
            color = if (inputOpen.value) MaterialTheme.colorScheme.surfaceTint else Color.Transparent,
            shape = RoundedCornerShape(15.dp)
        )
        .clickable {
            inputOpen.value = true
            focusRequester.requestFocus()
        }
        .clip(RoundedCornerShape(15.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.5f))

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Icon(
                imageVector = Icons.Default.Keyboard,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.surfaceTint
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Send Input",
                color = MaterialTheme.colorScheme.surfaceTint,
                style = MaterialTheme.typography.labelMedium
            )

        }


        BasicTextField(
            value = inputText,
            onValueChange = {
                if (it.length < inputText.length) {
                    connectionViewModel.sendMessage("PRESS key=backspace")
                } else if (it.length > inputText.length) {
                    if (it.last() == ' '){
                        // Space detected
                        connectionViewModel.sendMessage("PRESS key=space")
                    }
                    if (it.last() == '\n'){
                        // Enter detected
                        connectionViewModel.sendMessage("PRESS key=enter")
                    }
                    else {
                        connectionViewModel.sendMessage("PRESS key=${it.last()}")
                    }
                }
                inputText = it
            },

            maxLines = 1,
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .focusable()
                .onFocusChanged {
                    println("Focus changed New state: ${it.isFocused}")
                    if (it.isFocused) {
                        inputOpen.value = true
                    } else {
                        inputOpen.value = false
                    }
                },
            textStyle = TextStyle(
                color = Color.Transparent
            ),
            cursorBrush = SolidColor(Color.Transparent),

            )

    }
}


// Controller Button
@Composable
fun ControllerButton(
    icon: ImageVector? = null,
    text: String? = null,
    weight: Float = 1f,
    onClick: () -> Unit
) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(15.dp))
        .clickable { onClick() }
        .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.6f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = "")
                Spacer(modifier = Modifier.width(10.dp))
            }
            if (text != null) {
                Text(
                    text = text
                )
            }

        }
    }
}
