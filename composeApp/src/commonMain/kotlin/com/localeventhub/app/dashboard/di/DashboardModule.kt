package com.localeventhub.app.dashboard.di


import com.localeventhub.app.dashboard.presentation.events.EventViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dashboardDIModule = module {
    viewModel { EventViewModel() }
}