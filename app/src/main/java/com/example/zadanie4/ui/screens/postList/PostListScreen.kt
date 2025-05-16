package com.example.zadanie4.ui.screens.postList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zadanie4.model.PostDetailsScreen
import com.example.zadanie4.model.UserDetailsScreen

@Composable
fun PostListScreen(
    navController: NavController,
    viewModel: PostListViewModel,
    retryAction: () -> Unit
) {
    when (val state = viewModel.uiState) {
        is PostListViewModel.UiState.Loading -> LoadingScreen()
        is PostListViewModel.UiState.Error -> ErrorScreen(retryAction)
        is PostListViewModel.UiState.Success -> PostListContent(
            posts = state.posts,
            onPostClick = { post ->
                navController.navigate(PostDetailsScreen(post.post.id))
            },
            onUserClick = { userId ->
                navController.navigate(UserDetailsScreen(userId))
            }
        )
    }
}


@Composable
fun PostListContent(
    posts: List<PostListViewModel.PostWithUser>,
    onPostClick: (PostListViewModel.PostWithUser) -> Unit,
    onUserClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Posts",
                modifier = Modifier.weight(1f),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(posts) { postWithUser ->
                PostItem(
                    postWithUser,
                    onPostClick = { onPostClick(postWithUser) },
                    onUserClick = onUserClick
                )
                Divider()
            }
        }
    }
}


@Composable
fun PostItem(
    postWithUser: PostListViewModel.PostWithUser,
    onPostClick: () -> Unit,
    onUserClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = postWithUser.post.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.clickable { onPostClick() }
        )
        Spacer(modifier = Modifier.height(4.dp))
        postWithUser.user?.let { user ->
            Text(
                text = "by ${user.name}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onUserClick(user.id) }
            )
        } ?: Text(
            text = "by Unknown",
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
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
        Text("Błąd: Nie udało się wczytać listy postów", color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = retryAction) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Spróbuj ponownie")
        }
    }
}

