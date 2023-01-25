package tech.gusgol.wodclock.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.wear.compose.material.*
import tech.gusgol.wodclock.data.ClockType

@Composable
fun HomeRoute(
    listState: ScalingLazyListState,
    onItemClick: (ClockType) -> Unit
) {
    HomeScreen(listState = listState, onItemClick = onItemClick)
}

@Composable
fun HomeScreen(
    listState: ScalingLazyListState,
    onItemClick: (ClockType) -> Unit
) {
    val items = ClockType.values()
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        autoCentering = AutoCenteringParams(itemIndex = 0),
        state = listState
    ) {
        items(items) { clockType ->
            ClockTypeItem(clockType = clockType, onItemClick = onItemClick)
        }
    }
}

@Composable
fun ClockTypeItem(
    clockType: ClockType,
    onItemClick: (ClockType) -> Unit
) {
    Chip(
        label = {
            Text(
                text = stringResource(clockType.displayName),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = ChipDefaults.imageBackgroundChipColors(
            backgroundImagePainter = painterResource(
                id = clockType.image
            )
        ),
        onClick = { onItemClick(clockType) },
        modifier = Modifier.fillMaxWidth()
    )
}