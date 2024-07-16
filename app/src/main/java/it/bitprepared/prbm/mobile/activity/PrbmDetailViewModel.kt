package it.bitprepared.prbm.mobile.activity

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class PrbmDetailViewModel : ViewModel() {

    private val _modelState = MutableStateFlow(PrbmDetailModelState())
    val modelState: StateFlow<PrbmDetailModelState> = _modelState.asStateFlow()

}