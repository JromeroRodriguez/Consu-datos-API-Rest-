package com.example.aservicer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aservicer.model.Post
import com.example.aservicer.ui.theme.AServicerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AServicerTheme {
                MyApp(PostViewModel())
            }
        }
    }
}

@Composable
fun MyApp(viewModel: PostViewModel) {
    val state by viewModel.uiState

    when (state) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            val successState = state as UiState.Success
            PostList(posts = successState.posts)
        }

        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${(state as UiState.Error).message}")
            }
        }
    }
}

@Composable
fun PostList(posts: List<Post>) {
    LazyColumn(modifier = Modifier.padding(20.dp)) {
        items(posts) { post ->
            PostItem(post = post)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Card(modifier = Modifier.padding(10.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = post.body)
        }
    }
}
