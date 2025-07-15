package com.moseti.cutecats.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.moseti.cutecats.R
import com.moseti.cutecats.ui.viewmodels.CatViewModel

@Composable
fun UploadsPage(
    catViewModel: CatViewModel, // ViewModel is kept for future use (e.g., calling an upload function)
    modifier: Modifier = Modifier
) {
    // This state will hold the URI of the selected image.
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // This is the modern way to handle activity results.
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            // The URI of the selected image is passed here.
            selectedImageUri = uri
        }
    )

    val context = LocalContext.current

    // The UI will change depending on whether an image has been selected.
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (selectedImageUri == null) {
            // --- INITIAL VIEW ---
            // This view is shown when no image is selected yet.
            Icon(
                painter = painterResource(R.drawable.baseline_cloud_upload_24),
                contentDescription = "Upload Icon",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Upload a photo of your cat",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Share the cuteness with the world!",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Text("Select Image from Gallery")
            }
        } else {
            // --- PREVIEW VIEW ---
            // This view is shown after an image has been selected.
            Text("Image Preview", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Custom Preview Card
            UploadPreviewCard(uri = selectedImageUri!!)

            Spacer(modifier = Modifier.height(24.dp))

            // 2. AI Confirmation Button
            Button(
                onClick = {
                    // TODO: Implement your AI model check here.
                    Toast.makeText(context, "AI Check coming soon!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Run AI Confirmation")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Row for Cancel and Submit Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Cancel Button
                OutlinedButton(
                    onClick = {
                        // Clear everything by setting the URI back to null
                        selectedImageUri = null
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Submit Button
                Button(
                    onClick = {
                        // TODO: Implement your upload logic here.
                        // You would take selectedImageUri and pass it to a function
                        // in your ViewModel, e.g., catViewModel.uploadImage(selectedImageUri!!)
                        Toast.makeText(context, "Uploading... (Not really!)", Toast.LENGTH_SHORT).show()
                        selectedImageUri = null // Go back to initial state after "uploading"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Submit")
                }
            }
        }
    }
}

/**
 * A custom Composable card specifically for previewing the user's selected image
 * before uploading.
 *
 * @param uri The content URI of the image to display.
 */
@Composable
private fun UploadPreviewCard(uri: Uri) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f), // Makes the card a square
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        Box(contentAlignment = Alignment.Center) {
            // AsyncImage from Coil is perfect for loading the image from a URI
            AsyncImage(
                model = uri,
                contentDescription = "Selected image preview",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // You could add a text overlay here if you wanted
            // Text("Preview", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}