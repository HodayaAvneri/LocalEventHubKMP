package com.localeventhub.app.featurebase.presentation.ui.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import localeventhub.composeapp.generated.resources.Res
import localeventhub.composeapp.generated.resources.ic_back
import localeventhub.composeapp.generated.resources.logo
import com.localeventhub.app.featurebase.common.Colors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldContainer(
    modifier: Modifier = Modifier,
    title: String = "",
    navIcon: DrawableResource = Res.drawable.ic_back,
    color: Color = Color.White,
    navBackIconClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    elevation: Dp = 10.dp,
    snackbarHost: @Composable () -> Unit = {}
) {
    val platform = remember {
        com.localeventhub.app.getPlatform().name
    }
    // scaffold -> screen skeleton with appbar , content and bottom bar
    Scaffold(
        containerColor = Color.White,
        snackbarHost = snackbarHost,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = if (platform == "Android") Colors.primary else Color.White),
                title = {
                    Text(
                        text = title,
                        color = if (platform == "Android") Color.White else Color.Black,
                        fontWeight = FontWeight.Normal
                    )
                },
            navigationIcon = {
                IconButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = { navBackIconClick() }) {
                    Icon(
                        painter = painterResource(resource = navIcon),
                        contentDescription = "Back",
                        tint = if (platform == "Android") Color.White else Color.Black
                    )
                }
            })
        }
    ) {
        content(it)
    }
}

@Composable
fun ScaffoldContainerBottomNav(
    navItems: () -> List<String>,
    navIcons: () -> List<DrawableResource>,
    onItemSelect: (Int) -> Unit,
    selectedPosition: () -> Int,
    content: @Composable (PaddingValues) -> Unit,
) {
    //val navBackStackEntry by navController.currentBackStackEntryAsState()
    // val currentDestination = navBackStackEntry?.destination

    // val bottomBarDestination = navItems.any { it.route == currentDestination?.route }

    Scaffold(
        bottomBar = {
            Divider(modifier = Modifier.height(0.5.dp), color = Color.LightGray)
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp)
                    .clip(
                        RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)
                    ),
                containerColor = Color.White
            ) {
                navItems().forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedPosition() == index,
                        onClick = {
                            onItemSelect(index)
                            // selectedTitle = item.title
                            /*navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }*/
                        },
                        icon = {
                            Icon(painterResource(navIcons()[index]), contentDescription = "")
                        },
                        label = {
                            Text(text = navItems()[index])
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            unselectedIconColor = Color.Black,
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }
            }
        }, floatingActionButton = {
            if(selectedPosition() == 0)
            FloatingActionButton(onClick = {}, containerColor = Colors.primary){
                Icon( painter = painterResource(Res.drawable.logo), "Floating action button.", tint = Color.White)
            }
        }
    ) {
        content(it)
    }
}
