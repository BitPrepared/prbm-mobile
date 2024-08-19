package it.bitprepared.prbm.mobile.activity

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {

    private val _appLoadedState = MutableStateFlow(false)
    val appLoadedState: StateFlow<Boolean> = _appLoadedState.asStateFlow()

    fun load(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
            UserData.restorePrbms(context)
            UserData.getEntityTypes(context)
            _appLoadedState.emit(true)
        }
    }
}
