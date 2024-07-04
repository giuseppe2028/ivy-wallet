package com.ivy.timezone

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.ivy.data.db.dao.read.SettingsDao
import com.ivy.data.db.dao.write.WriteSettingsDao
import com.ivy.legacy.IvyWalletCtx
import com.ivy.legacy.utils.ioThread
import com.ivy.ui.ComposeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@SuppressLint("StaticFieldLeak")
@HiltViewModel
class TimeZoneViewModel @Inject constructor(
    private val settingsWriter: WriteSettingsDao,
    private val settingsDao: SettingsDao,
): ComposeViewModel<TimeZoneState, TimeZoneEvent>() {

    private val timeZone = mutableStateOf("")


    @Composable
    override fun uiState(): TimeZoneState {
        TODO("Not yet implemented")
    }

    override fun onEvent(event: TimeZoneEvent) {
        when(event){
            is TimeZoneEvent.SetPeriod-> setPeriod(event.newTimeZone);
        }
    }

    private fun setPeriod(newPeriodOffset:String){
        //save the period on the shared database
        timeZone.value = newPeriodOffset

        viewModelScope.launch {
            ioThread {
                settingsWriter.save(
                    settingsDao.findFirst().copy(
                        periodOffset = newPeriodOffset
                    )
                )
            }
        }
    }

}
