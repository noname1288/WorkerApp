package com.example.workerapp.view.home

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
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
import com.example.workerapp.R

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        CustomAvatarRow()

        Spacer(Modifier.height(24.dp))

        SearchRow()

        Spacer(Modifier.height(24.dp))

        CustomCategoryListRow()

        Spacer(Modifier.height(24.dp))

        CustomJobListColumn()
    }
}

@Composable
fun CustomJobListColumn() {
    Text(
        stringResource(R.string.job_list_title),
        fontSize = 16.sp,
        fontWeight = FontWeight.W500
    )

    Spacer(Modifier.height(16.dp))

    ServiceItemCard()
}

@Composable
fun ServiceItemCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .border(width = 1.dp,
                            colorResource(R.color.orange),
                            RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Dọn dẹp",
                        fontSize = 12.sp,
                        color = colorResource(R.color.orange),
                    )
                }

                Spacer(Modifier.width(16.dp))

                Text(
                    text = "SN02 ngõ 38, Mộ Lao Hà Đông, Hà Nội",
                    fontSize = 12.sp,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Date row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Ngày: 18/08/2025 - 08:00",
                    fontSize = 14.sp
                )
                Text(
                    text = "[2 ngày nữa]",
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = colorResource(R.color.orange)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Customer info
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoRow(icon = Icons.Default.Person, text = "Phạm Thanh Sơn")
                InfoRow(icon = Icons.Default.Call, text = "0123456789")
                InfoRow(icon = Icons.Default.List, text = "Số lượng: 3")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price bottom-right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "100.000 VND",
                    color = Color(0xFFFF6F00),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
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
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}


@Composable
fun CustomCategoryListRow() {
    var context = LocalContext.current

    val titleList = listOf("Dọn dẹp", "Vận chuyển", "Bảo trì", "Chăm sóc")

    Text(
        stringResource(R.string.category_title),
        fontSize = 16.sp,
        fontWeight = FontWeight.W500
    )

    Spacer(Modifier.height(16.dp))

    LazyRow (Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween){
        items(titleList, key = {it}){titleItem ->
            CategoryItem(titleItem){
                Toast.makeText(context, "click $titleItem", Toast.LENGTH_SHORT ).show()
            }

        }

    }
}

@Composable
fun CategoryItem(title: String = "Cleaning", callback: (String)-> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable{ callback(title) }
    ) {
        Image(
            painter = painterResource(R.drawable.avt),
            null,
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(8.dp))
        Text(
            title,
            fontSize = 14.sp,
            maxLines = 1
        )

    }
}

@Composable
fun SearchRow() {
    var context = LocalContext.current

    var light_gray = colorResource(R.color.light_gray)

    var searchInput by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchInput,
        onValueChange = { searchInput = it },
        shape = RoundedCornerShape(24.dp),
        placeholder = {
            Text(
                stringResource(R.string.search_hint),
                fontSize = 16.sp,
                color = light_gray
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
            unfocusedTextColor = light_gray,
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
                .size(56.dp)
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
fun HomeScreenPreview() {
    HomeScreen()
}