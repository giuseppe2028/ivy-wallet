import android.icu.util.TimeZone
import android.util.Log
import com.ivy.legacy.utils.timeNowLocal
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class IvyTimeZoneCustom(val id:String){
    companion object{


        val timeZones = TimeZone.getAvailableIDs().mapNotNull { id ->
            try {


                val tokens = id.replace("_", " ").split("/")
                val name = if (tokens.size == 2) {
                    val (country, city) = tokens
                    "$city ($country)"
                } else {
                    id
                }

                IvyTimeZoneCustom(id)
            } catch (e: DateTimeException) {
                // Log the exception if necessary
                Log.w("TimeZonePicker", "Unknown time-zone ID: $id", e)
                null // Skip this time zone ID
            }
        }
        fun getDefault(): LocalDateTime {
            return timeNowLocal();
        }

    }
    fun getOffset(): String {
        Log.i("prova","${this.id}")
        val zone = ZoneId.of(this.id)
        val offsetFormatter = DateTimeFormatter.ofPattern("XXX")
        val offsetToday = OffsetDateTime.now(zone).offset
        val offset = offsetFormatter.format(offsetToday)
        return offset
    }


}