package xget.dev.jet.presentation.main.home

import dagger.hilt.android.lifecycle.HiltViewModel
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.domain.repository.devices.DevicesRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val devicesRepository: DevicesRepository
): BaseViewModel<HomeUiState>() {



    override fun defaultState(): HomeUiState {
        return HomeUiState()
    }
}