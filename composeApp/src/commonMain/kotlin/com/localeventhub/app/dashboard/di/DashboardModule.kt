package com.localeventhub.app.dashboard.di


import com.localeventhub.app.auth.data.datasource.AuthApiService
import com.localeventhub.app.auth.data.datasource.AuthDatasource
import com.localeventhub.app.auth.domain.repository.AuthRepository
import com.localeventhub.app.auth.domain.usecase.AuthUseCase
import com.localeventhub.app.auth.presentation.login.LoginViewModel
import com.localeventhub.app.auth.presentation.signup.SignUpViewModel
import com.localeventhub.app.dashboard.presentation.events.EventViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dashboardDIModule = module {
    viewModel { EventViewModel(get()) }
}