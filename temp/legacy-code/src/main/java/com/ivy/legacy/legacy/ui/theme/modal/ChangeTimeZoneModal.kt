package com.ivy.wallet.ui.theme.modal

import IvyTimeZoneCustom
import TimeZoneModel
import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivy.design.l0_system.UI
import com.ivy.design.l0_system.style
import com.ivy.design.system.colors.IvyColors.Gray
import com.ivy.legacy.IvyWalletPreview
import com.ivy.legacy.R
import com.ivy.legacy.utils.getDefaultTimeZone
import com.ivy.wallet.ui.theme.components.CurrencyPicker
import com.ivy.wallet.ui.theme.components.TimeZonePicker
import java.util.UUID


@Deprecated("Old design system. Use `:ivy-design` and Material3")
@Composable
fun BoxWithConstraintsScope.TimeZoneModal(
    title: String,
    //TODO to implement
    initialTimeZone: String?,
    visible: Boolean,
    dismiss: () -> Unit,
    id: UUID = UUID.randomUUID(),

    onSetTimeZone: (String) -> Unit
) {

    var timeZone by remember(id) {
        mutableStateOf(initialTimeZone ?: IvyTimeZoneCustom.getDefault().toString())
    }

    IvyModal(
        id = id,
        visible = visible,
        dismiss = dismiss,
        PrimaryAction = {
            ModalSave(
                modifier = Modifier.testTag("set_currency_save")
            ) {
                onSetTimeZone(timeZone)
                dismiss()
            }
        },
        includeActionsRowPadding = false,
        scrollState = null
    ) {
        var keyboardVisible by remember {
            mutableStateOf(false)
        }

        if (!keyboardVisible) {
            Spacer(Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ModalTitle(text = title)

                Spacer(Modifier.weight(1f))

                Text(
                    text = stringResource(com.ivy.ui.R.string.time_zone),
                    style = UI.typo.c.style(
                        fontWeight = FontWeight.ExtraBold,
                        color = Gray
                    )
                )

                Spacer(Modifier.width(32.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
        TimeZonePicker(
            modifier = Modifier
                .weight(1f),
            initialSelectedTimeZone = null,
            preselectedTimeZone = IvyTimeZoneCustom(getDefaultTimeZone().toString()),
            onKeyboardShown =  { visible -> keyboardVisible = visible },
            includeKeyboardShownInsetSpacer = false,
            lastItemSpacer = 120.dp,
            onSelectedTimeZoneChanged = {value->timeZone = value.id}
        )

    }
}

@Preview
@Composable
private fun Preview() {
    IvyWalletPreview {
        TimeZoneModal(
            title = "Set TimeZone",
            initialTimeZone = "Africa/Accra",
            visible = true,
            dismiss = {},
            onSetTimeZone = {}
        )
    }
}
