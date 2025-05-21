package com.localeventhub.app

import kotlinx.serialization.Serializable

sealed class AppNavigationGraph {

  @Serializable
  data object AuthGraph: AppNavigationGraph()

  @Serializable
  data object DashboardGraph: AppNavigationGraph()


}