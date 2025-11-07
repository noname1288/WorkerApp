package com.example.workerapp.presentation.screens.notification_chat.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.workerapp.R
import com.example.workerapp.utils.cached.UserSession
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.popBackIfCan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    roomId: String,
    partnerName: String,
    partnerAvatar: String,
    navController: NavController,
    viewModel: ChatDetailViewModel
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.messageList.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.observeMessages(roomId)
    }

    LaunchedEffect(message.size) {
        if (message.isNotEmpty()) {
            listState.animateScrollToItem(message.lastIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackIfCan()
                    }) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = partnerAvatar,
                            error = painterResource(R.drawable.ic_launcher_background),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(36.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            partnerName, style = MaterialTheme.typography.titleLarge,
                            maxLines = 1
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            painterResource(R.drawable.ic_information_48), null,
                            tint = colorResource(R.color.blue_button),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.shadow(
                    elevation = 3.dp
                )
            )
        },
        bottomBar = {
            SendMessageBar(onSend = { input ->
                if (!input.isNullOrBlank()){
                    val partnerId = roomId.split("_").first {
                        it != UserSession.uid
                    }

                    viewModel.sendMessage(roomId, partnerId, input.trim())
                }
            })
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {

            itemsIndexed(message) { index, message ->
                if (message.senderId == UserSession.uid) {
                    SenderRow(
                        currentUserAvatar = UserSession.userProfilePicUrl ?: "",
                        content = message.message
                    )
                } else {
                    ReceiverRow(partnerAvatar = partnerAvatar, content = message.message)
                }
            }
        }

        when(uiState){
            is ChatDetailUiState.Error -> {
                Toast.makeText(context, (uiState as ChatDetailUiState.Error).message, Toast.LENGTH_SHORT).show()
            }
            ChatDetailUiState.Idle -> {}
            ChatDetailUiState.Loading -> {
                CircleLoadingIndicator()
            }
            ChatDetailUiState.Success ->{}
        }
    }
}

@Composable
fun SendMessageBar(modifier: Modifier = Modifier, onSend: (String) -> Unit) {
    var input by remember { mutableStateOf("") }

    Row(
        Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
            )
            .background(Color.White)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = {}
        ) {
            Icon(
                painterResource(R.drawable.ic_photo_48), null,
                tint = colorResource(R.color.blue_button),
                modifier = Modifier.size(24.dp)
            )
        }

        TextField(
            value = input,
            onValueChange = { input = it },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,   // Khi focus
                unfocusedIndicatorColor = Color.Transparent, // Khi không focus
                disabledIndicatorColor = Color.Transparent,   // Khi disable
                focusedContainerColor = Color.LightGray.copy(alpha = 0.2f),
                unfocusedContainerColor = Color.LightGray.copy(alpha = 0.2f)
            ),
            textStyle = TextStyle(
                fontSize = 14.sp
            ),
            modifier = Modifier.height(48.dp).weight(1f)

        )

        Spacer(Modifier.width(8.dp))

        IconButton(
            onClick = {
                onSend(input)
                input = ""
            }
        ) {
            Icon(
                painterResource(R.drawable.ic_send_64), null,
                tint = colorResource(R.color.blue_button),
                modifier = Modifier.size(28.dp)
            )
        }

    }
}

@Composable
fun SenderRow(modifier: Modifier = Modifier, currentUserAvatar: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .widthIn(max = 250.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Spacer(Modifier.width(8.dp))

        AsyncImage(
            currentUserAvatar,
            "Avatar B",
            error = painterResource(R.drawable.ic_launcher_background),
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
        )
    }
}

@Composable
fun ReceiverRow(modifier: Modifier = Modifier, partnerAvatar: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = partnerAvatar,
            error = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Avatar",
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
        )

        Spacer(Modifier.width(8.dp))

        Box(
            Modifier
                .widthIn(max = 250.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
