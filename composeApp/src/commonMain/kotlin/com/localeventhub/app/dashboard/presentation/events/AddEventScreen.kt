package com.localeventhub.app.dashboard.presentation.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localeventhub.app.dashboard.presentation.navigation.EventPageFlag
import com.localeventhub.app.expect.isApiLevel35
import com.localeventhub.app.featurebase.common.Colors
import com.localeventhub.app.featurebase.common.Status
import com.localeventhub.app.featurebase.common.UIStatus
import com.localeventhub.app.featurebase.presentation.ui.compose.FullScreenLoadingProgress
import com.localeventhub.app.featurebase.presentation.ui.compose.TextField
import com.localeventhub.app.featurebase.presentation.ui.state.UIState
import dev.jordond.compass.Place
import dev.jordond.compass.autocomplete.Autocomplete
import dev.jordond.compass.autocomplete.mobile
import kotlinx.coroutines.launch
import localeventhub.composeapp.generated.resources.Res
import localeventhub.composeapp.generated.resources.add_photo
import localeventhub.composeapp.generated.resources.email_validation
import localeventhub.composeapp.generated.resources.events
import localeventhub.composeapp.generated.resources.ic_back
import localeventhub.composeapp.generated.resources.loading
import localeventhub.composeapp.generated.resources.name_validation
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(onNavigate: () -> Unit, viewModel: EventViewModel = koinViewModel()) {
    val signInResponse by viewModel.eventListResponse.collectAsState(initial = null)
    var uiState by remember { mutableStateOf<UIState<Boolean>>(UIState.initial()) }
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val platform = remember {
        com.localeventhub.app.getPlatform().name
    }
    val shouldAddInsets = remember {
        isApiLevel35()
    }
    LaunchedEffect(signInResponse?.status) {
        when (signInResponse?.status) {
            Status.SUCCESS -> {
                val signInResponseData = signInResponse?.data
                if (signInResponseData?.accessToken != null && signInResponseData.accessToken.isNotEmpty()) {
                    onNavigate()
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

    Scaffold(
        modifier = if (shouldAddInsets) Modifier.fillMaxSize()
            .consumeWindowInsets(WindowInsets.statusBars) else Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 5.dp),
                title = {
                    Text(
                        stringResource(Res.string.events),
                        color = if (platform.startsWith("Android")) Color.White else Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (platform.startsWith(
                            "Android"
                        )
                    ) Colors.primary else Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { onNavigate() }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_back),
                            contentDescription = "Back",
                            tint = if (platform.startsWith("Android")) Color.White else Color.Black
                        )
                    }
                })
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (uiState.status) {
                UIStatus.INITIAL -> {
                    AddEvent(paddingValues, onAddClick = {})
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEvent(
    paddingValues: PaddingValues,
    onAddClick: () -> Unit,
    viewModel: EventViewModel = koinViewModel(),
) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Music Festival", "Food Festival", "Donors event")
    var selectedItem by remember { mutableStateOf(items[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFFF5F5F5))
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(150.dp).background(color = Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(Res.drawable.add_photo),
                    contentDescription = "",
                    tint = Color.Black
                )
                Text(modifier = Modifier.padding(top = 5.dp), text = "Attach Image")
            }

        }
        // Description
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            hint = "Description",
            isError = { viewModel.descriptionState.value.isError },
            errorString = { stringResource(Res.string.name_validation) },
            textFieldValue = { viewModel.descriptionState.value.textValue },
            onValueChange = viewModel::setDescription
        )
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            // TextField with dropdown icon
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tag") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            // Dropdown menu items
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            selectedItem = item
                            expanded = false
                        }
                    )
                }
            }
        }
        PlacesAutocomplete()
        if(viewModel.pageFlag != EventPageFlag.VIEW.name){
        // Save Button
            Button(
                onClick = { onAddClick() },
                modifier = Modifier
                    .fillMaxWidth().padding(top = 15.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
            ) {
                Text(
                    text = when (viewModel.pageFlag) {
                        EventPageFlag.ADD.name -> "Save"
                        EventPageFlag.UPDATE.name -> "Update"
                        else -> ""
                    },
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesAutocomplete() {
    val scope = rememberCoroutineScope()
    val autocomplete = remember { Autocomplete.mobile()   }
    var autoCompleteExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val places = remember { mutableStateListOf<Place?>() }
    var selectedPlace: Place? by remember { mutableStateOf(null) }

    ExposedDropdownMenuBox(
        expanded = autoCompleteExpanded,
        onExpandedChange = {}
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Location") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    scope.launch {
                        autocomplete.search(searchQuery).getOrNull().let {
                            println(it)
                            places.clear()
                            places.addAll(it?.toList() ?: emptyList())
                        }
                    }
                    autoCompleteExpanded = !autoCompleteExpanded
                })
        )
        ExposedDropdownMenu(
            expanded = autoCompleteExpanded,
            onDismissRequest = {
                autoCompleteExpanded = false
            }
        ) {
            if (places.isNotEmpty()) {
                places.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = {
                            Text(text = selectionOption?.locality ?: selectionOption?.subLocality ?: selectionOption?.country ?: "Unknown Place")
                        },
                        onClick = {
                            selectedPlace = selectionOption
                            autoCompleteExpanded = false
                        }
                    )
                }
            }
        }
    }
}