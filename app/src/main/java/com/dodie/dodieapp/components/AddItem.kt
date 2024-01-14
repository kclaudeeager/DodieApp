package com.dodie.dodieapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dodie.dodieapp.ui.theme.DodieAppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddItem(
    modifier: Modifier = Modifier,
    onAddItem: (title: String, description: String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    // Access to SoftwareKeyboardController
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    // Display content
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Move focus to the next field

                }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Close the keyboard when the Done action is triggered
                    softwareKeyboardController?.hide()
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Trigger the callback with the entered title and description
                onAddItem(title, description)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Add")
        }
    }

}

@Composable
fun LoadingIndicator() {
    // Display loading indicator
    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
}

@Preview
@Composable
fun AddItemPreview() {
    DodieAppTheme {
        Surface {
            AddItem(modifier = Modifier.fillMaxSize()) { _, _ ->
                // Handle the callback action (e.g., add item to the list)
            }
        }
    }
}
