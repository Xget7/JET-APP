package xget.dev.jet.presentation.main.home

import xget.dev.jet.core.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(

): BaseViewModel<HomeUiState>() {



    override fun defaultState(): HomeUiState {
        return HomeUiState()
    }
}