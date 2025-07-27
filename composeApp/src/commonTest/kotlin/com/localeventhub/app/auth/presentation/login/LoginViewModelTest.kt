package com.localeventhub.app.auth.presentation.login

import app.cash.turbine.test
import com.localeventhub.app.auth.data.datasource.AuthDatasource
import com.localeventhub.app.auth.data.model.AuthRequestDto
import com.localeventhub.app.auth.data.model.AuthResponseDto
import com.localeventhub.app.auth.data.model.UserDto
import com.localeventhub.app.auth.domain.entity.AuthResponse
import com.localeventhub.app.auth.domain.repository.AuthRepository
import com.localeventhub.app.auth.domain.usecase.SignInUseCase
import com.localeventhub.app.expect.targetModule
import com.localeventhub.app.featurebase.common.ApiResult
import com.localeventhub.app.featurebase.common.PrefsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.jetbrains.compose.resources.getFontResourceBytes
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import kotlin.math.log
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginViewModelTest : KoinTest {

    private val loginViewModel: LoginViewModel by inject()
    // Koin module
    private val testModule = module {
        single<AuthDatasource> {
            DummyAuthFirebaseService()
        }
        single<AuthRepository> {
            com.localeventhub.app.auth.data.repositoryimpl.AuthRepositoryImpl(get())
        }
        single {
            SignInUseCase(get())
        }
        viewModel { LoginViewModel(get()) }
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should return success when email and password are correct`() = runTest {
        // arrange
        val dummyRequest = AuthRequestDto("test@gmail.com", "123456")
        loginViewModel.setEmail(dummyRequest.email)
        loginViewModel.setPassword(dummyRequest.password)
        // act
        loginViewModel.validateAndSignIn()

        // run current tasks
        runCurrent()

        // assert
        loginViewModel.signInResponse.test {
            skipItems(2)
            val result = awaitItem()
            assertEquals(ApiResult.success(AuthResponse("abc123", "message")), result)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should return failure when email and password are inCorrect`() = runTest {
        // arrange
        val dummyRequest = AuthRequestDto("abc@gmail.com", "message")
        loginViewModel.setEmail(dummyRequest.email)
        loginViewModel.setPassword(dummyRequest.password)
        // act
        loginViewModel.validateAndSignIn()

        // run current tasks
        runCurrent()

        // assert
        loginViewModel.signInResponse.test {
            val result1 = awaitItem()
            assertEquals(null, result1)
            val result2 = awaitItem()
            assertEquals(ApiResult.loading(), result2)
            val result3 = awaitItem()
            assertEquals(ApiResult.success(AuthResponse(uID=null, message="")), result3)
        }
    }

    // fake implementation
    class DummyAuthFirebaseService : AuthDatasource {

        override suspend fun signUp(
            authRequestDto: AuthRequestDto,
            userDto: UserDto
        ): AuthResponseDto {
            TODO("Not yet implemented")
        }

        override suspend fun signIn(authRequestDto: AuthRequestDto): AuthResponseDto {
            return if (authRequestDto.email == "test@gmail.com" && authRequestDto.password == "123456")
                AuthResponseDto("abc123", "message")
            else
                AuthResponseDto(null, "")

        }
    }


}