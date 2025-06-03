package com.example.zadanie4.ui.screens.myProfile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.io.FileOutputStream
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.zadanie4.model.UserPrefs


@Composable
fun MyProfileScreen(
    viewModel: MyProfileViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is MyProfileViewModel.UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is MyProfileViewModel.UiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Błąd wczytywania profilu")
            }
        }

        is MyProfileViewModel.UiState.Success -> {
            val userPrefs = (uiState as MyProfileViewModel.UiState.Success).userPrefs
            ProfileForm(
                userPrefs = userPrefs,
                onSave = { firstName, lastName, imagePath ->
                    viewModel.updateUser(firstName, lastName, imagePath)
                    onBackClick()
                },
                onBackClick = onBackClick,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ProfileForm(
    userPrefs: UserPrefs,
    onSave: (String, String, String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: MyProfileViewModel
) {
    val context = LocalContext.current

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var imagePath by rememberSaveable { mutableStateOf("") }
    var tempImagePath by rememberSaveable { mutableStateOf("") }


    LaunchedEffect(userPrefs) {
        imagePath = userPrefs.profileImagePath
        tempImagePath = userPrefs.profileImagePath
        firstName = userPrefs.firstName
        lastName = userPrefs.lastName
    }


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val file = File(context.filesDir, "temp_profile.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            tempImagePath = file.toUri().toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Edytuj profil",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Wróć",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (tempImagePath.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = tempImagePath),
                        contentDescription = "Zdjęcie profilowe",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Domyślna ikona profilu",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        tint = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(150.dp)
            ) {
                Text("Wybierz zdjęcie")
            }
            Button(
                onClick = {
                    val file = File(context.filesDir, "profile.jpg")
                    if (file.exists()) {
                        file.delete()
                    }

                    tempImagePath = ""
                    imagePath = ""

                    onSave(firstName, lastName, "")
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(150.dp)
            ) {
                Text("Usuń zdjęcie")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Imię") },
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nazwisko") },
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tryb ciemny")
                Switch(
                    checked = userPrefs.isDarkTheme,
                    onCheckedChange = {
                        viewModel.updateDarkTheme(it)
                    }
                )
            }

        }

        Button(
            onClick = {
                val tempFile = File(context.filesDir, "temp_profile.jpg")
                val finalFile = File(context.filesDir, "profile.jpg")

                if (tempFile.exists()) {
                    tempFile.copyTo(finalFile, overwrite = true)
                }

                onSave(firstName, lastName, finalFile.toUri().toString())

            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(150.dp)
        ) {
            Text("Zapisz")
        }
    }
}