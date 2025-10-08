package com.example.workerapp.presentation.screens.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.utils.ext.openGoogleMap
import com.example.workerapp.utils.ext.popBackIfCan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(modifier: Modifier = Modifier) {
    val tag = "ReviewScreen"
    val context = LocalContext.current

    Column (Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(R.string.review_title),
                    fontWeight = FontWeight.Bold
                )
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = {

                }) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },

            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        LazyColumn {
            item {
                Spacer(Modifier.height(16.dp))
            }

            item {
                repeat(10){
                    ReviewItem()
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun ReviewItem(modifier: Modifier = Modifier) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.childcare),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(16.dp))

        Column(Modifier.weight(1f)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Lê Minh Quang", style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "10 thang 10",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = colorResource(R.color.subtext)
                    )
                )
            }

            Spacer(Modifier.height(4.dp))


            RatingBar(3.5)

            Spacer(Modifier.height(12.dp))

            Text(
                "hello guys. You do so good, i like it", style =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = colorResource(R.color.subtext)
                    )
            )
        }


    }
}

@Composable
fun RatingBar(rating: Double) {
    val fullStars = rating.toInt()
    val maxStars = 5

    Row() {
        repeat(fullStars) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = colorResource(R.color.light_orange_icon),
                modifier = Modifier.size(16.dp)
            )
        }
        repeat(maxStars - fullStars) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(Modifier.width(8.dp))

        Text("$rating", style = MaterialTheme.typography.labelMedium.copy(
            color = colorResource(R.color.subtext)
        ))
    }
}

@Preview (showBackground = true, showSystemUi = true)
@Composable
fun Prev11(modifier: Modifier = Modifier) {
    ReviewScreen()
}