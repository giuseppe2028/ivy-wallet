package com.ivy.timezone

import android.icu.util.TimeZone
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivy.attributions.AttributionItem
import com.ivy.base.legacy.Theme
import com.ivy.design.l0_system.style
import com.ivy.design.l1_buildingBlocks.Divider
import com.ivy.design.l1_buildingBlocks.DividerH
import com.ivy.legacy.IvyWalletPreview
import com.ivy.legacy.ivyWalletCtx
import com.ivy.legacy.rootScreen
import com.ivy.legacy.utils.setStatusBarDarkTextCompat
import com.ivy.navigation.ReleasesScreen
import com.ivy.navigation.TimeZoneScreen
import com.ivy.navigation.navigation
import com.ivy.wallet.ui.theme.components.IvyToolbar
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
@OptIn(ExperimentalFoundationApi::class)
@Suppress("LongMethod", "LongParameterList")
@Composable
private fun BoxWithConstraintsScope.UI(
    screen: TimeZoneScreen
) {

    val nav = navigation()

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
        Log.d("TimeZoneScreen", "id: $id, name: $name, offset: $offset")
        TimeZoneState(id,name,offsetToday.toString(),offset)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("My_tag")
    ){
        stickyHeader {
            IvyToolbar(
               onBack = { nav.onBackPressed() },
            ) {
                Spacer(Modifier.weight(1f))

                val rootScreen = rootScreen()
                Text(
                    modifier = Modifier.clickable {
                        nav.navigateTo(ReleasesScreen)
                    },
                    text = "${rootScreen.buildVersionName} (${rootScreen.buildVersionCode})",
                    style = com.ivy.design.l0_system.UI.typo.nC.style(
                        color = com.ivy.design.l0_system.UI.colors.gray,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.width(32.dp))
            }
            // onboarding toolbar include paddingBottom 16.dp
        }
        items(itemsTimeZone){
          item -> SingleTimeZoneElement(item, onItemClick = {})
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
    item: TimeZoneState,
    onItemClick: (TimeZoneState) -> Unit
){
    Column(
    modifier = Modifier.
        padding(bottom = 12.dp)
        .clickable { onItemClick(item) }
    ) {
        Text(item.name)
        Text(item.offset + " GMT")
        DividerH()
    }
}
