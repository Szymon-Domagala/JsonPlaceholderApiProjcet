package com.example.zadanie4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.zadanie4.model.PostDetailsScreen
import com.example.zadanie4.model.PostListScreen
import com.example.zadanie4.model.UserDetailsScreen
import com.example.zadanie4.ui.screens.postDetails.PostDetailsScreen
import com.example.zadanie4.ui.screens.postDetails.PostDetailsViewModel
import com.example.zadanie4.ui.screens.postList.PostListScreen
import com.example.zadanie4.ui.screens.postList.PostListViewModel
import com.example.zadanie4.ui.screens.userDetails.UserDetailsScreen
import com.example.zadanie4.ui.screens.userDetails.UserDetailsViewModel
import com.example.zadanie4.ui.theme.Zadanie4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Zadanie4Theme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = PostListScreen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<PostListScreen> {
                            val viewModel: PostListViewModel =
                                viewModel(factory = PostListViewModel.Factory)
                            PostListScreen(
                                navController = navController,
                                viewModel = viewModel,
                                viewModel::loadPostsWithUsers
                            )
                        }

                        composable<PostDetailsScreen> { backStackEntry ->
                            val route = backStackEntry.toRoute<PostDetailsScreen>()
                            val viewModel: PostDetailsViewModel =
                                viewModel(factory = PostDetailsViewModel.Factory)
                            PostDetailsScreen(
                                postId = route.postId,
                                onBackClick = { navController.popBackStack() },
                                onUserClick = { userId ->
                                    navController.navigate(UserDetailsScreen(userId))
                                },
                                viewModel = viewModel
                            )
                        }

                        composable<UserDetailsScreen> { backStackEntry ->
                            val route = backStackEntry.toRoute<UserDetailsScreen>()
                            val viewModel: UserDetailsViewModel =
                                viewModel(factory = UserDetailsViewModel.Factory)
                            UserDetailsScreen(
                                userId = route.userId,
                                onBackClick = { navController.popBackStack() },
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}