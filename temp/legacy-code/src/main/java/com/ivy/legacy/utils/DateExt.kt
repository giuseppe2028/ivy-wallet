package com.ivy.legacy.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ivy.base.legacy.stringRes
import com.ivy.frp.Total
import com.ivy.legacy.domain.data.IvyTimeZone
import com.ivy.legacy.domain.data.IvyTimeZone.Companion.toIvyTimeZoneOrDefault
import com.ivy.ui.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjuster
import java.util.Locale
import java.util.concurrent.TimeUnit

//fun timeNowLocal(zone:ZoneId = ZoneOffset.systemDefault()): LocalDateTime = LocalDateTime.now(zone)

fun Instant.toLocalDateTimeWithZone(timeZone: IvyTimeZone?): LocalDateTime = ZonedDateTime.ofInstant(this,timeZone?.zoneId).toLocalDateTime() // FIXME remove later

fun dateNowLocal(): LocalDate = LocalDate.now()

@Total
fun timeNowUTC(): LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)

@Total
fun timeUTC(): LocalTime = LocalTime.now(ZoneOffset.UTC)

@Total
fun dateNowUTC(): LocalDate = LocalDate.now(ZoneOffset.UTC)

fun startOfDayNowUTC() = dateNowUTC().atStartOfDay()

fun endOfDayNowUTC() = dateNowUTC().atEndOfDay()

fun Long.epochSecondToDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)

fun LocalDateTime.toEpochSeconds() = this.toEpochSecond(ZoneOffset.UTC)

fun LocalDateTime.millis() = this.toInstant(ZoneOffset.UTC).toEpochMilli()

fun LocalDateTime.formatNicely(
    noWeekDay: Boolean = false,
    zone: ZoneId = ZoneOffset.systemDefault()
): String {
    val today = dateNowUTC()
    val isThisYear = today.year == this.year

    val patternNoWeekDay = "dd MMM"

    if (noWeekDay) {
        return if (isThisYear) {
            this.formatLocal(patternNoWeekDay)
        } else {
            this.formatLocal("dd MMM, yyyy")
        }
    }

    return when (this.toLocalDate()) {
        today -> {
            stringRes(R.string.today_date, this.formatLocal(patternNoWeekDay, zone))
        }

        today.minusDays(1) -> {
            stringRes(R.string.yesterday_date, this.formatLocal(patternNoWeekDay, zone))
        }

        today.plusDays(1) -> {
            stringRes(R.string.tomorrow_date, this.formatLocal(patternNoWeekDay, zone))
        }

        else -> {
            if (isThisYear) {
                this.formatLocal("EEE, dd MMM", zone)
            } else {
                this.formatLocal("dd MMM, yyyy", zone)
            }
        }
    }
}

fun LocalDateTime.getISOFormattedDateTime(): String = this.formatLocal("yyyyMMdd-HHmm")

fun LocalDateTime.formatNicelyWithTime(
    noWeekDay: Boolean = true,
    zone: ZoneId = ZoneOffset.systemDefault()
): String {
    val today = dateNowUTC()
    val isThisYear = today.year == this.year

    val patternNoWeekDay = "dd MMM HH:mm"

    if (noWeekDay) {
        return if (isThisYear) {
            this.formatLocal(patternNoWeekDay)
        } else {
            this.formatLocal("dd MMM, yyyy HH:mm")
        }
    }

    return when (this.toLocalDate()) {
        today -> {
            stringRes(R.string.today_date, this.formatLocal(patternNoWeekDay, zone))
        }

        today.minusDays(1) -> {
            stringRes(R.string.yesterday_date, this.formatLocal(patternNoWeekDay, zone))
        }

        today.plusDays(1) -> {
            stringRes(R.string.tomorrow_date, this.formatLocal(patternNoWeekDay, zone))
        }

        else -> {
            if (isThisYear) {
                this.formatLocal("EEE, dd MMM HH:mm", zone)
            } else {
                this.formatLocal("dd MMM, yyyy HH:mm", zone)
            }
        }
    }
}

@Composable
fun LocalDateTime.formatLocalTime(): String {
    val timeFormat = android.text.format.DateFormat.getTimeFormat(LocalContext.current)
    return timeFormat.format(this.millis())
}

fun LocalDate.formatDateOnly(): String = this.formatLocal("MMM. dd", ZoneOffset.systemDefault())

fun LocalDateTime.formatTimeOnly(): String = this.format(DateTimeFormatter.ofPattern("HH:mm"))

fun LocalDate.formatDateOnlyWithYear(): String =
    this.formatLocal("dd MMM, yyyy", ZoneOffset.systemDefault())

fun LocalDate.formatDateWeekDay(): String =
    this.formatLocal("EEE, dd MMM", ZoneOffset.systemDefault())

fun LocalDate.formatDateWeekDayLong(): String =
    this.formatLocal("EEEE, dd MMM", ZoneOffset.systemDefault())

fun LocalDate.formatNicely(
    pattern: String = "EEE, dd MMM",
    patternNoWeekDay: String = "dd MMM",
    zone: ZoneId = ZoneOffset.systemDefault()
): String {
    val closeDay = closeDay()
    return if (closeDay != null) {
        "$closeDay, ${this.formatLocal(patternNoWeekDay, zone)}"
    } else {
        this.formatLocal(
            pattern,
            zone
        )
    }
}

fun LocalDate.closeDay(): String? {
    val today = dateNowUTC()
    return when (this) {
        today -> {
            stringRes(R.string.today)
        }

        today.minusDays(1) -> {
            stringRes(R.string.yesterday)
        }

        today.plusDays(1) -> {
            stringRes(R.string.tomorrow)
        }

        else -> {
            null
        }
    }
}

fun LocalDateTime.formatLocal(
    pattern: String = "dd MMM yyyy, HH:mm",
    zone: ZoneId = ZoneOffset.systemDefault()
): String {
    val localDateTime = this.convertUTCtoLocal(zone)
    return localDateTime.atZone(zone).format(
        DateTimeFormatter
            .ofPattern(pattern)
            .withLocale(Locale.getDefault())
            .withZone(zone) // this is if you want to display the Zone in the pattern
    )
}


fun LocalDateTime.format(
    pattern: String
): String {
    return this.format(
        DateTimeFormatter.ofPattern(pattern)
    )
}

fun LocalDateTime.convertUTCtoLocal(zone: ZoneId = ZoneOffset.systemDefault()): LocalDateTime {
    return this.convertUTCto(zone)
}

fun LocalDateTime.convertUTCto(zone: ZoneId): LocalDateTime {
    return plusSeconds(atZone(zone).offset.totalSeconds.toLong())
}

fun LocalTime.convertLocalToUTC(): LocalTime {
    val offset = timeNowLocal(IvyTimeZone.getDeviceDefault()).offset.totalSeconds.toLong()
    return this.minusSeconds(offset)
}

fun LocalTime.convertUTCToLocal(): LocalTime {
    val offset = timeNowLocal(IvyTimeZone.getDeviceDefault()).offset.totalSeconds.toLong()
    return this.plusSeconds(offset)
}

fun LocalDateTime.convertLocalToUTC(): LocalDateTime {
    val offset = timeNowLocal(IvyTimeZone.getDeviceDefault()).offset.totalSeconds.toLong()
    return this.minusSeconds(offset)
}

fun LocalDateTime.toInstant(ivyTimeZone: IvyTimeZone): Instant =
    ZonedDateTime.ofLocal(this, ivyTimeZone.zoneId, null).toInstant()

// The timepicker returns time in UTC, but the date picker returns date in LocalTimeZone
// hence use this method to get both date & time in UTC
fun getTrueDate(date: LocalDate, time: LocalTime, convert: Boolean = true): LocalDateTime {
    val timeLocal = if (convert) time.convertUTCToLocal() else time

    return timeNowUTC()
        .withYear(date.year)
        .withMonth(date.monthValue)
        .withDayOfMonth(date.dayOfMonth)
        .withHour(timeLocal.hour)
        .withMinute(timeLocal.minute)
        .withSecond(0)
        .withNano(0)
        .convertLocalToUTC()
}

fun LocalDate.formatLocal(
    pattern: String = "dd MMM yyyy",
    zone: ZoneId = ZoneOffset.systemDefault()
): String {
    return this.format(
        DateTimeFormatter
            .ofPattern(pattern)
            .withLocale(Locale.getDefault())
            .withZone(zone) // this is if you want to display the Zone in the pattern
    )
}

fun LocalDateTime.timeLeft(
    from: LocalDateTime = timeNowUTC(),
    daysLabel: String = "d",
    hoursLabel: String = "h",
    minutesLabel: String = "m",
    secondsLabel: String = "s"
): String {
    val timeLeftMs = this.millis() - from.millis()
    if (timeLeftMs <= 0) return stringRes(R.string.expired)

    val days = TimeUnit.MILLISECONDS.toDays(timeLeftMs)
    var timeLeftAfterCalculations = timeLeftMs - TimeUnit.DAYS.toMillis(days)

    val hours = TimeUnit.MILLISECONDS.toHours(timeLeftAfterCalculations)
    timeLeftAfterCalculations -= TimeUnit.HOURS.toMillis(hours)

    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftAfterCalculations)
    timeLeftAfterCalculations -= TimeUnit.MINUTES.toMillis(minutes)

    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftAfterCalculations)

    var result = ""
    if (days > 0) {
        result += "$days$daysLabel "
    }
    if (hours > 0) {
        result += "$hours$hoursLabel "
    }
    if (minutes > 0) {
        result += "$minutes$minutesLabel "
    }
//    if (seconds > 0) {
//        result += "$seconds$secondsLabel "
//    }

    return result.trim()
}

fun startOfMonth(date: LocalDate): LocalDateTime =
    date.withDayOfMonth(1).atStartOfDay().convertLocalToUTC()

fun endOfMonth(date: LocalDate): LocalDateTime =
    date.withDayOfMonth(date.lengthOfMonth()).atEndOfDay().convertLocalToUTC()

fun LocalDate.atEndOfDay(): LocalDateTime =
    this.atTime(23, 59, 59)

/**
 * +1 day so things won't fck up with Long overflow
 */
fun beginningOfIvyTime(): LocalDateTime = LocalDateTime.now().minusYears(10)

fun toIvyFutureTime(): LocalDateTime = timeNowUTC().plusYears(30)

fun LocalDate.withDayOfMonthSafe(targetDayOfMonth: Int): LocalDate {
    val maxDayOfMonth = this.lengthOfMonth()
    return this.withDayOfMonth(
        if (targetDayOfMonth > maxDayOfMonth) maxDayOfMonth else targetDayOfMonth
    )
}

/*
* timezone aware utils
* */
fun timeNowLocal(zone: IvyTimeZone): ZonedDateTime = ZonedDateTime.now(zone.zoneId)

fun ZonedDateTime.formatNicely(
    noWeekDay: Boolean = false
): String = this.toInstant().formatNicely(noWeekDay, timeZone = this.zone.id.toIvyTimeZoneOrDefault())

fun Instant.formatNicely(
    noWeekDay: Boolean = false,
    includeTime: Boolean = false,
    includeDate: Boolean = true,
    timeZone: IvyTimeZone
): String {
    val targetTime = this.atZone(timeZone.zoneId)
    val todayTime = Instant.now().atZone(timeZone.zoneId)
    val todayDate = todayTime.toLocalDate()

    val isThisYear = todayTime.year == targetTime.year
    val patternNoWeekDay = if (includeTime) "HH:mm" else ""
    val patternWithDate = if (includeDate) "dd MMM" else ""
    val patternNoWeekDayWithDate = if (includeDate && includeTime) "dd MMM HH:mm" else patternWithDate
    val patternWithYear = if (includeDate) "dd MMM, yyyy" else ""
    val patternNoWeekDayWithYear = if (includeDate && includeTime) "dd MMM, yyyy HH:mm" else patternWithYear

    if (noWeekDay) {
        return if (isThisYear) {
            targetTime.formatLocal(patternNoWeekDayWithDate)
        } else {
            targetTime.formatLocal(patternNoWeekDayWithYear)
        }
    }

    return when (targetTime.toLocalDate()) {
        todayDate -> {
            stringRes(R.string.today_date, targetTime.formatLocal(patternNoWeekDayWithDate))
        }

        todayDate.minusDays(1) -> {
            stringRes(R.string.yesterday_date, targetTime.formatLocal(patternNoWeekDayWithDate))
        }

        todayDate.plusDays(1) -> {
            stringRes(R.string.tomorrow_date, targetTime.formatLocal(patternNoWeekDayWithDate))
        }

        else -> {
            if (isThisYear) {
                targetTime.formatLocal("EEE, ${patternNoWeekDayWithDate.ifEmpty { patternNoWeekDay }}")
            } else {
                targetTime.formatLocal(patternNoWeekDayWithYear)
            }
        }
    }
}


fun Instant.formatLocal(
    pattern: String = "dd MMM yyyy, HH:mm",
    tz: IvyTimeZone
): String = this.atZone(tz.zoneId).formatLocal(pattern)

fun ZonedDateTime.formatLocal(
    pattern: String = "dd MMM yyyy, HH:mm"
): String {
    return this.format(
        DateTimeFormatter
            .ofPattern(pattern)
            .withLocale(Locale.getDefault())
    )
}

fun Instant.toLocalDate(timeZone: IvyTimeZone): LocalDate = this.atZone(timeZone.zoneId).toLocalDate()

fun Instant.formatTimeOnly(tz: IvyTimeZone): String = this.formatLocal(pattern = "HH:mm", tz = tz)

fun Instant.formatDateOnly(tz: IvyTimeZone): String = this.formatLocal(pattern = "MMM. dd", tz = tz)

fun replaceDateOrTimeInInstant(instant: Instant, dateOrTime: TemporalAdjuster, timeZone: IvyTimeZone): Instant {
    val originalZonedDateTime = instant.atZone(timeZone.zoneId)
    val newZonedDateTime = originalZonedDateTime.with(dateOrTime)
    return newZonedDateTime.toInstant()
}
