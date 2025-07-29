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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.moseti.cutecats.R
import com.moseti.cutecats.data.local.UserUpload
import com.moseti.cutecats.ui.viewmodels.CatViewModel
import java.io.File


private enum class UploadsTab(val title: String) {
    UPLOAD_NEW("Upload"),
    MY_UPLOADS("My Uploads")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadsScreen(
    catViewModel: CatViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = UploadsTab.entries

    Column(modifier = modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = tab.title) }
                )
            }
        }

        // Content for the selected tab
        when (tabs[selectedTabIndex]) {
            UploadsTab.UPLOAD_NEW -> UploadNewCatTab(catViewModel = catViewModel)
            UploadsTab.MY_UPLOADS -> MyUploadsTab(catViewModel = catViewModel)
        }
    }
}

@Composable
private fun UploadNewCatTab(catViewModel: CatViewModel) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (selectedImageUri == null) {
            // Initial view: prompt to select an image
            Icon(
                painter = painterResource(R.drawable.baseline_cloud_upload_24),
                contentDescription = "Upload Icon",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
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
            // Preview view: show image and actions
            Text("Image Preview", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            UploadPreviewCard(uri = selectedImageUri!!)
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { selectedImageUri = null },
                    modifier = Modifier.weight(1f)
                ) { Text("Cancel") }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        // Use the ViewModel to save the upload!
                        catViewModel.submitUserUpload(selectedImageUri!!)
                        Toast.makeText(context, "Saved to your uploads!", Toast.LENGTH_SHORT).show()
                        selectedImageUri = null // Clear preview
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Submit") }
            }
        }
    }
}

@Composable
private fun MyUploadsTab(catViewModel: CatViewModel) {
    val myUploads by catViewModel.myUploads.collectAsState()

    if (myUploads.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "You haven't uploaded any cats yet.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(myUploads, key = { it.id }) { upload ->
                MyUploadCard(upload = upload)
            }
        }
    }
}

@Composable
private fun MyUploadCard(upload: UserUpload) {
    Card(
        modifier = Modifier.aspectRatio(1f),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        AsyncImage(
            model = File(upload.filePath),
            contentDescription = "User uploaded cat",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun UploadPreviewCard(uri: Uri) {
    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "Selected image preview",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}