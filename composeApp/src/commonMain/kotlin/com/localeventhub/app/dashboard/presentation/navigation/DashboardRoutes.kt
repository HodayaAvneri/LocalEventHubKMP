package com.localeventhub.app.dashboard.presentation.navigation

import com.localeventhub.app.dashboard.domain.entity.PostEntity
import kotlinx.serialization.Serializable
import localeventhub.composeapp.generated.resources.Res
import localeventhub.composeapp.generated.resources.events
import localeventhub.composeapp.generated.resources.ic_event
import localeventhub.composeapp.generated.resources.ic_map
import localeventhub.composeapp.generated.resources.ic_profile
import localeventhub.composeapp.generated.resources.map
import localeventhub.composeapp.generated.resources.profile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed class DashboardRoutes {

    @Serializable
    data object Events: DashboardRoutes()

    @Serializable
    data object Map: DashboardRoutes()

    @Serializable
    data object Profile : DashboardRoutes()

    @Serializable
    data object EventDetail: DashboardRoutes()

    @Serializable
    data class AddEvent(val pageFlag: String,val id: String = ""): DashboardRoutes()


}

sealed class DashboardBottomRoutes(val title: StringResource, val icon: DrawableResource, val route: DashboardRoutes){
    data object Events : DashboardBottomRoutes(Res.string.events, Res.drawable.ic_event,
        DashboardRoutes.Events
    )
    data object Map : DashboardBottomRoutes(Res.string.map, Res.drawable.ic_map,
        DashboardRoutes.Map
    )
    data object Profile : DashboardBottomRoutes(Res.string.profile, Res.drawable.ic_profile,
        DashboardRoutes.Profile
    )
}