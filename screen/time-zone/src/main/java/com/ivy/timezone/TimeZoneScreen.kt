package com.ivy.timezone

import android.icu.util.TimeZone
import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import com.ivy.base.legacy.Theme
import com.ivy.legacy.IvyWalletPreview
import com.ivy.legacy.ivyWalletCtx
import com.ivy.legacy.utils.setStatusBarDarkTextCompat
import com.ivy.navigation.TimeZoneScreen
import com.ivy.navigation.navigation
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BoxWithConstraintsScope.TimeZoneScreen(screen: TimeZoneScreen) {

    val ivyContext = ivyWalletCtx()
    val nav = navigation()
    //val uiState = viewModel.uiState()

    val view = LocalView.current
    LaunchedEffect(Unit) {
       // viewModel.start(screen)

        nav.onBackPressed[screen] = {
            setStatusBarDarkTextCompat(
                view = view,
                darkText = ivyContext.theme == Theme.LIGHT
            )
            false
        }
    }
    UI(
        screen = screen
    )
}
@Suppress("LongMethod", "LongParameterList")
@Composable
private fun BoxWithConstraintsScope.UI(screen: TimeZoneScreen) {

    val offsetFormatter = DateTimeFormatter.ofPattern("XXX")
    val itemsTimeZone = TimeZone.getAvailableIDs().map {
        id ->
        val zone = ZoneId.of(id);
        val offsetToday = OffsetDateTime.now(zone).offset
        val offset = offsetFormatter.format(offsetToday)    // GMT
        val tokens = id.replace("_", " ").split("/")
        val name = if (tokens.size == 2) {
            val (country, city) = tokens
            "$city ($country)"
        }
        else id
        TimeZoneState(id,name,offsetToday.toString(),offset)
    }

    LazyColumn{
        items(itemsTimeZone){
          item -> SingleTimeZoneElement(item.id,item.name,item.offsetName,item.offset)
        }
    }

}
@Preview
@Composable
private fun BoxWithConstraintsScope.Preview_empty() {
    IvyWalletPreview(Theme.LIGHT) {
       //UI(screen = TimeZoneScreen())
    }
}

@Composable
private fun SingleTimeZoneElement(
    id:String,
    name:String,
    offsetName:String,
    offset:String
){
    Column {
        Text(id)
        Text(name)
        Text(offsetName)
        Text(offset)
    }
}
