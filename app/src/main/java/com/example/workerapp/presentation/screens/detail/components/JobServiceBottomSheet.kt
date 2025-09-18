package com.example.workerapp.ui.detail.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.R
import com.example.workerapp.data.source.model.cleaning.CleaningServiceModel
import com.example.workerapp.utils.button.FilledRoundedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobServiceBottomSheet(
    items: List<CleaningServiceModel>,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    var currentIndex by remember { mutableIntStateOf(0) }
    val labelList = items.map { it.serviceName }

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.job_detail_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
            Spacer(Modifier.height(16.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                itemsIndexed(labelList) { index, item ->
                    val isSelected = currentIndex == index
                    FilledRoundedButton(
                        label = item,
                        isSelected = isSelected,
                        onClick = { currentIndex = index })
                    if (index < labelList.size - 1) {
                        Spacer(Modifier.width(16.dp))
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            LazyColumn(
                Modifier
                    .padding(horizontal = 16.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                userScrollEnabled = false
            ) {
                itemsIndexed(items[currentIndex].duties) { index, item ->
                    JobDetailItem(title = item)
                }
            }
            Spacer(Modifier.height(64.dp))
        }
    }
}

@Composable
fun JobDetailItem(title: String = "Quet nha") {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            null,
            tint = colorResource(R.color.light_orange_icon)
        )
        Spacer(Modifier.width(16.dp))
        Text(title, fontSize = 14.sp)
    }
}
