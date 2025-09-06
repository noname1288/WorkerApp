package com.example.workerapp.view.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Domain
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.outlined.WorkHistory
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R
import com.example.workerapp.data.model.base.UserModel
import com.example.workerapp.view.profile.components.CustomExtendedButton
import com.example.workerapp.view.profile.components.UserCard

sealed class ProfileSection {
    object Avatar : ProfileSection()
    data class UserInfo(val user: UserModel) : ProfileSection()
    object Settings : ProfileSection()
    object Logout : ProfileSection()
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val fakeUser = UserModel(
        username = "Phạm Thanh Sơn",
        gender = "Male",
        dob = "1990-01-01",
        avatar = "https://example.com/avatar.jpg",
        tel = "1234567890",
        location = "New York",
        email = "john.doe@example.com",
        role = "user"
    )
    val sections = listOf(
        ProfileSection.Avatar,
        ProfileSection.UserInfo(fakeUser),
        ProfileSection.Settings,
        ProfileSection.Logout
    )

    LazyColumn(
        modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {

        item { Spacer(Modifier.height(16.dp)) }

        sections.forEach { section ->
            when (section) {
                is ProfileSection.Avatar -> {
                    item {
                        CustomAvatar(
                            onEditClick = {},
                            onChangePasswordClick = {}
                        )
                        Spacer(Modifier.height(24.dp))
                    }
                }

                is ProfileSection.UserInfo -> {
                    item {
                        UserCard(section.user)
                        Spacer(Modifier.height(24.dp))
                    }
                }

                is ProfileSection.Settings -> {
                    item {
                        SettingButtons()

                        Spacer(Modifier.height(24.dp))

                        Spacer(Modifier.height(24.dp))

                        Divider(
                            color = Color.LightGray,       // màu xám
                            thickness = 1.dp,         // độ dày
                            modifier = Modifier.fillMaxWidth() // kéo ngang hết
                        )

                        Spacer(Modifier.height(24.dp))
                    }
                }

                is ProfileSection.Logout -> {
                    item {
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.light_orange_icon)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.logout_title), color = Color.White)
                        }
                        Spacer(Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CustomAvatar(
    onEditClick: () -> Unit,
    onChangePasswordClick: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        // Nút đổi mật khẩu
        Text(
            text = "Đổi mật khẩu",
            color = Color(0xFF1565C0), // xanh dương
            fontStyle = FontStyle.Italic,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onChangePasswordClick() }
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            // Avatar
            Image(
                painter = painterResource(id = R.drawable.avt), // ảnh từ drawable
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )

            // Nút edit
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFFFF9800), CircleShape) // màu cam
                    .border(2.dp, Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }


    }
}


@Composable
fun SettingButtons(
    modifier: Modifier = Modifier,
    onHistoryClick: () -> Unit = {},
    onTermClick: () -> Unit = {},
    onSupportClick: () -> Unit = {},
    onSettingClick: () -> Unit = {}
    ) {
    val internalModifier = Modifier.fillMaxWidth()
    Column(modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = {},
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, colorResource(R.color.light_orange_icon)),
            modifier = internalModifier
        ) {
            Text(
                stringResource(R.string.edit_profile_title),
                color = colorResource(R.color.orange),
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(24.dp))

        Divider(
            color = Color.LightGray,       // màu xám
            thickness = 1.dp,         // độ dày
            modifier = Modifier.fillMaxWidth() // kéo ngang hết
        )

        Spacer(Modifier.height(24.dp))



        CustomExtendedButton(
            label = stringResource(R.string.job_history_title),
            leadingIcon = Icons.Outlined.WorkHistory,
            onClick = {onHistoryClick()}
        )

        Spacer(Modifier.height(12.dp))

        CustomExtendedButton(
            label = stringResource(R.string.term_title),
            leadingIcon = Icons.Outlined.Domain,
            onClick = {onTermClick()}
        )

        Spacer(Modifier.height(12.dp))

        CustomExtendedButton(
            label = stringResource(R.string.support_title),
            leadingIcon = Icons.Outlined.SupportAgent,
            onClick = {onSupportClick()}
        )

        Spacer(Modifier.height(12.dp))

        CustomExtendedButton(
            label = stringResource(R.string.setting_title),
            leadingIcon = Icons.Outlined.Settings,
            onClick = {onSettingClick()}
        )

        Spacer(Modifier.height(12.dp))


    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen()
}
