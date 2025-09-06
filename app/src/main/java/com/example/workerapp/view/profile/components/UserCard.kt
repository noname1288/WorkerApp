package com.example.workerapp.view.profile.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.data.model.base.UserModel
import com.example.workerapp.view.components.InfoItemRow

@Composable
fun UserCard(user: UserModel) {
    Column(Modifier
        .fillMaxWidth()
        .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
        .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        InfoItemRow(
            Icons.Outlined.Person,
            user.username,
            width = 12.dp,
            iconSize = 22.dp
        )
        InfoItemRow(
            Icons.Outlined.DateRange,
            user.dob,
            width = 12.dp,
            iconSize = 22.dp
        )
        InfoItemRow(
            Icons.Outlined.Phone,
            user.tel,
            width = 12.dp,
            iconSize = 22.dp
        )
        InfoItemRow(
            Icons.Outlined.Email,
            user.email,
            width = 12.dp,
            iconSize = 22.dp
        )
        InfoItemRow(
            Icons.Outlined.LocationOn,
            user.location,
            width = 12.dp,
            iconSize = 22.dp
        )
    }
}

@Preview (showBackground = true)
@Composable fun PrevUserCard(){
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
    UserCard(fakeUser)
}


