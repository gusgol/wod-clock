package tech.gusgol.wodclock.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import tech.gusgol.wodclock.data.ClockType
import tech.gusgol.wodclock.ui.clocks.AmrapRoute
import tech.gusgol.wodclock.ui.home.HomeRoute

@Composable
fun WODClockNavGraph(
    listState: ScalingLazyListState,
    navController: NavHostController,
    startDestination: NavigationDestination = Home
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Home.route) {
            HomeRoute(
                listState = listState
            ) { type ->
                when(type) {
                    ClockType.AMRAP -> navController.navigate(Amrap.route)
                    else -> {
                    }
                }
            }
        }
        composable(Amrap.route) {
            AmrapRoute()
        }
    }
}