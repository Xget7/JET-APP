package xget.dev.jet.core.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<State> : ViewModel() {

    protected val _state = MutableStateFlow(this.defaultState())
    open val state = _state.asStateFlow()


    protected abstract fun defaultState(): State


}