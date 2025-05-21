package com.localeventhub.app.dashboard.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.localeventhub.app.dashboard.presentation.navigation.DashboardBottomRoutes
import com.localeventhub.app.dashboard.presentation.navigation.DashboardNavGraph
import com.localeventhub.app.expect.isApiLevel35
import com.localeventhub.app.featurebase.common.Colors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DashboardScreen(mainNavController: NavController, onSessionCleared: () -> Unit){

    val navItems = remember {
        listOf(DashboardBottomRoutes.Events,DashboardBottomRoutes.Map,DashboardBottomRoutes.Profile)
    }
    val bottomNavController = rememberNavController()

    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
        ?: DashboardBottomRoutes.Events.route::class.qualifiedName.orEmpty()

    val currentRouteTrimmed by remember(currentRoute) {
        derivedStateOf { currentRoute.substringBefore("?") }
    }
    val platform = remember {
        com.localeventhub.app.getPlatform().name
    }
    val shouldAddInsets = remember {
        isApiLevel35()
    }
    Scaffold(
        modifier = if(shouldAddInsets) Modifier.fillMaxSize().consumeWindowInsets(WindowInsets.statusBars) else Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(56.dp), containerColor = if(platform.startsWith("Android")) Colors.primary else Color.Gray) {
                navItems.forEach { navigationItem ->

                    val isSelected by remember(currentRoute) {
                        derivedStateOf { currentRouteTrimmed == navigationItem.route::class.qualifiedName }
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        icon = {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                painter =  painterResource(navigationItem.icon),
                                contentDescription = stringResource(navigationItem.title),
                                tint = if(isSelected) MaterialTheme.colorScheme.secondary else Color.White
                            )
                        },
                        onClick = {
                            bottomNavController.navigate(navigationItem.route){
                                popUpTo(bottomNavController.graph.startDestinationId){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        DashboardNavGraph(bottomNavController, mainNavController, { paddingValues }){
            onSessionCleared()
        }
    }

}