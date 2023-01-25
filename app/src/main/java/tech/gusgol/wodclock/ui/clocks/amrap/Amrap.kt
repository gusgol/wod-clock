package tech.gusgol.wodclock.ui.clocks

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import com.google.android.horologist.composables.ExperimentalHorologistComposablesApi
import com.google.android.horologist.composables.ProgressIndicatorSegment
import com.google.android.horologist.composables.SegmentedProgressIndicator
import tech.gusgol.wodclock.R
import tech.gusgol.wodclock.ui.WearApp
import tech.gusgol.wodclock.ui.theme.WODClockTheme


enum class AmrapFlow {
    TIME_CONFIG,
    CLOCK,
    RESULT
}

const val DEFAULT_AMRAP_TIME = 10

@Composable
fun AmrapRoute() {
    AmrapScreen()
}

@Composable
fun AmrapScreen() {
    var step by remember { mutableStateOf(AmrapFlow.TIME_CONFIG) }
    var time by remember { mutableStateOf(DEFAULT_AMRAP_TIME) }

    when (step) {
        AmrapFlow.TIME_CONFIG -> {
            TimeConfiguration(
                title = stringResource(id = R.string.title_how_long),
                onConfirm = { selectedTime ->
                    time = selectedTime
                    step = AmrapFlow.CLOCK
                }
            )
        }
        AmrapFlow.CLOCK -> {
            AmrapClock(time) {
                step = AmrapFlow.RESULT
            }
        }
        AmrapFlow.RESULT -> {
            AmrapFinished()
        }
    }
}

@OptIn(ExperimentalHorologistComposablesApi::class)
@Composable
fun AmrapClock(
    timeMin: Int,
    onFinished: () -> Unit = {},
) {
    val segments = mutableListOf<ProgressIndicatorSegment>()
    repeat(timeMin) {
        segments.add(
            ProgressIndicatorSegment(
                weight = 1f,
                indicatorColor = MaterialTheme.colors.primary,
                trackColor = MaterialTheme.colors.secondary.copy(alpha = 0.1f)
            )
        )
    }

    val totalMs = timeMin * 60 * 1000L
    var remainingMillis by remember { mutableStateOf(-1L) }

    var progress by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    val countDownTimer: CountDownTimer = object : CountDownTimer(
        totalMs,
        1000
    ) {
        override fun onTick(millisUntilFinished: Long) {
            remainingMillis = millisUntilFinished
            progress = 1f - (millisUntilFinished.toFloat() / totalMs.toFloat())
        }

        override fun onFinish() {
            onFinished()
        }
    }

    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = LocalContext.current.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE)
                as Vibrator
    }

    var roundCount by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F)
                    .clickable {
                        if (roundCount > 0) roundCount--
                    }
            ) {}
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F)
                    .clickable {
                        roundCount++
                    }
            ) {}
        }
        SegmentedProgressIndicator(
            trackSegments = segments,
            progress = animatedProgress,
            paddingAngle = 2f,
            strokeWidth = 8.dp,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Rounds: $roundCount",
                style = MaterialTheme.typography.caption1
            )
            Text(
                text = getRemainingTime(remainingMillis) {
                    val vibrationEffect1: VibrationEffect =
                        VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                    vibrator.cancel()
                    vibrator.vibrate(vibrationEffect1)
                },
                style = MaterialTheme.typography.display1
            )
        }
    }

    LaunchedEffect(Unit) {
        countDownTimer.start()
    }
}



@Composable
fun AmrapFinished() {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Workout finished!")
    }
}

private fun getRemainingTime(
    millis: Long,
    onMinuteChange: () -> Unit = {}
): String {
    val minutes: Long = millis / 1000 / 60
    val seconds: Long = millis / 1000 % 60
    if (seconds == 0L) {
        onMinuteChange()
    }
    return "%02d:%02d".format(minutes, seconds)
}

@Preview(
    device = Devices.WEAR_OS_SMALL_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true
)
@Composable
fun AmrapClockPreview() {
    WODClockTheme {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(IntrinsicSize.Min)

            ) {
                Text(
                    text = "Rounds",
                    style = MaterialTheme.typography.caption3
                )
                Spacer(modifier = Modifier.width(8.dp))
                Divider(
                    color = MaterialTheme.colors.onSurfaceVariant.copy(
                        alpha = 0.4F
                    ),
                    modifier = Modifier
                        .height(8.dp)
                        .width(1.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "4",
                    style = MaterialTheme.typography.caption3
                )
            }
            Text(
                text = "09:32",
                style = MaterialTheme.typography.display1
            )
        }
    }
}