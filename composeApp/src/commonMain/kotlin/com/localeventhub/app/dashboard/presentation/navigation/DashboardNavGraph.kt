package com.localeventhub.app.dashboard.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.localeventhub.app.AppNavigationGraph
import com.localeventhub.app.dashboard.presentation.events.EventsScreen
import com.localeventhub.app.dashboard.presentation.map.MapScreen
import com.localeventhub.app.dashboard.presentation.profile.ProfileScreen

@Composable
fun DashboardNavGraph(
    bottomBarNavController: NavHostController,
    mainNavController: NavController,
    paddingValues: () -> PaddingValues,
    onSessionCleared: () -> Unit
) {
    NavHost(
        navController = bottomBarNavController,
        startDestination = AppNavigationGraph.DashboardGraph
    ) {
        bottomBarGraph(mainNavController, paddingValues, onSessionCleared)
    }

}

fun NavGraphBuilder.bottomBarGraph(
    navController: NavController,
    paddingValues: () -> PaddingValues,
    onSessionCleared: () -> Unit
) {
    navigation<AppNavigationGraph.DashboardGraph>(startDestination = DashboardRoutes.Events) {
        composable<DashboardRoutes.Events> {
             EventsScreen(paddingValues){ pageFlag ->
                 navController.navigate(DashboardRoutes.AddEvent(pageFlag.name))
             }
        }
        composable<DashboardRoutes.Map> {
            MapScreen(paddingValues)
        }
        composable<DashboardRoutes.Profile> {
            ProfileScreen(paddingValues, onNavigate = {

            })
        }
    }
}