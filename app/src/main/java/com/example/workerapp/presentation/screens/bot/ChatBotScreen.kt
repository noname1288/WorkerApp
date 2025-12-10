package com.example.workerapp.presentation.screens.bot

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.presentation.screens.map.LocationPermissionHandler
import com.example.workerapp.presentation.screens.notification_chat.chat.ChatDetailViewModel
import com.example.workerapp.presentation.screens.notification_chat.chat.ReceiverRow
import com.example.workerapp.presentation.screens.notification_chat.chat.SendMessageBar
import com.example.workerapp.presentation.screens.notification_chat.chat.SenderRow
import com.example.workerapp.utils.cached.UserSession
import com.example.workerapp.utils.ext.popBackIfCan
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import kotlin.text.trim

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotScreen(
    modifier: Modifier = Modifier, navController: NavController, viewModel: ChatbotViewModel
) {
    val context = LocalContext.current
    val fused by remember { mutableStateOf(LocationServices.getFusedLocationProviderClient(context)) }

    var showPermissionDialog by rememberSaveable { mutableStateOf(true) }
    var hasLocationPermission by rememberSaveable { mutableStateOf(false) }

    val message by viewModel.messageList.collectAsState()

    if (showPermissionDialog) {
        LocationPermissionHandler {
            showPermissionDialog = false
            hasLocationPermission = true
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            val cts = CancellationTokenSource()
            try {
                val loc = fused.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY, cts.token
                ).await()

                loc?.let {
                    viewModel.updateCurrentLocation("Vi tri hien tai", it.latitude, it.longitude)
                }
            } catch (e: SecurityException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {

            }

        }
    }

    Scaffold(
        topBar = {
        TopAppBar(
            title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painterResource(R.drawable.ic_chatbot_48),
                    null,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Trợ lý ảo", style = MaterialTheme.typography.titleLarge, maxLines = 1
                )
            }
        },
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
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            windowInsets = WindowInsets(0, 0, 0, 0),
            modifier = Modifier.shadow(
                elevation = 3.dp
            )
        )
    }, bottomBar = {
        SendMessageBar(onSend = { input ->

        })
    }, modifier = Modifier.imePadding()
    ) { innerPadding ->
        LazyColumn(
            Modifier
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
                    val partnerAvatar = painterResource(R.drawable.icon_bot).toString()
                    ReceiverRow(partnerAvatar = partnerAvatar, content = message.message)
                }
            }
        }
    }
}

fun requestAccurateLocation(onResult: (LatLng?) -> Unit) {

}
