package com.example.zadanie4.ui.screens.userDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zadanie4.model.Todo

@Composable
fun UserDetailsScreen(
    userId: Int,
    onBackClick: () -> Unit,
    viewModel: UserDetailsViewModel
) {
    val uiState = viewModel.uiState

    LaunchedEffect(userId) {
        viewModel.loadUserAndTodos(userId)
    }

    when (uiState) {
        is UserDetailsViewModel.UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UserDetailsViewModel.UiState.Error -> ErrorScreen(retryAction = { viewModel.loadUserAndTodos(userId) })


        is UserDetailsViewModel.UiState.Success -> {
            val user = uiState.user
            val todos = uiState.todos
            Column(modifier = Modifier.fillMaxSize()){

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wróć")
                    }
                    Text(
                        text = "Szczegóły użytkownika",
                        modifier = Modifier.weight(1f),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }

                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {

                    item {
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Imię i nazwisko: ")
                                }
                                append(user.name)
                            },
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Nazwa użytkownika: ")
                                }
                                append(user.username)
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Email: ")
                                }
                                append(user.email)
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Telefon: ")
                                }
                                append(user.phone)
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Strona internetowa: ")
                                }
                                append(user.website)
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Adres:\n")
                                }
                                append("${user.address.zipcode} ${user.address.city}\n")
                                append("${user.address.suite} ${user.address.street}")
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Firma: ")
                                }
                                append(user.company.name)
                            }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Zadania (Todos):", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(todos) { todo ->
                        TodoItem(todo)
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItem(todo: Todo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (todo.completed) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = todo.title,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = if (todo.completed) "✔" else "❌",
                style = MaterialTheme.typography.bodyLarge
            )
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
        Text("Błąd: Nie udało się wczytać szczegółów użytkownika", color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = retryAction) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Spróbuj ponownie")
        }
    }
}
