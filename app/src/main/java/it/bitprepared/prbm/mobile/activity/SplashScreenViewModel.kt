package it.bitprepared.prbm.mobile.activity

import android.content.Context
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.bitprepared.prbm.mobile.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {

    private val _appLoadedState = MutableStateFlow(false)
    val appLoadedState: StateFlow<Boolean> = _appLoadedState.asStateFlow()

    /* List of Resource ID (bitmaps) to be loaded  */
    private val ids = listOf(
        R.drawable.background_curiosity_list,
        R.drawable.background_fauna_list,
        R.drawable.background_flower_list,
        R.drawable.background_forecast_list,
        R.drawable.background_interview_list,
        R.drawable.background_monument_list,
        R.drawable.background_news_list,
        R.drawable.background_panorama_list,
        R.drawable.background_tree_list,
        R.drawable.background_other_list,
        R.drawable.background_building_list,
    )

    fun load(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            UserData.restorePrbms(context)
            // TODO remove me (probably unnecessary)
            ids.forEach {
                val resource = context.resources.openRawResource(it)
                UserData.backBitmaps[it] = BitmapFactory.decodeStream(resource)
            }
            _appLoadedState.emit(true)
        }
    }
}