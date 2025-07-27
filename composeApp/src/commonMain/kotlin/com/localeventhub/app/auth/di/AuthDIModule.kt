package com.localeventhub.app.auth.di


import com.localeventhub.app.auth.data.datasource.AuthFirebaseService
import com.localeventhub.app.auth.data.datasource.AuthDatasource
import com.localeventhub.app.auth.domain.repository.AuthRepository
import com.localeventhub.app.auth.domain.usecase.SignInUseCase
import com.localeventhub.app.auth.domain.usecase.SignUpUseCase
import com.localeventhub.app.auth.presentation.login.LoginViewModel
import com.localeventhub.app.auth.presentation.signup.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authDIModule = module {

    single<AuthDatasource> {
        AuthFirebaseService(get())
    }
    single<AuthRepository> {
        com.localeventhub.app.auth.data.repositoryimpl.AuthRepositoryImpl(get())
    }
    single {
        SignInUseCase(get())
    }
    single {
        SignUpUseCase(get())
    }
    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
}