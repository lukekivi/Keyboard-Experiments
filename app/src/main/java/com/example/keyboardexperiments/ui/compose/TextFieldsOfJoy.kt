package com.example.keyboardexperiments.ui.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyboardexperiments.R.string

@Composable
fun TextFieldsOfJoy(
    modifier: Modifier = Modifier
) {
    val text = stringResource(id = string.hello_keyboard)
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = text)) }
    val textFieldModifier = Modifier
        .padding(100.dp)
        .border(width = 2.dp, color = Color.Red)
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        items(5) {
            BasicTextField(
                textStyle = TextStyle.Default.copy(
                    color = Color.White,
                    fontSize = 15.sp,
                ),
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = textFieldModifier,
            )
        }
    }
}