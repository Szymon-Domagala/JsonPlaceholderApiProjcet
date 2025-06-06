package com.example.zadanie4.ui.screens.postDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PostDetailsScreen(
    postId: Int,
    onBackClick: () -> Unit,
    onUserClick: (Int) -> Unit,
    viewModel: PostDetailsViewModel
) {
    val uiState = viewModel.uiState

    LaunchedEffect(postId) {
        viewModel.loadPost(postId)
    }

    when (uiState) {
        is PostDetailsViewModel.UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is PostDetailsViewModel.UiState.Error -> ErrorScreen(retryAction = { viewModel.loadPost(postId) })


        is PostDetailsViewModel.UiState.Success -> {
            val post = uiState.post
            val user = uiState.user
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Post",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Wróć",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Tytuł: ")
                            }
                            append(post.title)
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Treść: ")
                            }
                            append(post.body)
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("ID autora: ")
                            }
                            append(user.id.toString())
                        },
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable { onUserClick(user.id) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Wróć")
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Błąd: Nie udało się wczytać szczegółów postu", color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = retryAction) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Spróbuj ponownie")
        }
    }
}