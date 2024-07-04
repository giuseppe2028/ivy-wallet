package com.ivy.timezone

import com.ivy.legacy.data.model.TimePeriod
import com.ivy.navigation.TransactionsScreen

sealed interface TimeZoneEvent {
    data class SetPeriod(
        val newTimeZone:String
    ) : TimeZoneEvent
}