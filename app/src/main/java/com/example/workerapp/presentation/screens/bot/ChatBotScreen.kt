package com.example.workerapp.presentation.screens.bot

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.workerapp.R
import com.example.workerapp.data.source.remote.dto.response.ChatbotJobResponse
import com.example.workerapp.presentation.screens.map.LocationPermissionHandler
import com.example.workerapp.presentation.screens.notification_chat.chat.ReceiverRow
import com.example.workerapp.presentation.screens.notification_chat.chat.SendMessageBar
import com.example.workerapp.presentation.screens.notification_chat.chat.SenderRow
import com.example.workerapp.utils.StringUtils
import com.example.workerapp.utils.cached.UserSession
import com.example.workerapp.utils.ext.popBackIfCan
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

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
    val chatbotResponse by viewModel.chatbotResponse.collectAsState()

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
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
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
                if (input.isNotEmpty())
                    viewModel.sendMessage(input.trim())
            })
        }, modifier = Modifier.imePadding()
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {
            itemsIndexed(chatbotResponse) { index, message ->

                when (message) {
                    is ChatbotResponseUiModel.JobResponse -> {
                        val partnerAvatar = painterResource(R.drawable.icon_bot).toString()
                        JobResponseRow(partnerAvatar = partnerAvatar, content = StringUtils.extractIntroAndOutro(message.text), jobs = message.listJobs)
                    }
                    is ChatbotResponseUiModel.TextResponse -> {
                        if (message.userUid == UserSession.uid) {
                            SenderRow(
                                currentUserAvatar = UserSession.userProfilePicUrl ?: "",
                                content = message.text
                            )
                        } else {
                            val partnerAvatar = painterResource(R.drawable.icon_bot).toString()
                            ReceiverRow(partnerAvatar = partnerAvatar, content = message.text)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JobResponseRow(
    modifier: Modifier = Modifier,
    partnerAvatar: String,
    content: String,
    jobs: List<ChatbotJobResponse>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {

        // Avatar
        AsyncImage(
            model = partnerAvatar,
            error = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Avatar",
            modifier = Modifier
                .clip(CircleShape)
                .size(32.dp)
        )

        Spacer(Modifier.width(8.dp))

        // Bubble Container
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color.LightGray.copy(alpha = 0.3f))
                .padding(12.dp)
        ) {
            // Bot message
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    color = Color.Black
                ),

                )

            Spacer(Modifier.height(8.dp))

            // Job list
            jobs.forEach { job ->
                ChatbotJobCard(job = job)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ChatbotJobCard(job: ChatbotJobResponse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {
        Text(
            text = job.serviceType,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = job.location,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "💰 ${job.price}đ   ⏰ ${job.startTime}",
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = "📅 ${job.listDays.joinToString()}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
fun PreviewJobResponseRow(modifier: Modifier = Modifier) {
    val partnerAvatar = painterResource(R.drawable.icon_bot).toString()
    val mockJobs = listOf(
        ChatbotJobResponse(
            context = "Giúp việc theo giờ, dọn dẹp phòng khách và bếp.",
            createdAt = "2025-01-10T14:32:00Z",
            jobID = "JOB_001",
            lat = 10.762622,
            listDays = listOf("Monday", "Wednesday", "Friday"),
            location = "Quận 1, TP. Hồ Chí Minh",
            lon = 106.660172,
            price = 150000,
            serviceType = "CLEANING",
            similarityScore = 0.92,
            startTime = "08:00",
            userID = "USER_1001"
        ),
        ChatbotJobResponse(
            context = "Trông trẻ 3 tuổi trong 4 giờ buổi chiều.",
            createdAt = "2025-01-11T09:15:00Z",
            jobID = "JOB_002",
            lat = 21.028511,
            listDays = listOf("Tuesday", "Thursday"),
            location = "Cầu Giấy, Hà Nội",
            lon = 105.804817,
            price = 250000,
            serviceType = "BABYSITTING",
            similarityScore = 0.87,
            startTime = "14:00",
            userID = "USER_2002"
        ),
        ChatbotJobResponse(
            context = "Chăm sóc người lớn tuổi, hỗ trợ ăn uống và đi lại.",
            createdAt = "2025-01-12T18:50:00Z",
            jobID = "JOB_003",
            lat = 10.823099,
            listDays = listOf("Saturday", "Sunday"),
            location = "Bình Thạnh, TP. Hồ Chí Minh",
            lon = 106.629664,
            price = 300000,
            serviceType = "ELDERCARE",
            similarityScore = 0.94,
            startTime = "09:30",
            userID = "USER_3003"
        )
    )

    JobResponseRow(
        partnerAvatar = partnerAvatar,
        content = "Dưới đây là phiên bản nâng cấp của JobResponseRow để hiển thị tin nhắn trả về + danh sách job gợi ý một cách đẹp – rõ ràng – UI kiểu chat.\n",
        jobs = mockJobs
    )
}