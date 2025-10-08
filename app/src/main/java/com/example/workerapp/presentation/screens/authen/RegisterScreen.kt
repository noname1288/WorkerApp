package com.example.workerapp.presentation.screens.authen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.popBackIfCan

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel, navController: NavController
) {
    val context = LocalContext.current

    val registerState by viewModel.registerState.collectAsState()

    var displayName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp)
            .imePadding(),
        horizontalAlignment = Alignment.Start
    ) {
        /* *
        * Header
        * */
        item {
            Text(
                text = stringResource(R.string.register_action),
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.orange_primary)
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.register_body),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = colorResource(R.color.orange_primary),
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(Modifier.height(12.dp))
        }

        /* *
        * Avatar
        * */
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box {
                    Image(
                        painterResource(R.drawable.ic_launcher_background),
                        null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        /* *
        * Form
        * */
        item {
            CustomEditTextField(
                leadingIcon = Icons.Default.Person2,
                title = "Tên hiển thị",
                placeholderText = "Nhập tên hiển thị",
                isPasswordTextField = false,
                onTextChange = { displayName = it }
            )

            Spacer(Modifier.height(12.dp))

            CustomEditTextField(
                leadingIcon = Icons.Default.Email,
                title = "Tài khoản",
                placeholderText = "Nhập email",
                onTextChange = { email = it }
            )

            Spacer(Modifier.height(12.dp))

            CustomEditTextField(
                leadingIcon = Icons.Default.Password,
                title = "Mật khẩu",
                placeholderText = "Nhập mật khẩu",
                isPasswordTextField = true,
                onTextChange = { password = it }
            )

            Spacer(Modifier.height(24.dp))
        }

        /* *
        * Button Register
        * */
        item {
            Button(
                onClick = {
                    viewModel.registerWithForm(
                        displayName, email, password, imageUrl
                    )

                    //clear form
                    displayName = ""
                    email = ""
                    password = ""
                    imageUrl = null
                },
                enabled = registerState !is AuthenticationUIState.Loading,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonColors(
                    containerColor = colorResource(R.color.orange_primary),
                    contentColor = Color.White,
                    disabledContainerColor = colorResource(R.color.light_gray),
                    disabledContentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    stringResource(R.string.register_button),
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.ban_da_co_tai_khoan)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.login_title),
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    color = colorResource(R.color.orange_primary),
                    modifier = Modifier.clickable {
                        navController.popBackIfCan()
                    }
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }

    when (registerState) {
        is AuthenticationUIState.Success -> {
            Toast.makeText(
                context,
                "Register successfully! Please login to continue.",
                Toast.LENGTH_LONG
            ).show()

            viewModel.clearState()

            //navigate to login screen
            navController.popBackIfCan()
        }

        is AuthenticationUIState.Error -> {
            Toast.makeText(
                context,
                (registerState as AuthenticationUIState.Error).message,
                Toast.LENGTH_LONG
            ).show()
        }

        AuthenticationUIState.Idle -> {}

        AuthenticationUIState.Loading -> {
            CircleLoadingIndicator()
        }
    }
}
