package com.example.workerapp.view.login

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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


    Column(
        modifier = modifier
            .fillMaxSize()
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

        CustomEditTextField(title = "Tài khoản: ", isPasswordTextField = false)

        Spacer(Modifier.height(32.dp))

        CustomEditTextField(title = "Mật khẩu: ", isPasswordTextField = true)

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
                Toast.makeText(context, "clicked login", Toast.LENGTH_SHORT).show()
                navController.navigate(AppRoutes.HOME)
            },
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
                text = stringResource(R.string.register_title)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.register_action),
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
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
}

@Composable
fun CustomEditTextField(
    modifier: Modifier = Modifier,
    title: String = "Email:",
    isPasswordTextField: Boolean = false
) {
    val light_gray = colorResource(R.color.light_gray)
    val keyboardOptions = if (isPasswordTextField) KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
    ) else KeyboardOptions(
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next
    )
    val leadingIcon = if (isPasswordTextField) Icons.Outlined.Password else Icons.Outlined.Email
    val placeholderText = if (isPasswordTextField) "Nhập mật khẩu" else "Nhập email"

    var input by remember { mutableStateOf("") }
    var isVisibility by remember { mutableStateOf(false) }

    Text(title, fontSize = 18.sp)

    Spacer(Modifier.height(4.dp))

    OutlinedTextField(
        value = input,
        onValueChange = { input = it },
        placeholder = {
            Text(
                placeholderText,
                fontStyle = FontStyle.Italic,
                color = light_gray
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
                Text("|", fontSize = 25.sp, color = light_gray)
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
        visualTransformation = if (isVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = Color.Gray,
            focusedBorderColor = colorResource(R.color.orange_primary)
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

