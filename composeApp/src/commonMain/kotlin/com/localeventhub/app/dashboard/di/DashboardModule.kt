package com.localeventhub.app.dashboard.di


import com.localeventhub.app.dashboard.data.datasource.DashboardDatasource
import com.localeventhub.app.dashboard.data.datasource.DashboardFirebaseService
import com.localeventhub.app.dashboard.data.repositoryImpl.DashboardRepositoryImpl
import com.localeventhub.app.dashboard.domain.repository.DashboardRepository
import com.localeventhub.app.dashboard.domain.usecase.AddCommentUseCase
import com.localeventhub.app.dashboard.domain.usecase.AddPostUseCase
import com.localeventhub.app.dashboard.domain.usecase.DeleteCommentUseCase
import com.localeventhub.app.dashboard.domain.usecase.DeletePostUseCase
import com.localeventhub.app.dashboard.domain.usecase.GetAllPostUseCase
import com.localeventhub.app.dashboard.domain.usecase.GetCommentsUseCase
import com.localeventhub.app.dashboard.domain.usecase.GetPostUseCase
import com.localeventhub.app.dashboard.domain.usecase.UpdatePostLikeUseCase
import com.localeventhub.app.dashboard.domain.usecase.UpdatePostUseCase
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
        GetAllPostUseCase(get())
    }
    single {
        GetPostUseCase(get())
    }
    single {
        UpdateUserUseCase(get())
    }
    single {
        AddPostUseCase(get())
    }
    single {
        UpdatePostUseCase(get())
    }
    single {
        DeletePostUseCase(get())
    }
    single {
        UpdatePostLikeUseCase(get())
    }
    single {
        AddCommentUseCase(get())
    }
    single {
        DeleteCommentUseCase(get())
    }
    single {
        GetCommentsUseCase(get())
    }
    viewModel { ProfileViewModel(get()) }
    viewModel { EventViewModel(get(),get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}