package com.verrecchia.demo.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.verrecchia.demo.R
import com.verrecchia.touchtunes_domain.Album

@Composable
fun AlbumItem(album: Album, onAlbumClick: (Album) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAlbumClick(album) }
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = album.artwork,
                placeholder = painterResource(id = R.drawable.baseline_album_24),
                error = painterResource(id = R.drawable.baseline_album_error_24)
            ),
            contentDescription = "Album artwork",
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = album.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
            Text(
                text = album.releaseDate,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun AlbumDialog(album: Album, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Ok")
            }
        },
        title = {
            Text(
                text = album.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = { AlbumDetails(album = album) }
    )
}


@Composable
fun AlbumDetails(album: Album) {
    Box(contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Genre",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = album.genre,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Price",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "${album.price} $ ${album.currency}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Copyright",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = album.copyright,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
