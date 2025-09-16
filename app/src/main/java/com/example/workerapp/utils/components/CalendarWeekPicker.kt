import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workerapp.utils.components.WheelMonthYearPicker
import com.example.workerapp.utils.components.WheelPickerDefaults
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarWeekPicker(
    modifier: Modifier = Modifier,
    today: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit = {},
) {
    var showSheet by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf(today) }
    var currentWeekStart by remember { mutableStateOf(today.with(DayOfWeek.MONDAY)) }
    var currentMonthYear by remember { mutableStateOf(YearMonth.from(today)) }

    if (showSheet) {
        WheelMonthYearPicker(
            modifier = Modifier.fillMaxWidth(),
            title = "Chọn tháng và năm",
            titleStyle = TextStyle(
                fontSize = 16.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF333333),
                textAlign = TextAlign.Center,
            ),
            doneLabelStyle = TextStyle(
                fontSize = 16.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center,
                color = Color(0xFF007AFF),
            ),
            textColor = Color(0xff007AFF),
            selectorProperties = WheelPickerDefaults.selectorProperties(
                color = Color.LightGray,
            ),
            rowCount = 5,
            size = DpSize(128.dp, 160.dp),
            onDismiss = {
                showSheet = false
            },
            doneLabel = "Xong",
            onDoneClick = {
                currentMonthYear = YearMonth.from(it)
                currentWeekStart = it.with(DayOfWeek.MONDAY)
                showSheet = false
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                currentWeekStart = currentWeekStart.minusWeeks(1)
                currentMonthYear = YearMonth.from(currentWeekStart)
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous week")
            }

            Text(
                text = "Tháng ${currentMonthYear.monthValue}/${currentMonthYear.year}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable {
                    // Giả sử bạn đã viết MonthYearPickerDialog
                    // Trả về (month, year)
                    // -> cập nhật currentWeekStart
                    showSheet = true

                }
            )

            IconButton(onClick = {
                currentWeekStart = currentWeekStart.plusWeeks(1)
                currentMonthYear = YearMonth.from(currentWeekStart)
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next week")
            }
        }

        Spacer(Modifier.height(12.dp))

        // Hiển thị 7 ngày
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val daysOfWeek = DayOfWeek.entries.toTypedArray()
            for (i in 0..6) {
                val date = currentWeekStart.plusDays(i.toLong())
                val isSelected = date == selectedDate
                val dayLabel = when (daysOfWeek[i]) {
                    DayOfWeek.MONDAY -> "T2"
                    DayOfWeek.TUESDAY -> "T3"
                    DayOfWeek.WEDNESDAY -> "T4"
                    DayOfWeek.THURSDAY -> "T5"
                    DayOfWeek.FRIDAY -> "T6"
                    DayOfWeek.SATURDAY -> "T7"
                    DayOfWeek.SUNDAY -> "CN"
                }

                Column(
                    modifier = Modifier
                        .width(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            1.dp,
                            if (isSelected) Color(0xFFFF9800) else Color.LightGray,
                            RoundedCornerShape(8.dp)
                        )
                        .background(
                            if (isSelected) Color(0xFFFF9800) else Color.Transparent
                        )
                        .padding(vertical = 8.dp)
                        .clickable {
                            selectedDate = date
                            onDateSelected(date)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dayLabel,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isSelected) Color.White else Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = date.dayOfMonth.toString().padStart(2, '0'),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isSelected) Color.White else Color.Black
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarWeekPickerDemo() {
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }
    var selectedMonth by remember { mutableStateOf<LocalDate?>(null) }
    val today = remember { LocalDate.now() }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CalendarWeekPicker(today = today, onDateSelected = { selectedDay = it })
        Spacer(Modifier.height(16.dp))
        Text(
            text = if (selectedDay != null) "Selected date: ${selectedDay ?: "-"}" else "Today: ${today}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Selected month: ${selectedMonth ?: "-"}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarWeekPickerPreview() {
    MaterialTheme {
        CalendarWeekPickerDemo()
    }
}