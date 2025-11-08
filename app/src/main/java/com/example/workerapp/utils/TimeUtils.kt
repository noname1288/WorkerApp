package com.example.workerapp.utils

import com.example.workerapp.utils.components.MonthWithDays
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeUtils {

    fun formatDateTimeFull(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val zone = ZoneId.systemDefault()

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(zone)

        return formatter.format(instant)
    }

    fun timeRemainingText(timestamp: Long): String {
        val now = Instant.now()
        val event = Instant.ofEpochMilli(timestamp)

        val duration = Duration.between(now, event)

        // Nếu sự kiện đã qua
        if (duration.isNegative) return "Đã kết thúc"

        val days = duration.toDays()
        val hours = duration.toHours()
        val minutes = duration.toMinutes()

        return when {
            days > 1 -> "Còn $days ngày"
            days == 1L -> "Còn 1 ngày"
            hours >= 1 -> "Còn $hours giờ"
            minutes >= 1 -> "Còn $minutes phút"
            else -> "Ngay bây giờ"
        }
    }

    fun formatMessageTime(timestamp: Long): String {
        val now = Calendar.getInstance()
        val messageTime = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }

        val diffInMillis = now.timeInMillis - messageTime.timeInMillis
        val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)

        val isSameDay = now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == messageTime.get(Calendar.DAY_OF_YEAR)

        return when {
            // 🔹 Nếu trong ngày → hiển thị giờ:phút (vd: 14:35)
            isSameDay -> {
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(messageTime.time)
            }

            // 🔹 Nếu trong tuần (7 ngày gần nhất) → hiển thị Thứ (T2, T3,..., CN)
            diffInDays in 1..6 -> {
                when (messageTime.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> "T2"
                    Calendar.TUESDAY -> "T3"
                    Calendar.WEDNESDAY -> "T4"
                    Calendar.THURSDAY -> "T5"
                    Calendar.FRIDAY -> "T6"
                    Calendar.SATURDAY -> "T7"
                    else -> "CN"
                }
            }

            // 🔹 Nếu quá 1 tuần → hiển thị ngày/tháng (vd: 03/11)
            else -> {
                SimpleDateFormat("dd/MM", Locale.getDefault()).format(messageTime.time)
            }
        }
    }

    fun calculateDuration(startTime: Long, endTime: Long): String {
        val durationMillis = endTime - startTime
        val hours = TimeUnit.MILLISECONDS.toHours(durationMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60
        return "%02d:%02d".format(hours, minutes)
    }

    fun groupDates(dates: List<String>): List<MonthWithDays> {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val parsedDates = dates.map { LocalDate.parse(it, formatter) }

        return parsedDates
            .groupBy { YearMonth.from(it) }
            .map { (month, days) ->
                MonthWithDays(
                    month = month,
                    highlighted = days.map { d -> d.dayOfMonth }.toSet()
                )
            }
            .sortedBy { it.month }
    }

    fun getShiftLabel(time: LocalTime): String {
        return when {
            time.isBefore(LocalTime.NOON) -> TimeShift.MORNING          // < 12:00
            time.isBefore(LocalTime.of(18, 0)) -> TimeShift.AFTERNOON    // 12:00 - 18:00
            else -> TimeShift.EVENING                                    // >= 18:00
        }
    }

    fun toStringWithFormatter(date: LocalDate, pattern: String = "dd/MM/yyyy") : String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return date.format(formatter)
    }
}

object TimeShift{
    const val MORNING = "Ca sáng"
    const val AFTERNOON = "Ca chiều"
    const val EVENING = "Ca tối"
}

