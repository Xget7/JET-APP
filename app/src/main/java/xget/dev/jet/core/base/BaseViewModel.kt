package xget.dev.jet.core.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<State> : ViewModel() {

    protected val _uiState = MutableStateFlow(this.defaultState())
    val uiState = _uiState.asStateFlow()


    protected abstract fun defaultState(): State


}