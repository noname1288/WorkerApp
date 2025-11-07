package com.example.workerapp.presentation.screens.policy

import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.utils.components.CircleLoadingIndicator
import com.example.workerapp.utils.ext.popBackIfCan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicyScreen(modifier: Modifier = Modifier, viewModel: PolicyViewModel, navController: NavController) {
    val context = LocalContext.current

    val content by viewModel.policy.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val error by viewModel.error.collectAsState()

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Column(Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(R.string.policy_title),
                    fontWeight = FontWeight.Bold
                )
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackIfCan()
                }) {
                    Icon(
                        Icons.Default.ArrowBackIosNew, contentDescription = "Back",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        if (loading) {
            Box(Modifier.fillMaxSize()) {
                CircleLoadingIndicator()
            }
        } else {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = false
                        settings.defaultTextEncodingName = "utf-8"
                        webViewClient = WebViewClient() // để mở link trong WebView
                        loadDataWithBaseURL(null, content?.htmlContent ?: "Long time no see", "text/html", "utf-8", null)
                    }
                },
                update = { webView ->
                    webView.loadDataWithBaseURL(
                        null,
                        content?.htmlContent ?: "Long time no see",
                        "text/html",
                        "utf-8",
                        null
                    )
                }
            )
        }
    }
}