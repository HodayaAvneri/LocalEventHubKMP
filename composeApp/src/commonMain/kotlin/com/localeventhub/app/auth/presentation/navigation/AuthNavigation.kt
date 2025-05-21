package com.localeventhub.app.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.localeventhub.app.auth.presentation.login.LoginScreen
import com.localeventhub.app.auth.presentation.signup.SignUpScreen


fun NavGraphBuilder.authNavigation(navHostController: NavHostController, onNavigateToDashboard: () -> Unit) {
    navigation<com.localeventhub.app.AppNavigationGraph.AuthGraph>(startDestination = AuthRoutes.LoginRoute) {
        composable<AuthRoutes.LoginRoute> {
            LoginScreen(onNavigate =  { authPageFlag ->
                if(authPageFlag == AuthPageFlag.LOGIN){
                    onNavigateToDashboard.invoke()
                }else{
                    navHostController.navigate(AuthRoutes.SignUpRoute)
                }
            })
        }
        composable<AuthRoutes.SignUpRoute> {
            SignUpScreen(onNavigate = { authPageFlag ->
                if(authPageFlag == AuthPageFlag.SIGN_UP){
                    onNavigateToDashboard.invoke()
                }else{
                    navHostController.popBackStack()
                }
            })
        }
    }
}
