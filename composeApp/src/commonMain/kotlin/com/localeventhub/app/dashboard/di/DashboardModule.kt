package com.localeventhub.app.dashboard.di


import com.localeventhub.app.auth.presentation.login.LoginViewModel
import com.localeventhub.app.dashboard.data.datasource.DashboardDatasource
import com.localeventhub.app.dashboard.data.datasource.DashboardFirebaseService
import com.localeventhub.app.dashboard.data.repositoryImpl.DashboardRepositoryImpl
import com.localeventhub.app.dashboard.domain.repository.DashboardRepository
import com.localeventhub.app.dashboard.domain.usecase.UpdateUserUseCase
import com.localeventhub.app.dashboard.presentation.events.EventViewModel
import com.localeventhub.app.dashboard.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dashboardDIModule = module {
    single {
        DashboardFirebaseService(get())
    }
    single {
        DashboardDatasource(get())
    }
    single<DashboardRepository> {
        DashboardRepositoryImpl(get())
    }
    single {
        UpdateUserUseCase(get())
    }
    viewModel { ProfileViewModel(get()) }
    viewModel { EventViewModel() }
}