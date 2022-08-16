package com.evanadwyer.simplevisitortracker.navigation

interface Destination {
    val route: String
}

object Home : Destination {
    override val route: String
        get() = "home"
}

object Permissions : Destination {
    override val route: String
        get() = "permissions"
}

object Scanner : Destination {
    override val route: String
        get() = "scanner"
}

object TimeStampForm : Destination {
    override val route: String
        get() = "timestamp"
}