package com.ivy.legacy.legacy.ui.theme.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ivy.design.l0_system.Green
import com.ivy.design.l0_system.UI
import com.ivy.design.l0_system.style
import com.ivy.legacy.IvyWalletComponentPreview
import com.ivy.legacy.domain.data.IvyTimeZone
import com.ivy.legacy.domain.data.toIvyTimeZoneOrDefault
import com.ivy.legacy.utils.addKeyboardListener
import com.ivy.legacy.utils.clickableNoIndication
import com.ivy.legacy.utils.computationThread
import com.ivy.legacy.utils.densityScope
import com.ivy.legacy.utils.hideKeyboard
import com.ivy.legacy.utils.keyboardOnlyWindowInsets
import com.ivy.legacy.utils.onScreenStart
import com.ivy.legacy.utils.rememberInteractionSource
import com.ivy.ui.R
import com.ivy.wallet.ui.theme.GradientGreen
import com.ivy.wallet.ui.theme.GradientIvy
import com.ivy.wallet.ui.theme.Ivy
import com.ivy.wallet.ui.theme.White
import com.ivy.wallet.ui.theme.components.IvyIcon
import com.ivy.wallet.ui.theme.modal.DURATION_MODAL_ANIM
import com.ivy.wallet.ui.theme.pureBlur

@Deprecated("Old design system. Use `:ivy-design` and Material3")
@Suppress("ParameterNaming")
@Composable
fun TimeZonePicker(
    initialSelectedTimeZone: IvyTimeZone?,
    includeKeyboardShownInsetSpacer: Boolean,
    preselectedTimeZone: IvyTimeZone,
    modifier: Modifier = Modifier,
    lastItemSpacer: Dp = 0.dp,
    onKeyboardShown: (keyboardVisible: Boolean) -> Unit = {},
    onSelectedTimeZoneChanged: (IvyTimeZone) -> Unit
) {
    val rootView = LocalView.current
    var keyboardShown by remember { mutableStateOf(false) }

    onScreenStart {
        rootView.addKeyboardListener {
            keyboardShown = it
            onKeyboardShown(it)
        }
    }

    val keyboardShownInsetDp by animateDpAsState(
        targetValue = densityScope {
            if (keyboardShown) keyboardOnlyWindowInsets().bottom.toDp() else 0.dp
        },
        animationSpec = tween(DURATION_MODAL_ANIM)
    )

    Column(
        modifier = modifier
    ) {
        var preselected by remember {
            mutableStateOf(initialSelectedTimeZone == null)
        }

        var selectedTimeZone by remember {
            mutableStateOf(initialSelectedTimeZone ?: preselectedTimeZone)
        }

        var searchTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

        if (!keyboardShown) {
            SelectedTimeZoneCard(
                timeZone = selectedTimeZone,
                preselected = preselected
            )

            Spacer(Modifier.height(20.dp))
        }

        SearchInput(searchTextFieldValue = searchTextFieldValue) {
            searchTextFieldValue = it
        }

        Spacer(Modifier.height(20.dp))

        TimeZoneList(
            searchQuery = searchTextFieldValue.text,
            selectedTimeZone = selectedTimeZone,
            lastItemSpacer = if (includeKeyboardShownInsetSpacer) {
                keyboardShownInsetDp + lastItemSpacer
            } else {
                lastItemSpacer
            },
        ) {
            preselected = false
            //preselectedTimeZone = com.ivy.legacy.domain.data.IvyTimeZone("id","10")
            selectedTimeZone = it
            onSelectedTimeZoneChanged(it)
        }
    }
}

@Deprecated("Old design system. Use `:ivy-design` and Material3")
@Composable
private fun SearchInput(
    searchTextFieldValue: TextFieldValue,
    onSetSearchTextFieldValue: (TextFieldValue) -> Unit
) {
    val inputFocus = FocusRequester()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(UI.shapes.rFull)
            .border(2.dp, UI.colors.mediumInverse, UI.shapes.rFull)
            .clickable {
                inputFocus.requestFocus()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(16.dp))

        IvyIcon(
            modifier = Modifier.padding(vertical = 8.dp),
            icon = R.drawable.ic_search
        )

        Spacer(Modifier.width(8.dp))

        Box(
            contentAlignment = Alignment.CenterStart
        ) {
            if (searchTextFieldValue.text.isEmpty()) {
                // Hint
                Text(
                    text = stringResource(id = R.string.search_currency),
                    style = UI.typo.c.style(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            val view = LocalView.current
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .focusRequester(inputFocus)
                    .testTag("search_input"),
                value = searchTextFieldValue,
                onValueChange = {
                    onSetSearchTextFieldValue(it.copy(it.text.trim()))
                },
                textStyle = UI.typo.b2.style(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                ),
                singleLine = true,
                cursorBrush = SolidColor(UI.colors.pureInverse),
                keyboardActions = KeyboardActions(
                    onDone = {
                        hideKeyboard(view)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
            )
        }
    }
}

@Composable
private fun SelectedTimeZoneCard(
    timeZone: IvyTimeZone,
    preselected: Boolean,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(UI.shapes.r3)
            .background(
                brush = (if (preselected) GradientGreen else GradientIvy).asHorizontalBrush(),
                shape = UI.shapes.r3
            )
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(24.dp))

        Column {
            Text(
                text = timeZone.id,
                style = UI.typo.b2.style(
                    color = White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = timeZone.offset,
                style = UI.typo.b1.style(
                    color = White,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }

        Spacer(Modifier.weight(1f))

        IvyIcon(
            icon = R.drawable.ic_check,
            tint = White
        )

        Text(
            text = if (preselected) stringResource(R.string.pre_selected) else stringResource(R.string.selected_text),
            style = UI.typo.b2.style(
                color = White,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(Modifier.width(32.dp))
    }
}

@Composable
@Suppress("ParameterNaming")
private fun TimeZoneList(
    searchQuery: String,
    selectedTimeZone: IvyTimeZone,
    lastItemSpacer: Dp,
    onTimeZoneSelected: (IvyTimeZone) -> Unit
) {
    // even though this is not a recommended practice for this project but this use case is too
    // small and fixed to use sealed interface/class
    var isLoading by remember { mutableStateOf(true) }

    var allSupportedTimeZones by remember { mutableStateOf(listOf<IvyTimeZone>()) }
    var timeZonesWithLetterDividers by remember { mutableStateOf(listOf<Any>()) }

    LaunchedEffect(Unit) {
        // loading timeZones on the uiThread was causing massive lags
        computationThread {
            allSupportedTimeZones = IvyTimeZone.getSupportedTimeZones()
            isLoading = false
        }
    }

    LaunchedEffect(searchQuery, allSupportedTimeZones) {
        // filter with searchQuery for available timeZones
        val timeZonesToShow = allSupportedTimeZones.filter {
            searchQuery.isBlank() ||
                    it.id.contains(searchQuery, ignoreCase = true) ||
                    it.offset.contains(searchQuery, ignoreCase = true)
        }.sortedBy { it.id }

        timeZonesWithLetterDividers = getTimeZonesWithLetterDividers(timeZonesToShow)
    }

    val listState = remember(searchQuery, selectedTimeZone) {
        LazyListState(
            firstVisibleItemIndex = 0,
            firstVisibleItemScrollOffset = 0
        )
    }

    AnimatedVisibility(isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1000f)
                .background(pureBlur())
                .clickableNoIndication(rememberInteractionSource()) { /*consume clicks*/ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.loading),
                style = UI.typo.b1.style(
                    fontWeight = FontWeight.ExtraBold,
                    color = Green
                )
            )
        }
    }

    LazyColumn(
        state = listState
    ) {
        itemsIndexed(timeZonesWithLetterDividers) { index, item ->
            when (item) {
                is IvyTimeZone -> {
                    TimeZoneItemCard(
                        timeZone = item,
                        selected = item == selectedTimeZone
                    ) {
                        onTimeZoneSelected(item)
                    }
                }
                is LetterDivider -> {
                    LetterDividerItem(
                        spacerTop = if (index == 0) 12.dp else 32.dp,
                        letterDivider = item
                    )
                }
            }
        }

        if (lastItemSpacer.value > 0) {
            item {
                Spacer(Modifier.height(lastItemSpacer))
            }
        }
    }
}

@Composable
private fun TimeZoneItemCard(
    timeZone: IvyTimeZone,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column {
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clip(UI.shapes.r4)
                .background(
                    color = if (selected) Ivy else UI.colors.medium,
                    shape = UI.shapes.r4
                )
                .clickable {
                    onClick()
                }
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(24.dp))

            Text(
                text = timeZone.id,
                style = UI.typo.b1.style(
                    color = if (selected) White else UI.colors.pureInverse,
                    fontWeight = FontWeight.ExtraBold
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.weight(1.0f).padding(end = 4.dp)
            )
            Text(
                text = timeZone.offset.take(20),
                style = UI.typo.b2.style(
                    color = if (selected) White else UI.colors.pureInverse,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.width(32.dp))
        }
    }
}

@Composable
private fun LetterDividerItem(
    spacerTop: Dp,
    letterDivider: LetterDivider
) {
    Column {
        if (spacerTop > 0.dp) {
            Spacer(Modifier.height(spacerTop))
        }
        Text(
            modifier = Modifier.padding(start = 32.dp),
            text = letterDivider.letter,
            style = UI.typo.c.style(
                color = UI.colors.pureInverse,
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(Modifier.height(6.dp))
    }
}

@Preview
@Composable
private fun Preview() {
    IvyWalletComponentPreview {
        TimeZonePicker(
            initialSelectedTimeZone = null,
            includeKeyboardShownInsetSpacer = true,
            preselectedTimeZone = IvyTimeZone("Europe/Rome","+02:00")
        ) {}
    }
}

@Preview()
@Composable
private fun TimeZoneItemCardPreview() {
    IvyWalletComponentPreview {
        TimeZoneItemCard(
            timeZone = "America/Argentina/Catamarca".toIvyTimeZoneOrDefault(),
            selected = true,
            onClick = {}
        )
    }
}

// should return a combined list of [IvyTimeZone] and [LetterDivider]
private fun getTimeZonesWithLetterDividers(timeZones: List<IvyTimeZone>): List<Any> {
    val timeZonesWithLetters = mutableListOf<Any>()
    var lastFirstLetter: String? = null
    for (timeZone in timeZones) {
        val firstLetter =  timeZone.id.first().toString()
        if (firstLetter != lastFirstLetter) {
            timeZonesWithLetters.add(
                //TODO review the access of letterDevider
                LetterDivider(
                    letter = firstLetter
                )
            )
            lastFirstLetter = firstLetter
        }

        timeZonesWithLetters.add(timeZone)
    }
    return timeZonesWithLetters
}

data class LetterDivider(
    val letter: String
)


