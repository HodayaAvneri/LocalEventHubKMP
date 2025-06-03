package com.localeventhub.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.localeventhub.app.auth.presentation.navigation.authNavigation
import com.localeventhub.app.dashboard.presentation.dashboard.DashboardScreen
import com.localeventhub.app.dashboard.presentation.events.AddEventScreen
import com.localeventhub.app.dashboard.presentation.navigation.DashboardRoutes
import com.localeventhub.app.featurebase.common.PreferenceStorage
import com.localeventhub.app.featurebase.common.PrefsDataStore
import org.koin.compose.koinInject

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val prefsDataStore = koinInject<PrefsDataStore>()
    LaunchedEffect(Unit){
        PreferenceStorage.getData(prefsDataStore,"user_id")?.let { id ->
            if(id.isNotEmpty()){
                /*navController.navigate(com.localeventhub.app.AppNavigationGraph.DashboardGraph){
                    popUpTo(com.localeventhub.app.AppNavigationGraph.AuthGraph){
                        inclusive = true
                    }
                }*/
            }
        }
    }

    NavHost(navController = navController, startDestination = com.localeventhub.app.AppNavigationGraph.AuthGraph,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700))
        }) {

        authNavigation(navController) {
            navController.navigate(AppNavigationGraph.DashboardGraph) { // next screen graph
                popUpTo(AppNavigationGraph.AuthGraph) {
                    inclusive = true
                }
            }
        }

        composable<AppNavigationGraph.DashboardGraph> {
            DashboardScreen(navController){
                navController.navigate(AppNavigationGraph.AuthGraph){
                    popUpTo(AppNavigationGraph.DashboardGraph){
                        inclusive = true
                    }
                }
            }
        }

        composable<DashboardRoutes.AddEvent> {
            AddEventScreen(onNavigate = {
                navController.navigateUp()
            })
        }

//        signUpNavigation(navController) {
//            navController.navigate(AppNavigationGraph.DashboardGraph)
//        }


    }
}
