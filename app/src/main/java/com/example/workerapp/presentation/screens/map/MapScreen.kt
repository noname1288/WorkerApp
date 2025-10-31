package com.example.workerapp.presentation.screens.map

import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workerapp.R
import com.example.workerapp.presentation.screens.profile.detail.MapResult
import com.example.workerapp.utils.ext.openGoogleMap
import com.example.workerapp.utils.ext.popBackIfCan
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.tasks.await
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    val fused by remember { mutableStateOf(LocationServices.getFusedLocationProviderClient(context)) }

    // ---- STATE ----
    var pickedLatLng by remember { mutableStateOf<LatLng?>(null) }
    var pickedAddress by remember { mutableStateOf<String?>(null) }
    val cameraPositionState = rememberCameraPositionState()

    var showPermissionDialog by rememberSaveable { mutableStateOf(false) }
    var hasLocationPermission by rememberSaveable { mutableStateOf(false) }
    var requestUpdatePosition by rememberSaveable { mutableStateOf(false) }
    var isMapLoaded by remember { mutableStateOf(false) }

    // ---- MAP UI SETTINGS ----
    val mapProps = MapProperties(isMyLocationEnabled = hasLocationPermission)
    val uiSettings = MapUiSettings(myLocationButtonEnabled = true, zoomControlsEnabled = false)

    var unAvailability = pickedAddress.isNullOrEmpty()

    // ---- Khi được cấp quyền, hoặc khi user yêu cầu cập nhật vị trí, thì lấy current location & animate camera ----
    LaunchedEffect(hasLocationPermission, requestUpdatePosition) {
        if (hasLocationPermission) {
            if (requestUpdatePosition) {
                val cts = CancellationTokenSource()
                try {
                    val loc = fused.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        cts.token
                    ).await()

                    loc?.let {
                        val here = LatLng(it.latitude, it.longitude)
                        pickedLatLng = here
                        val g = Geocoder(context, Locale.getDefault())
                        pickedAddress = g.getFromLocation(here.latitude, here.longitude, 1)
                            ?.firstOrNull()?.getAddressLine(0) ?: "Vị trí hiện tại"

                        cameraPositionState.animate(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition(here, 16f, 0f, 0f)
                            ),
                            durationMs = 800
                        )
                    }
                } catch (_: SecurityException) {
                } catch (_: Exception) {
                } finally {
                    requestUpdatePosition = false
                }
            } else if (pickedLatLng == null) {
                // 🗺️ Mặc định zoom đến Hà Nội nếu đã có quyền nhưng chưa có vị trí
                val hanoi = LatLng(21.0278, 105.8342)
                pickedLatLng = hanoi
                pickedAddress = "Hà Nội, Việt Nam"
                cameraPositionState.move(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition(hanoi, 12f, 0f, 0f)
                    )
                )
            }
        }
    }

    // ---- UI ----
    Column(Modifier.fillMaxSize()) {

        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.map_title), fontWeight = FontWeight.Bold) },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = { navController.popBackIfCan() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
                }
            },
            actions = {
                // Nút My Location trên TopAppBar
                IconButton(onClick = {
                    showPermissionDialog = true
                    requestUpdatePosition = true
                }) {
                    Icon(Icons.Default.LocationOn, contentDescription = "My Location")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        // Hộp thoại xin quyền (hiển thị khi user nhấn nút Location)
        if (showPermissionDialog) {
            LocationPermissionHandler {
                showPermissionDialog = false
                hasLocationPermission = true
                // requestUpdatePosition đã set = true khi bấm nút
            }
        }

        Box(Modifier.weight(1f).fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                onMapLoaded = { isMapLoaded = true },
                properties = mapProps,
                uiSettings = uiSettings,
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    pickedLatLng = latLng
                    val g = Geocoder(context, Locale.getDefault())
                    pickedAddress = g.getFromLocation(latLng.latitude, latLng.longitude, 1)
                        ?.firstOrNull()?.getAddressLine(0) ?: "Không rõ địa chỉ"
                }
            ) {
                pickedLatLng?.let { Marker(state = MarkerState(it), title = pickedAddress) }
            }

            // Nút Confirm chọn vị trí
            Button(
                onClick = {
                    val lat = pickedLatLng?.latitude
                    val lng = pickedLatLng?.longitude
                    val addr = pickedAddress
                    if (lat != null && lng != null && addr != null) {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("map_result", MapResult(addr, lat, lng))
                    }
                    navController.popBackStack()
                },
                enabled = !unAvailability,
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
            ) { Text("Chọn vị trí này") }

            if (!isMapLoaded) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .matchParentSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
                        .wrapContentSize()
                )
            }
        }
    }
}

