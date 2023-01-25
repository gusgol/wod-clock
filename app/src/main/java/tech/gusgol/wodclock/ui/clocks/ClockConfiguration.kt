package tech.gusgol.wodclock.ui.clocks

import android.view.MotionEvent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import tech.gusgol.wodclock.R


@Composable
fun TimeConfiguration(
    range: List<Int> = (1..60).toList(),
    title: String,
    onConfirm: (Int) -> Unit
) {
    val state = rememberPickerState(
        initialNumberOfOptions = range.size,
        initiallySelectedOption = 12
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = title,
                modifier = Modifier.weight(1f)
            )
            val weightsToCenterVertically = 0.2f
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .weight(weightsToCenterVertically)
            )
            Picker(
                state = state,
                contentDescription = stringResource(R.string.title_how_long),
                modifier = Modifier.weight(4f)
            ) {
                MinutePiece(
                    text = range[it].toString(),
                )
            }
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .weight(weightsToCenterVertically)
            )
            Button(
                onClick = {
                    onConfirm(range[state.selectedOption])
                },
                modifier = Modifier.weight(1.2f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription =  stringResource(R.string.action_confirm),
                    modifier = Modifier
                        .size(24.dp)
                        .wrapContentSize(align = Alignment.Center)
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }

}

@Composable
internal fun MinutePiece(
    text: String
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val modifier = Modifier
            .align(Alignment.Center)
        Text(
            text = text,
            maxLines = 1,
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.display3,
            modifier = modifier
        )
    }
}

@Preview(
    device = Devices.WEAR_OS_SMALL_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true
)
@Composable
fun TimeConfigurationPreview() {
    TimeConfiguration(
        title = stringResource(R.string.title_how_long)
    ) {
    }
}
