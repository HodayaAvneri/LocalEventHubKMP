package com.localeventhub.app.featurebase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LocationPermissionViewModel(private val controller: PermissionsController): ViewModel() {

    private val _permissionEvent = MutableSharedFlow<PermissionState>()
    val permissionEvent = _permissionEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            val permissionState = controller.getPermissionState(Permission.LOCATION)
            _permissionEvent.emit(permissionState)
        }
    }

    fun provideOrRequestLocationPermission(){
        viewModelScope.launch {
            try{
                controller.providePermission(Permission.LOCATION)
                _permissionEvent.emit(PermissionState.Granted)
            }catch (e: DeniedAlwaysException){
                _permissionEvent.emit(PermissionState.DeniedAlways)
            }catch (e: DeniedException){
                _permissionEvent.emit(PermissionState.Denied)
            }catch (e: RequestCanceledException){
                e.printStackTrace()
            }

        }
    }

}