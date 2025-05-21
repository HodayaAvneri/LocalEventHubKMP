package com.localeventhub.app.auth.presentation.navigation

import kotlinx.serialization.Serializable

sealed class AuthRoutes {

     @Serializable
     data object LoginRoute : AuthRoutes()

     @Serializable
     data object SignUpRoute : AuthRoutes()

}