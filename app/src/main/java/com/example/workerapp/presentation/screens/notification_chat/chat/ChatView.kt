package com.example.workerapp.presentation.screens.notification_chat.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.workerapp.R
import com.example.workerapp.data.source.model.ChatUser
import com.example.workerapp.data.source.model.RoomModel
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.presentation.screens.notification_chat.RootViewModel
import com.example.workerapp.utils.TimeUtils
import com.example.workerapp.utils.cached.UserSession
import com.example.workerapp.utils.ext.navigateWithArgs
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: RootViewModel,
) {
    val conversations by viewModel.roomChat.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAllConversations()
    }

    Column(Modifier.fillMaxSize()) {
        if (conversations.isNullOrEmpty()) {
            Text("Không có cuộc trò chuyện nào", modifier = Modifier.padding(16.dp))
        } else {
            conversations.forEach { conversation ->
                val partnerMap = conversation.users.filter {
                    it.key != UserSession.uid
                }

                val partner = partnerMap.values.first()

                RoomItemRow(room = conversation, partner = partner, onClick = {
                    val encodedName = URLEncoder.encode(
                        partner.username,
                        StandardCharsets.UTF_8.toString()
                    )

                    val encodedAvatar = URLEncoder.encode(
                        partner.avatar,
                        StandardCharsets.UTF_8.toString()
                    )

                    navController.navigateWithArgs(
                        AppRoutes.CHAT_DETAIL,
                        args = arrayOf(
                            conversation.roomId,
                            encodedName,
                            encodedAvatar
                        )
                    )
                })
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun RoomItemRow(
    modifier: Modifier = Modifier,
    room: RoomModel,
    partner: ChatUser,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        AsyncImage(
            model = partner.avatar,
            error = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Sender's Avatar",
            modifier = Modifier
                .clip(CircleShape)
                .size(48.dp)
        )

        Spacer(Modifier.width(16.dp))

        Column(Modifier.weight(1f)) {
            Text(partner.username, style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    room.lastMessage,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = colorResource(R.color.subtext)
                    )
                )

                Text(
                    " • " + TimeUtils.formatMessageTime(room.lastTimestamp),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = colorResource(R.color.subtext),
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
