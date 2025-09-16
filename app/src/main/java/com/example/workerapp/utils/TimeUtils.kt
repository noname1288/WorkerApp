package com.example.workerapp.utils

import com.example.workerapp.utils.components.MonthWithDays
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object TimeUtils {

    fun formatDateTimeFull(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val zone = ZoneId.systemDefault()

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(zone)

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

