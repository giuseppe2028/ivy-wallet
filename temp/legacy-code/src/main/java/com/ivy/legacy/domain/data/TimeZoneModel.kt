import androidx.compose.runtime.Immutable

@Immutable
data class TimeZoneModel(
    val id:String,
    val name:String,
    val offsetName:String,
    val offset:String
);