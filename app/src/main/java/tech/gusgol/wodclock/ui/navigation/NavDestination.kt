package tech.gusgol.wodclock.ui.navigation

interface NavigationDestination {
    val route: String
}

object Home : NavigationDestination {
    override val route: String = "home"
}

object Amrap : NavigationDestination {
    override val route: String = "amrap"
}