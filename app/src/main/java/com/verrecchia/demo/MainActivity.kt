package com.verrecchia.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.verrecchia.demo.album.Event
import com.verrecchia.demo.album.Intent
import com.verrecchia.demo.album.ViewState
import com.verrecchia.touchtunes_domain.Album
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val demoViewModel: DemoViewModel = hiltViewModel()
                DemoScreen(
                    viewState = demoViewModel.viewState.collectAsState().value,
                    handleIntent = demoViewModel::handleIntent,
                    event = demoViewModel.event
                )
            }
        }
    }

    @Composable
    private fun DemoScreen(
        viewState: ViewState,
        handleIntent: (Intent) -> Unit,
        event: SharedFlow<Event>
    ) {
        val context = LocalContext.current
        var selectedAlbum by remember { mutableStateOf<Album?>(null) }
        LaunchedEffect(event) {
            event.collect { currentEvent ->
                when (currentEvent) {
                    is Event.ShowErrorToast -> {
                        Toast.makeText(context, currentEvent.message, Toast.LENGTH_SHORT).show()
                    }
                    is Event.ShowAlbumDetails -> {
                        selectedAlbum = currentEvent.album // Show dialog with selected album
                    }
                }
            }
        }
        Column {
            SearchBar { query -> handleIntent(Intent.Search(query)) }
            when (viewState) {
                is ViewState.Idle -> IdleScreen()
                is ViewState.Loading -> LoadingIndicator()
                is ViewState.SearchResults -> SearchResultsScreen(
                    albums = viewState.albums,
                    onAlbumClick = { album -> handleIntent(Intent.AlbumClicked(album)) }
                )

                is ViewState.Error -> ErrorScreen(
                    message = viewState.message,
                    onRetry = { handleIntent(Intent.Retry) }
                )

                is ViewState.Empty -> EmptyScreen()
            }
        }
        selectedAlbum?.let { album ->
            AlbumDialog(album = album) { selectedAlbum = null }
        }
    }

    @Composable
    private fun LoadingIndicator() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    private fun SearchBar(onSearch: (String) -> Unit) {
        var query by remember { mutableStateOf("") }
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search Albums with artist name") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch(query) }
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    private fun IdleScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Hello :)")
        }
    }

    @Composable
    private fun ErrorScreen(message: String, onRetry: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            var errorVisible by remember { mutableStateOf(false) }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "error occurred",
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "nerd stuff",
                    modifier = Modifier.clickable {
                        errorVisible = !errorVisible
                    }
                )
                if (errorVisible) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onRetry() }) {
                    Text("Retry")
                }
            }
        }
    }


    @Composable
    private fun EmptyScreen() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No results found")
        }
    }

    @Composable
    private fun SearchResultsScreen(albums: List<Album>, onAlbumClick: (Album) -> Unit) {
        val keyboardController = LocalSoftwareKeyboardController.current
        LaunchedEffect(Unit) {
            keyboardController?.hide()
        }
        LazyColumn {
            items(
                count = albums.size,
                key = { index -> albums[index].id }
            ) { index ->
                val album = albums[index]
                AlbumItem(album, onAlbumClick)
            }
        }
    }
}





