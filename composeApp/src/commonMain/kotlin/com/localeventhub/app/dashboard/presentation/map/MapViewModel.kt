package com.localeventhub.app.dashboard.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.localeventhub.app.dashboard.domain.entity.PostEntity
import com.localeventhub.app.dashboard.domain.usecase.GetAllPostUseCase
import com.localeventhub.app.featurebase.common.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel(private val getAllPostUseCase: GetAllPostUseCase): ViewModel() {

    private val _getEventListResponse = MutableStateFlow<ApiResult<List<PostEntity>>?>(null)
    var getEventListResponse = _getEventListResponse.asStateFlow()

    fun getAllPost() {
        viewModelScope.launch {
            getAllPostUseCase.invoke().collect {
                _getEventListResponse.value = it
            }
        }
    }

    fun resetState() {
        _getEventListResponse.value = null
    }

}