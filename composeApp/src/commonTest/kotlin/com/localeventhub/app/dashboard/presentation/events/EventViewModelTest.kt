package com.localeventhub.app.dashboard.presentation.events

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.localeventhub.app.dashboard.data.datasource.DashboardDatasource
import com.localeventhub.app.dashboard.data.model.AddCommentResponseDto
import com.localeventhub.app.dashboard.data.model.CommentDto
import com.localeventhub.app.dashboard.data.model.EventLocationDto
import com.localeventhub.app.dashboard.data.model.PostDto
import com.localeventhub.app.dashboard.data.model.UpdateResponseDto
import com.localeventhub.app.dashboard.data.model.UserDto
import com.localeventhub.app.dashboard.data.repositoryImpl.DashboardRepositoryImpl
import com.localeventhub.app.dashboard.domain.entity.EventLocationEntity
import com.localeventhub.app.dashboard.domain.entity.PostEntity
import com.localeventhub.app.dashboard.domain.entity.UpdateResponse
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
import com.localeventhub.app.expect.targetModule
import com.localeventhub.app.featurebase.common.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EventViewModelTest : KoinTest {

    private val eventViewModel: EventViewModel by inject()

    // Koin module
    private val testModule = module {
        single<DashboardDatasource> {
            DummyDatabaseFirebaseService()
        }
        single<DashboardRepository> {
            DashboardRepositoryImpl(get())
        }
        single {
            GetPostUseCase(get())
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

        viewModel {
            EventViewModel(
                SavedStateHandle(mapOf("someKey" to "someValue")),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        // Start Koin
        startKoin {
            modules(testModule)
        }
        // Set the main dispatcher for testing
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        // Stop Koin
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `should return all posts`() = runTest {
        // arrange
        val list = listOf(
            PostEntity(
                "1",
                "abcd",
                "KMP",
                "url",
                EventLocationEntity(1.0, 1.0, "address"),
                timestamp = 1L,
                user = null
            )
        )

        // act
        eventViewModel.getAllPost()

        // assert
        eventViewModel.getEventListResponse.test {
            skipItems(2) // To skip initial null and loading
            val result = awaitItem()
            assertEquals(ApiResult.success(list), result)
        }
    }

    @Test
    fun `should update like if post id is not empty`() = runTest {
        // arrange
        eventViewModel.postId = "1"
        // act
        eventViewModel.updateLike(eventViewModel.postId, listOf("abcd"))
        // assert
        eventViewModel.createEventResponse.test {
            skipItems(2) // To skip initial null and loading
            val result = awaitItem()
            assertEquals(ApiResult.success(UpdateResponse("success")), result)
        }
    }

    class DummyDatabaseFirebaseService : DashboardDatasource {
        override suspend fun updateUser(userDto: UserDto): UpdateResponseDto {
            if(userDto.userId.isNotEmpty() && userDto.name.isNotEmpty())
                return UpdateResponseDto("Success")
            else
                return UpdateResponseDto("Failed")
        }

        override suspend fun addPost(postDto: PostDto): UpdateResponseDto {
            TODO("Not yet implemented")
        }

        override suspend fun getPosts(): List<PostDto> {
            return listOf(
                PostDto(
                    "1",
                    "abcd",
                    "KMP",
                    "url",
                    EventLocationDto(1.0, 1.0, "address"),
                    timestamp = 1L,
                    user = null
                )
            )
        }

        override suspend fun getPost(id: String): PostDto? {
            TODO("Not yet implemented")
        }

        override suspend fun updatePost(postDto: PostDto): UpdateResponseDto {
            TODO("Not yet implemented")
        }

        override suspend fun deletePost(id: String): UpdateResponseDto {
            TODO("Not yet implemented")
        }

        override suspend fun updateLike(
            id: String,
            likedUserList: MutableList<String>
        ): UpdateResponseDto {
            return if(id.isNotEmpty() && likedUserList.isNotEmpty())
                UpdateResponseDto("success")
            else
                UpdateResponseDto("Failure")
        }

        override suspend fun getComments(id: String): List<CommentDto> {
            TODO("Not yet implemented")
        }

        override suspend fun addComment(id: String, comment: CommentDto): AddCommentResponseDto {
            TODO("Not yet implemented")
        }

        override suspend fun deleteComment(id: String, commentId: String): UpdateResponseDto {
            TODO("Not yet implemented")
        }

    }
}

