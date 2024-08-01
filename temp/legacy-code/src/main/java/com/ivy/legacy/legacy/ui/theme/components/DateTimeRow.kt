package com.ivy.legacy.legacy.ui.theme.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivy.legacy.IvyWalletComponentPreview
import com.ivy.legacy.domain.data.IvyTimeZone
import com.ivy.legacy.ivyWalletCtx
import com.ivy.legacy.utils.formatNicely
import com.ivy.legacy.utils.formatTimeOnly
import com.ivy.legacy.utils.replaceDateOrTimeInInstant
import com.ivy.legacy.utils.toLocalDate
import com.ivy.ui.R
import com.ivy.wallet.ui.theme.components.IvyOutlinedButton
import java.time.Instant

@Composable
fun DateTimeRow(
    dateTime: Instant,
    timeZone: IvyTimeZone,
    onSetDateTime: (Instant) -> Unit,
    modifier: Modifier = Modifier
) {
    val ivyContext = ivyWalletCtx()

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(24.dp))

        IvyOutlinedButton(
            text = dateTime.formatNicely(timeZone = timeZone),
            iconStart = R.drawable.ic_date
        ) {
            ivyContext.datePicker(
                initialDate = dateTime.toLocalDate(timeZone = timeZone),
                onDatePicked = {
                    onSetDateTime(
                        replaceDateOrTimeInInstant(
                            instant = dateTime,
                            dateOrTime = it,
                            timeZone = timeZone
                        )
                    )
                }
            )
        }

        Spacer(Modifier.weight(1f))

        IvyOutlinedButton(
            text = dateTime.formatTimeOnly(timeZone),
            iconStart = R.drawable.ic_date
        ) {
            ivyContext.timePicker(
                tz = timeZone,
                onTimePicked = {
                    onSetDateTime(
                        replaceDateOrTimeInInstant(
                            instant = dateTime,
                            dateOrTime = it,
                            timeZone = timeZone
                        )
                    )
                }
            )
        }

        Spacer(Modifier.width(24.dp))
    }
}

@Preview
@Composable
private fun Preview() {
    IvyWalletComponentPreview {
        DateTimeRow(
            dateTime = Instant.now(),
            timeZone = IvyTimeZone.getDeviceDefault(),
            onSetDateTime = {}
        )
    }
}