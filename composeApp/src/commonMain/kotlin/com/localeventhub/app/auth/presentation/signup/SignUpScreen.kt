package com.localeventhub.app.auth.presentation.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.localeventhub.app.auth.presentation.navigation.AuthPageFlag
import kotlinx.coroutines.launch
import com.localeventhub.app.featurebase.common.Colors
import com.localeventhub.app.featurebase.common.Status
import com.localeventhub.app.featurebase.common.UIStatus
import com.localeventhub.app.featurebase.presentation.ui.compose.FullScreenLoadingProgress
import com.localeventhub.app.featurebase.presentation.ui.compose.TextField
import com.localeventhub.app.featurebase.presentation.ui.compose.TextNormal
import com.localeventhub.app.featurebase.presentation.ui.compose.TextTitleMedium
import com.localeventhub.app.featurebase.presentation.ui.state.UIState
import io.github.vinceglb.filekit.dialogs.FileKitType
import localeventhub.composeapp.generated.resources.Res
import localeventhub.composeapp.generated.resources.add_photo
import localeventhub.composeapp.generated.resources.confirm_password
import localeventhub.composeapp.generated.resources.confirm_password_validation
import localeventhub.composeapp.generated.resources.email
import localeventhub.composeapp.generated.resources.email_validation
import localeventhub.composeapp.generated.resources.have_account
import localeventhub.composeapp.generated.resources.loading
import localeventhub.composeapp.generated.resources.login
import localeventhub.composeapp.generated.resources.name
import localeventhub.composeapp.generated.resources.name_validation
import localeventhub.composeapp.generated.resources.password
import localeventhub.composeapp.generated.resources.password_validation
import localeventhub.composeapp.generated.resources.sign_up
import multiplatform.network.cmptoast.showToast
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.path
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(
    onNavigate: (AuthPageFlag) -> Unit,
    viewModel: SignUpViewModel = koinViewModel<SignUpViewModel>()
) {
    val signInResponse by viewModel.signUpResponse.collectAsState(initial = null)
    var uiState by remember { mutableStateOf<UIState<Boolean>>(UIState.initial()) }
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(signInResponse?.status) {
        when (signInResponse?.status) {
            Status.SUCCESS -> {
                val signInResponseData = signInResponse?.data
                if (signInResponseData?.uID != null && signInResponseData.uID.isNotEmpty()) {
                    onNavigate(AuthPageFlag.SIGN_UP)
                } else {
                    uiState = UIState.initial()
                    showToast(signInResponseData?.message ?: "Error occurred. Try again!!")
                    viewModel.clearAuthState()
                }
            }

            Status.ERROR -> {
                uiState = UIState.initial()
                showToast(signInResponse?.message ?: "Error occurred")
                viewModel.clearAuthState()
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
                    SignUp(onLoginClick = {
                        onNavigate(AuthPageFlag.LOGIN)
                    }, {
                        if(viewModel.imagePath.isEmpty())
                            showToast("Profile Image is required")
                        else
                            viewModel.validateAndSignUp()
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

                else -> {

                }

            }

        }
    }

}

@Composable
fun SignUp(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: SignUpViewModel = koinViewModel(),
) {
    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image,
    ) { file ->
        file?.let {
            viewModel.imagePath = it.path
        }
    }

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

            Box(
                modifier = Modifier.size(100.dp)
                    .clickable {
                        launcher.launch()
                    }
                    .border(2.dp, shape = RoundedCornerShape(100.dp), color = Colors.primary),
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.imagePath.isNotEmpty())
                    AsyncImage(
                        modifier = Modifier.size(95.dp).clip(CircleShape),
                        model = viewModel.imagePath,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight
                    )
                else
                    Image(
                        painter = painterResource(Res.drawable.add_photo),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SignUp Card
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
                        text = stringResource(Res.string.sign_up),
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    // Name
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        hint = stringResource(Res.string.name),
                        isError = { viewModel.nameState.value.isError },
                        errorString = { stringResource(Res.string.name_validation) },
                        textFieldValue = { viewModel.nameState.value.textValue },
                        onValueChange = viewModel::setName
                    )
                    // Email
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        hint = stringResource(Res.string.email),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = { viewModel.emailState.value.isError },
                        errorString = { stringResource(Res.string.email_validation) },
                        textFieldValue = { viewModel.emailState.value.textValue },
                        onValueChange = viewModel::setEmail
                    )
                    // Password Field
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        hint = stringResource(Res.string.password),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = { viewModel.passwordState.value.isError },
                        errorString = { stringResource(Res.string.password_validation) },
                        textFieldValue = { viewModel.passwordState.value.textValue },
                        onValueChange = viewModel::setPassword,
                    )
                    // Password Field
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        hint = stringResource(Res.string.confirm_password),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = { viewModel.confirmPasswordState.value.isError },
                        errorString = { stringResource(Res.string.confirm_password_validation) },
                        textFieldValue = { viewModel.confirmPasswordState.value.textValue },
                        onValueChange = viewModel::setConfirmPassword,
                    )
                    // SignUp Button
                    Button(
                        onClick = { onSignUpClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                    ) {
                        Text(
                            text = stringResource(Res.string.sign_up),
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
                    text = stringResource(Res.string.have_account),
                    color = Color.Black,
                )
                TextNormal(
                    modifier = Modifier.clickable {
                        onLoginClick()
                    }.padding(start = 5.dp),
                    text = stringResource(Res.string.login),
                    color = Colors.primary,
                )
            }

        }
    }
}