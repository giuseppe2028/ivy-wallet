import android.icu.util.TimeZone
import android.util.Log
import java.time.DateTimeException
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class IvyTimeZoneCustom(val id:String, val offset:String){
    companion object{
        val offsetFormatter = DateTimeFormatter.ofPattern("XXX")

        val timeZones = TimeZone.getAvailableIDs().mapNotNull { id ->
            try {
                val zone = ZoneId.of(id)
                val offsetToday = OffsetDateTime.now(zone).offset
                val offset = offsetFormatter.format(offsetToday)
                val tokens = id.replace("_", " ").split("/")
                val name = if (tokens.size == 2) {
                    val (country, city) = tokens
                    "$city ($country)"
                } else {
                    id
                }

                TimeZoneModel(id, name, offsetToday.toString(), offset)
            } catch (e: DateTimeException) {
                // Log the exception if necessary
                Log.w("TimeZonePicker", "Unknown time-zone ID: $id", e)
                null // Skip this time zone ID
            }
        }
    }
}