package com.ivy.legacy.domain.data

import android.icu.util.TimeZone
import timber.log.Timber
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * [IvyTimeZone]
 * Represents a supported time zone.
 *
 * @param id a [ZoneId] ex: "Asia/Kolkata"
 * @param offset timeOffset returned via the corresponding id. ex: "+05:30"
* */
data class IvyTimeZone(
    val id: String,
    val offset: String
) {
    companion object {
        /**
         * [getSupportedTimeZones]
         *
         * uses [ZoneId.getAvailableZoneIds] since [TimeZone.getAvailableIDs] returns short timezone ids which have been deprecated.
         * @return all supported timezones.
        * */
        fun getSupportedTimeZones(): List<IvyTimeZone> = ZoneId.getAvailableZoneIds().mapNotNull { id ->
            id?.toIvyTimeZone()
        }

        fun getDeviceDefault(): IvyTimeZone =
            ZoneId.systemDefault().id?.toIvyTimeZone() ?: IvyTimeZone("UTC", "Z")
    }

}

/**
 * [String.toIvyTimeZone] should be called only on a possible zoneId string.
 *
 * @return corresponding [IvyTimeZone] or null.
 * */
fun String.toIvyTimeZone(): IvyTimeZone? {
    return kotlin.runCatching {
        val zone = ZoneId.of(this)
        val offsetToday = OffsetDateTime.now(zone).offset
        val offset = DateTimeFormatter.ofPattern("XXX").format(offsetToday)
        IvyTimeZone(this, offset)
    }.onFailure {
        Timber.w("Error for zoneId: \"$this\" -> ${it.stackTraceToString()}")
    }.getOrNull()
}


/**
 * [String.toIvyTimeZoneOrDefault] should be called only on a possible zoneId string.
 *
 * @return corresponding [IvyTimeZone] or device default [IvyTimeZone] if an invalid zoneId string is passed.
 * */
fun String?.toIvyTimeZoneOrDefault(): IvyTimeZone = this?.toIvyTimeZone() ?: IvyTimeZone.getDeviceDefault()
