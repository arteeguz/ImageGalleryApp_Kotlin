package com.example.imagegalleryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ImageGalleryApp()
                }
            }
        }
    }
}

// Data class to represent each image item
data class ImageItem(
    val imageResId: Int,
    val caption: String
)

@Composable
fun ImageGalleryApp() {
    // Define your image list - make sure these image names match your actual files
    val imageList = listOf(
        ImageItem(R.drawable.toyota, "First Image: toyota"),
        ImageItem(R.drawable.volkswagen, "Second Image: volkswagen"),
        ImageItem(R.drawable.wiesmann, "Third Image: wiesmann"),
        // Add more images as needed
    )

    // State for tracking the current image index
    var currentIndex by remember { mutableStateOf(0) }

    // State for the text field
    var inputText by remember { mutableStateOf("") }

    // Focus manager to clear focus after submitting
    val focusManager = LocalFocusManager.current

    // Navigation functions
    fun goToNext() {
        currentIndex = (currentIndex + 1) % imageList.size
    }

    fun goToPrevious() {
        currentIndex = if (currentIndex > 0) {
            currentIndex - 1
        } else {
            imageList.size - 1
        }
    }

    fun goToIndex(indexStr: String) {
        val index = indexStr.toIntOrNull()
        if (index != null && index > 0 && index <= imageList.size) {
            currentIndex = index - 1  // Adjust for 0-based index
            inputText = ""
            focusManager.clearFocus()
        }
    }

    // Main UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Image Gallery",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Image display
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Image(
                painter = painterResource(id = imageList[currentIndex].imageResId),
                contentDescription = "Image ${currentIndex + 1}",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Caption
        Text(
            text = imageList[currentIndex].caption,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        // Navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { goToPrevious() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Previous")
            }

            Button(onClick = { goToNext() }) {
                Text("Next")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }

        // Direct navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Go to image: ",
                modifier = Modifier.padding(end = 8.dp)
            )

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        goToIndex(inputText)
                    }
                ),
                singleLine = true,
                modifier = Modifier.width(80.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { goToIndex(inputText) }
            ) {
                Text("Go")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Display current position
            Text(
                text = "${currentIndex + 1}/${imageList.size}",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ImageGalleryPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ImageGalleryApp()
        }
    }
}