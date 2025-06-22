package com.localeventhub.app.dashboard.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.localeventhub.app.auth.presentation.navigation.AuthPageFlag
import com.localeventhub.app.expect.isApiLevel35
import com.localeventhub.app.featurebase.common.Colors
import com.localeventhub.app.featurebase.common.PreferenceStorage
import com.localeventhub.app.featurebase.common.PrefsDataStore
import com.localeventhub.app.featurebase.common.Status
import com.localeventhub.app.featurebase.common.UIStatus
import com.localeventhub.app.featurebase.presentation.ui.compose.FullScreenLoadingProgress
import com.localeventhub.app.featurebase.presentation.ui.compose.TextField
import com.localeventhub.app.featurebase.presentation.ui.state.UIState
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.launch
import localeventhub.composeapp.generated.resources.Res
import localeventhub.composeapp.generated.resources.email
import localeventhub.composeapp.generated.resources.email_validation
import localeventhub.composeapp.generated.resources.loading
import localeventhub.composeapp.generated.resources.name
import localeventhub.composeapp.generated.resources.name_validation
import localeventhub.composeapp.generated.resources.profile
import multiplatform.network.cmptoast.showToast
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    paddingValues: () -> PaddingValues,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel<ProfileViewModel>()
) {
    val prefsDataStore = koinInject<PrefsDataStore>()
    val updateResponse by viewModel.updateProfileResponse.collectAsState(initial = null)
    var uiState by remember { mutableStateOf<UIState<Boolean>>(UIState.initial()) }
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val platform = remember {
        com.localeventhub.app.getPlatform().name
    }
    val shouldAddInsets = remember {
        isApiLevel35()
    }
    LaunchedEffect(Unit){
        viewModel.setInitData(PreferenceStorage.getData(prefsDataStore,"user_id").toString(),
            PreferenceStorage.getData(prefsDataStore,"user_image").toString(),
            PreferenceStorage.getData(prefsDataStore,"user_name").toString(),
            PreferenceStorage.getData(prefsDataStore,"user_email").toString())
    }
    LaunchedEffect(updateResponse?.status) {
        when (updateResponse?.status) {
            Status.SUCCESS -> {
                val updateResponseData = updateResponse?.data
                showToast(updateResponseData?.message ?: "")
                uiState = UIState.initial()
                viewModel.clearAuthState()
            }

            Status.ERROR -> {
                uiState = UIState.initial()
                showToast(updateResponse?.message ?: "Error occurred")
                viewModel.clearAuthState()
            }

            Status.LOADING -> {
                uiState = UIState.loading()
            }

            else -> {}

        }
    }


    Scaffold(
        modifier = if(shouldAddInsets) Modifier.fillMaxSize().consumeWindowInsets(WindowInsets.statusBars) else Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 5.dp),
                title = {
                    Text(
                        stringResource(Res.string.profile),
                        color = if (platform.startsWith("Android")) Color.White else Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (platform.startsWith(
                            "Android"
                        )
                    ) Colors.primary else Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(innerPadding),
        ) {
            when (uiState.status) {
                UIStatus.INITIAL -> {
                    Profile(paddingValues, onLogout = onLogout)
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
fun Profile(
    paddingValues: () -> PaddingValues,
    viewModel: ProfileViewModel = koinViewModel(),
    onLogout: () -> Unit
) {
    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image,
    ) { file ->
        file?.let {
            viewModel.imagePath = it.path
            viewModel.isProfileImageUpdated = true
        }
    }
    Column(
        modifier = Modifier
            .padding(paddingValues())
            .fillMaxSize()
            .padding(10.dp),
        ) {

        // Profile Card
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
                Box(
                    modifier = Modifier.size(100.dp)
                        .clickable {
                            launcher.launch()
                        }
                        .border(2.dp, shape = RoundedCornerShape(100.dp), color = Colors.primary),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        modifier = Modifier.size(95.dp).clip(CircleShape),
                        model = viewModel.imagePath,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight
                    )
                }
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
                /*// Email
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
                )*/
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = { viewModel.validateAndUpdate()},
                        modifier = Modifier.padding(end = 10.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                    ) {
                        Text(
                            text = "Update",
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }

                    Button(
                        onClick = {onLogout() },
                        modifier = Modifier.padding(start = 10.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                    ) {
                        Text(
                            text = "Logout",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }

            }
        }

    }

}
