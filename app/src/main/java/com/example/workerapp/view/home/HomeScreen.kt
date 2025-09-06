package com.example.workerapp.view.home

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.workerapp.R
import com.example.workerapp.data.repository.remote.dto.JobResponseDto
import com.example.workerapp.navigation.AppRoutes
import com.example.workerapp.utils.TimeUtils

/**
 * Sealed class representing different sections of the Home screen
 */
sealed class HomeSection {
    object Avatar : HomeSection()
    object Search : HomeSection()
    object Category : HomeSection()
    data class JobList(val jobs: List<JobResponseDto>) : HomeSection()
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {


    val currentTime = System.currentTimeMillis()

    val mockJobResponseList = listOf(
        // Future endTime
        JobResponseDto(
            id = "1",
            clientName = "John Doe",
            phoneNumber = "1234567890",
            address = "123 Main St, Cityville",
            workerQuantity = 5,
            serviceType = "Cleaning",
            startTime = currentTime + 3600000, // 1 hour from now
            endTime = currentTime + 5400000, // 1.5 hours from now
            price = 150.000
        ),
        JobResponseDto(
            id = "2",
            clientName = "Jane Smith",
            phoneNumber = "9876543210",
            address = "456 Elm St, Townsville",
            workerQuantity = 3,
            serviceType = "Plumbing",
            startTime = currentTime + 10800000, // 3 hours from now
            endTime = currentTime + 14400000, // 4 hours from now
            price = 200.000
        ),
        JobResponseDto(
            id = "3",
            clientName = "Alice Johnson",
            phoneNumber = "5551234567",
            address = "789 Oak St, Villagetown",
            workerQuantity = 2,
            serviceType = "Electrical",
            startTime = currentTime + 18000000, // 5 hours from now
            endTime = currentTime + 21600000, // 6 hours from now
            price = 180.000
        ),
        JobResponseDto(
            id = "4",
            clientName = "Bob Brown",
            phoneNumber = "4449876543",
            address = "321 Pine St, Hamlet",
            workerQuantity = 4,
            serviceType = "Painting",
            startTime = currentTime + 25200000, // 7 hours from now
            endTime = currentTime + 86400000, // 1 day from now
            price = 250.000
        ),
        JobResponseDto(
            id = "5",
            clientName = "Charlie Green",
            phoneNumber = "3336547890",
            address = "654 Maple St, Metropolis",
            workerQuantity = 6,
            serviceType = "Landscaping",
            startTime = currentTime + 43200000, // 12 hours from now
            endTime = currentTime + 129600000, // 1.5 days from now
            price = 300.000
        ),
        JobResponseDto(
            id = "6",
            clientName = "Diana White",
            phoneNumber = "2227894561",
            address = "987 Birch St, Capital City",
            workerQuantity = 1,
            serviceType = "Carpentry",
            startTime = currentTime + 72000000, // 20 hours from now
            endTime = currentTime + 172800000, // 2 days from now
            price = 120.000
        ),
        JobResponseDto(
            id = "7",
            clientName = "Eve Black",
            phoneNumber = "1114567892",
            address = "159 Cedar St, Urbania",
            workerQuantity = 3,
            serviceType = "Moving",
            startTime = currentTime + 90000000, // 25 hours from now
            endTime = currentTime + 259200000, // 3 days from now
            price = 220.000
        ),
        // Present endTime
        JobResponseDto(
            id = "8",
            clientName = "Frank Gray",
            phoneNumber = "6661237894",
            address = "753 Spruce St, Suburbia",
            workerQuantity = 2,
            serviceType = "Repair",
            startTime = currentTime - 3600000, // 1 hour ago
            endTime = currentTime, // Now
            price = 140.000
        ),
        // Past endTime
        JobResponseDto(
            id = "9",
            clientName = "Grace Blue",
            phoneNumber = "7779871234",
            address = "852 Willow St, Countryside",
            workerQuantity = 4,
            serviceType = "Installation",
            startTime = currentTime - 36000000, // 10 hours ago
            endTime = currentTime - 32400000, // 9 hours ago
            price = 260.000
        ),
        JobResponseDto(
            id = "10",
            clientName = "Henry Yellow",
            phoneNumber = "8886543219",
            address = "951 Aspen St, Downtown",
            workerQuantity = 5,
            serviceType = "Maintenance",
            startTime = currentTime - 43200000, // 12 hours ago
            endTime = currentTime - 39600000, // 11 hours ago
            price = 280.0
        )
    )
    // Create list of sections to display
    val homeSections = listOf(
        HomeSection.Avatar,
        HomeSection.Search,
        HomeSection.Category,
        HomeSection.JobList(mockJobResponseList)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Spacer(Modifier.height(8.dp))
        }

        // Render each section
        homeSections.forEach { section ->
            item {
                when (section) {
                    is HomeSection.Avatar -> {
                        CustomAvatarRow()
                        Spacer(Modifier.height(24.dp))
                    }

                    is HomeSection.Search -> {
                        SearchRow()
                        Spacer(Modifier.height(24.dp))
                    }

                    is HomeSection.Category -> {
                        CustomCategoryListRow()
                        Spacer(Modifier.height(16.dp))
                    }

                    is HomeSection.JobList -> {
                        JobListSection(section.jobs) {
                            navController.navigate(AppRoutes.CLEANING_DETAIL)
                        }
                        Spacer(Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun JobListSection(jobs: List<JobResponseDto>, onClickItem: (String) -> Unit) {
    val context = LocalContext.current
    Column {
        Text(
            stringResource(R.string.job_list_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500
        )

        Spacer(Modifier.height(16.dp))

        jobs.forEachIndexed { index, job ->
            ServiceItemCard(job) {
                onClickItem(index.toString())
                Toast.makeText(context, "Click job id: $it", Toast.LENGTH_SHORT).show()
            }
            if (index < jobs.size - 1) {
                Spacer(Modifier.height(12.dp))
            }

        }
    }
}

@Composable
fun ServiceItemCard(job: JobResponseDto, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(job.id)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            /* *
            * Date - countdown
            * */
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = TimeUtils.formatDateTimeFull(job.endTime),
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "[${TimeUtils.timeRemainingText(job.endTime)}]",
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = colorResource(R.color.orange)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            /* *
            * Category title and address
            * */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /* *
                * Service type box
                * */
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            colorResource(R.color.orange),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = job.serviceType,
                        fontSize = 12.sp,
                        color = colorResource(R.color.orange),
                    )
                }

                Spacer(Modifier.width(16.dp))

                /* *
                * Address
                * */
                Text(
                    text = job.address,
                    fontSize = 14.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(8.dp))



            Spacer(modifier = Modifier.height(8.dp))

            /* *
            * Price
            * */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom

            ) {
                /* *
                * Customer info
                * */
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    InfoRow(icon = Icons.Outlined.Person, text = job.clientName)
                    InfoRow(icon = Icons.Outlined.Call, text = job.phoneNumber)
                    InfoRow(
                        icon = Icons.Outlined.Group,
                        text = "Số lượng: " + job.workerQuantity.toString()
                    )
                }

                Text(
                    text = "${job.price} VND",
                    color = colorResource(R.color.orange_text ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFFF6F00),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp)
    }
}


@Composable
fun CustomCategoryListRow() {
    val context = LocalContext.current

    val titleList = listOf("Dọn dẹp", "Vận chuyển", "Bảo trì", "Chăm sóc")
    val svgList = listOf(
        R.drawable.cate1,
        R.drawable.cate2,
        R.drawable.cate3,
        R.drawable.cate4
    )

    Text(
        stringResource(R.string.category_title),
        fontSize = 16.sp,
        fontWeight = FontWeight.W500
    )

    Spacer(Modifier.height(16.dp))

    LazyRow(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(titleList.size) { index ->
            CategoryItem(titleList[index], painterResource = svgList[index]) {
                Toast.makeText(context, "click ${titleList[index]}", Toast.LENGTH_SHORT).show()
            }

        }

    }
}

@Composable
fun CategoryItem(title: String = "Cleaning", painterResource: Int = 0, callback: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { callback(title) }
    ) {
//        val context = LocalContext.current
//        // URI cho raw resource: android.resource://<package>/raw/<name>
//        val data = "android.resource://${context.packageName}/${painterResource}"

//        AsyncImage(
//            model = ImageRequest.Builder(context)
//                .data(data)
//                .decoderFactory(SvgDecoder.Factory()) // bật SVG decoder
//                .build(),
//            contentDescription = null,
//            modifier = Modifier
//                .size(64.dp)
//                .clip(CircleShape),
//            contentScale = ContentScale.Crop
//        )
        Image(
            painterResource(painterResource), null, modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            title,
            fontSize = 12.sp,
            maxLines = 1
        )

    }
}

@Composable
fun SearchRow() {
    val context = LocalContext.current

    val lightGray = colorResource(R.color.light_gray)

    var searchInput by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchInput,
        onValueChange = { searchInput = it },
        shape = RoundedCornerShape(16.dp),
        placeholder = {
            Text(
                stringResource(R.string.search_hint),
                fontSize = 14.sp,
                color = lightGray
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    Toast.makeText(context, "search button", Toast.LENGTH_LONG).show()
                }
            ) { Icon(Icons.Default.Search, null) }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = lightGray,
            focusedBorderColor = Color.Black
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun CustomAvatarRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.avt),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = "Do Duc Thien",
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold, fontSize = 20.sp
        )
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = {}
        ) { Icon(Icons.Default.Notifications, null, tint = colorResource(R.color.orange)) }

    }
}

@Preview(showBackground = true)
@Composable
fun PrevHomeScreen() {
    HomeScreen(navController = NavController(LocalContext.current))
}
