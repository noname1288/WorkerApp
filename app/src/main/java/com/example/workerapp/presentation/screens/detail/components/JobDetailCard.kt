package com.example.workerapp.ui.detail.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workerapp.R
import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.model.cleaning.CleaningJobModel1
import com.example.workerapp.data.source.model.healthcare.HealthcareJobModel
import com.example.workerapp.utils.components.InformationItem
import com.example.workerapp.utils.ext.toVND
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun JobDetailCard(job: JobModel1) {
    val sizeOfDays = job.listDays.size
    val sortDatesAscending: (List<String>) -> List<String> = { dates ->
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dates.sortedBy { dateFormat.parse(it) }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.white)
        ),
        elevation = CardDefaults.cardElevation(3.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.job_detail_title_2),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            when (job){
                is CleaningJobModel1 -> {
                    InformationItem(
                        "Danh mục",
                        value = "Dọn vệ sinh"
                    )
                    InformationItem(
                        "Thời lượng",
                        value = "[${job.duration.workingHour} giờ]"
                    )
                    InformationItem(
                        "Giờ làm việc",
                        value = "${job.startTime}"
                    )
                    InformationItem(
                        "Ngày bắt đầu",
                        value = sortDatesAscending(job.listDays)[0]
                    )
                    InformationItem(
                        "Ngày kết thúc",
                        value = sortDatesAscending(job.listDays)[sizeOfDays - 1]
                    )
                    InformationItem(
                        "Mô tả",
                        value = job.duration.description
                    )
                    InformationItem(
                        "Thanh toán",
                        value = job.price.toVND(), isImportant = true
                    )

                }
                is HealthcareJobModel -> {
                    InformationItem(
                        "Danh mục",
                        value = "Chăm sóc sức khỏe"
                    )
                    InformationItem(
                        "Thời lượng",
                        value = "[${job.shift.workingHour} giờ]"
                    )
                    InformationItem(
                        "Giờ làm việc",
                        value = "${job.startTime}"
                    )
                    InformationItem(
                        "Ngày bắt đầu",
                        value = sortDatesAscending(job.listDays)[0]
                    )
                    InformationItem(
                        "Ngày kết thúc",
                        value = sortDatesAscending(job.listDays)[sizeOfDays - 1]
                    )
                    InformationItem(
                        "Thanh toán",
                        value = job.price.toVND(), isImportant = true
                    )
                }
                else -> {
                    // Xử lý nếu job không thuộc các loại trên
                    Text("Unknown Job Type")
                }
            }


        }
    }
}
