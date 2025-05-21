package com.localeventhub.app.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localeventhub.app.auth.presentation.navigation.AuthPageFlag
import com.localeventhub.app.auth.presentation.signup.SignUpViewModel
import kotlinx.coroutines.launch
import com.localeventhub.app.featurebase.common.Colors
import com.localeventhub.app.featurebase.common.Status
import com.localeventhub.app.featurebase.common.UIStatus
import com.localeventhub.app.featurebase.presentation.ui.compose.FullScreenLoadingProgress
import com.localeventhub.app.featurebase.presentation.ui.compose.TextField
import com.localeventhub.app.featurebase.presentation.ui.compose.TextNormal
import com.localeventhub.app.featurebase.presentation.ui.compose.TextTitleMedium
import com.localeventhub.app.featurebase.presentation.ui.state.UIState
import localeventhub.composeapp.generated.resources.Res
import localeventhub.composeapp.generated.resources.email
import localeventhub.composeapp.generated.resources.email_validation
import localeventhub.composeapp.generated.resources.loading
import localeventhub.composeapp.generated.resources.login
import localeventhub.composeapp.generated.resources.logo
import localeventhub.composeapp.generated.resources.no_account
import localeventhub.composeapp.generated.resources.password
import localeventhub.composeapp.generated.resources.password_validation
import localeventhub.composeapp.generated.resources.sign_up
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onNavigate: (AuthPageFlag) -> Unit,
    viewModel: LoginViewModel = koinViewModel<LoginViewModel>()
) {

    val signInResponse by viewModel.signInResponse.collectAsState(initial = null)
    var uiState by remember { mutableStateOf<UIState<Boolean>>(UIState.initial()) }
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(signInResponse?.status) {
        when (signInResponse?.status) {
            Status.SUCCESS -> {
                val signInResponseData = signInResponse?.data
                if (signInResponseData?.accessToken != null && signInResponseData.accessToken.isNotEmpty()) {
                    onNavigate(AuthPageFlag.LOGIN)
                } else {
                    uiState = UIState.error("Error")
                }
            }

            Status.ERROR -> {
                uiState = UIState.error(signInResponse?.message.toString())
            }

            Status.LOADING -> {
                uiState = UIState.loading()
            }

            else -> {}

        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (uiState.status) {
                UIStatus.INITIAL -> {
                    Login(onLoginClick = {
                       onNavigate(AuthPageFlag.LOGIN)
                    }, {
                        onNavigate(AuthPageFlag.SIGN_UP)
                    })
                }

                UIStatus.LOADING -> {
                    FullScreenLoadingProgress(message = Res.string.loading)
                }

                UIStatus.ERROR -> {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(message = uiState.message ?: "")
                    }
                }
                else -> {}
            }

        }
    }

}

@Composable
fun Login(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    loginViewModel: LoginViewModel = koinViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(Res.drawable.logo),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Login Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextTitleMedium(
                        text = stringResource(Res.string.login),
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    // Email
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        hint = stringResource(Res.string.email),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = { loginViewModel.emailState.value.isError },
                        errorString = { stringResource(Res.string.email_validation) },
                        textFieldValue = { loginViewModel.emailState.value.textValue },
                        onValueChange = loginViewModel::setEmail
                    )
                    // Password Field
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        hint = stringResource(Res.string.password),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = { loginViewModel.passwordState.value.isError },
                        errorString = { stringResource(Res.string.password_validation) },
                        textFieldValue = { loginViewModel.passwordState.value.textValue },
                        onValueChange = loginViewModel::setPassword,
                    )
                    // Login Button
                    Button(
                        onClick = { onLoginClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                    ) {
                        Text(
                            text = stringResource(Res.string.login),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.Center) {
                // Sign Up Link
                TextNormal(
                    text = stringResource(Res.string.no_account),
                    color = Color.Black,
                )
                TextNormal(
                    modifier = Modifier.clickable {
                        onSignUpClick()
                    }.padding(start = 5.dp),
                    text = stringResource(Res.string.sign_up),
                    color = Colors.primary,
                )
            }

        }
    }
}