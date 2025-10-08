package com.example.workerapp.presentation.screens.authen

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.safeNavigate
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val credentialManager = remember { CredentialManager.create(context) }

    val scrollState = rememberScrollState()
    val loginState by viewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
            .background(Color.White)
            .padding(32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.login_title),
            style = TextStyle(
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.orange_primary)
            )
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.login_body),
            style = TextStyle(
                fontSize = 12.sp,
                color = colorResource(R.color.orange_primary),
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(Modifier.height(64.dp))

        CustomEditTextField(
            leadingIcon = Icons.Default.Email,
            title = "Tài khoản: ",
            placeholderText = "Nhập email",
            isPasswordTextField = false,
            onTextChange = {
                email = it
            }
        )

        Spacer(Modifier.height(32.dp))

        CustomEditTextField(
            leadingIcon = Icons.Default.Password,
            title = "Mật khẩu: ",
            placeholderText = "Nhập mật khẩu",
            isPasswordTextField = true,
            onTextChange = {
                password = it
            }
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.forgot_password_title),
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.End,
            modifier = Modifier
                .clickable {
                    Toast.makeText(context, "clicked quên mật khẩu", Toast.LENGTH_SHORT).show()
                }
                .fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.loginWithEmailAndPassword(email, password)
//                viewModel.changeLoginState(AuthenticationUIState.Success("Login successful"))

                // clear form
                email = ""
                password = ""

            },
            enabled = loginState !is AuthenticationUIState.Loading,
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
                stringResource(R.string.login_title),
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.ban_chua_co_tai_khoan)
            )

            Spacer(Modifier.width(8.dp))

            /*
            * Business Logic: Worker can't create new account by yourself
            * */
            Text(
                text = stringResource(R.string.register_action),
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                color = colorResource(R.color.orange_primary),
            )
        }

        Spacer(Modifier.height(32.dp))

        GoogleSignInButton {
            val request = viewModel.request
            activity.lifecycleScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        context = activity,
                        request = request
                    )
                    viewModel.onGoogleSignInSuccess(result)
                } catch (e: Exception) {
                    viewModel.onGoogleSignInError(e)
                }
            }
        }
    }

    when (loginState) {
        is AuthenticationUIState.Success -> {
            Toast.makeText(
                context,
                "Login successfully",
                Toast.LENGTH_LONG
            ).show()

            viewModel.clearState()

            // Navigate to home screen + clear login screen from back stack
            navController.safeNavigate(
                AppRoutes.HOME,
                AppRoutes.LOGIN,
                inclusive = true,
                restore = false
            )
        }

        is AuthenticationUIState.Error -> {
            Toast.makeText(
                context,
                (loginState as AuthenticationUIState.Error).message,
                Toast.LENGTH_LONG
            ).show()
        }

        AuthenticationUIState.Idle -> {}

        AuthenticationUIState.Loading -> {
            CircleLoadingIndicator()
        }
    }
}


@Composable
fun CustomEditTextField(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    title: String = "Email:",
    placeholderText: String = "Nhập email",
    onTextChange: (String) -> Unit,
    isPasswordTextField: Boolean = false
) {
    val lightGray = colorResource(R.color.light_gray)

    val keyboardOptions = if (isPasswordTextField) KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
    ) else KeyboardOptions(
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next
    )

    var input by remember { mutableStateOf("") }
    var isVisibility by remember { mutableStateOf(false) }

    Text(
        title,
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(Modifier.height(4.dp))

    OutlinedTextField(
        value = input,
        onValueChange = {
            input = it
            onTextChange(it)
        },
        placeholder = {
            Text(
                placeholderText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontStyle = FontStyle.Italic,
                    color = lightGray
                )
            )
        },
        leadingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text("|", fontSize = 25.sp, color = lightGray)
                Spacer(Modifier.width(8.dp))
            }
        },
        trailingIcon = {
            if (isPasswordTextField) {
                val image =
                    if (isVisibility) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff

                IconButton(
                    onClick = { isVisibility = !isVisibility }
                ) { Icon(imageVector = image, null) }
            }


        },
        visualTransformation = if (!isVisibility && isPasswordTextField) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedBorderColor = colorResource(R.color.orange_primary),
            unfocusedBorderColor = lightGray,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding()
    )

}

@Composable
fun GoogleSignInButton(
    text: String = "Đăng nhập với Google",
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),   // bo góc
        border = BorderStroke(1.dp, Color.Gray),
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Google logo (drawable)
            Image(
                painter = painterResource(id = R.drawable.icons8_google_48), // đặt logo google vào drawable
                contentDescription = "Google logo",
                modifier = Modifier
                    .size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}



