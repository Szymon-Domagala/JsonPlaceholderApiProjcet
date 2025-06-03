package com.example.zadanie4.ui.screens.postList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zadanie4.model.MyProfileScreen
import com.example.zadanie4.model.PostDetailsScreen
import com.example.zadanie4.model.UserDetailsScreen
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.rememberAsyncImagePainter
import com.example.zadanie4.PostListApplication
import com.example.zadanie4.model.UserPrefs

@Composable
fun PostListScreen(
    navController: NavController,
    viewModel: PostListViewModel,
    retryAction: () -> Unit
) {

    val context = LocalContext.current.applicationContext as PostListApplication
    val userPrefsRepo = context.container.userPreferencesRepository
    val userPrefs by userPrefsRepo.userFlow.collectAsState(initial = UserPrefs("", "", ""))

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
            },
            onProfileClick = { navController.navigate(MyProfileScreen) },
            userPrefs = userPrefs
        )
    }
}


@Composable
fun PostListContent(
    posts: List<PostListViewModel.PostWithUser>,
    onPostClick: (PostListViewModel.PostWithUser) -> Unit,
    onUserClick: (Int) -> Unit,
    onProfileClick: () -> Unit,
    userPrefs: UserPrefs
) {
    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Posts",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            ) {
                if (userPrefs.profileImagePath.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(userPrefs.profileImagePath),
                        contentDescription = "Profilowe",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profil",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
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
                HorizontalDivider()
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