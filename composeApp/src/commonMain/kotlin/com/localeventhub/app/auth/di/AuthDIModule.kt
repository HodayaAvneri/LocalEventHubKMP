package com.localeventhub.app.auth.di


import com.localeventhub.app.auth.data.datasource.AuthApiService
import com.localeventhub.app.auth.data.datasource.AuthDatasource
import com.localeventhub.app.auth.domain.repository.AuthRepository
import com.localeventhub.app.auth.domain.usecase.AuthUseCase
import com.localeventhub.app.auth.presentation.login.LoginViewModel
import com.localeventhub.app.auth.presentation.signup.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authDIModule = module {
    single {
        AuthApiService(get(),get())
    }
    single {
        AuthDatasource(get())
    }
    single<AuthRepository> {
        com.localeventhub.app.auth.data.repositoryimpl.AuthRespositoryImpl(get())
    }
    single {
        AuthUseCase(get())
    }
    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
}