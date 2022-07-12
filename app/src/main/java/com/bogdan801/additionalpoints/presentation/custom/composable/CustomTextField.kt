package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChanged: (String)->Unit = {},
    placeholder: String = "",
    roundedCorners: Dp = 15.dp,
    textAlign: TextAlign = TextAlign.Start,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(roundedCorners))
            .background(MaterialTheme.colors.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(15.dp)
            ),
        contentAlignment = Alignment.Center
    ){
        val isTextFieldInFocus = remember {
            mutableStateOf(false)
        }

        if(value.isBlank() && !isTextFieldInFocus.value){
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = placeholder,
                color = Color.Gray,
                style = MaterialTheme.typography.body1,
                textAlign = textAlign
            )
        }

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .onFocusChanged {
                    isTextFieldInFocus.value = it.isFocused
                },
            value = value,
            onValueChange = onValueChanged,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.copy(textAlign = textAlign),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush
        )
    }
}