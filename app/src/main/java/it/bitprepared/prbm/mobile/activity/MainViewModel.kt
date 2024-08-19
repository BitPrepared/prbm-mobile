package it.bitprepared.prbm.mobile.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.createSource
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.bitprepared.prbm.mobile.activity.UserData.gson
import it.bitprepared.prbm.mobile.activity.UserData.restInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class MainViewModel : ViewModel() {

  private val _modelState = MutableStateFlow(MainViewModelState(false, 0f))
  val modelState: StateFlow<MainViewModelState> = _modelState.asStateFlow()

  private val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
    error.printStackTrace()
    _modelState.tryEmit(MainViewModelState(false, 0f, error.message ?: "Unknown error"))
  }

  fun uploadPrbmJSONs(context: Context) =
    viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
      val totalToUpload = UserData.prbmList.map { prbm ->
        prbm.units.map {
          it.farLeft + it.farRight + it.nearLeft + it.nearRight
        }.flatten().flatMap { entity ->
          entity.pictureUri
        }.filter { it.isNotEmpty() }
      }.flatten().size + UserData.prbmList.size

      var uploadedSoFar = 0

      _modelState.emit(MainViewModelState(true, 0f))

      // TODO Move to a single network request.
      UserData.prbmList.forEach { prbm ->
        val fileName = "${prbm.uuid}.json"
        val json = gson.toJson(prbm)

        Log.d(TAG, "Uploading JSON to remote server: $fileName")
        Log.d(TAG, "Content: $json")
        restInterface.uploadPrbm(fileName, json)
        uploadedSoFar++
        _modelState.emit(MainViewModelState(true, uploadedSoFar.toFloat() / totalToUpload))

        prbm.units.map {
          it.farLeft + it.farRight + it.nearLeft + it.nearRight
        }.flatten().forEach { entity ->
          entity.pictureUri.forEach {
            if (it.isNotEmpty()) {
              val pictureName = entity.pictureFilenames[entity.pictureUri.indexOf(it)]
              val pictureEncoded = base64Encode(context, Uri.parse(it))
              Log.d(TAG, "Uploading image to remote server: $pictureName")
              restInterface.uploadImage(pictureName, pictureEncoded)
              uploadedSoFar++
              _modelState.emit(MainViewModelState(true, uploadedSoFar.toFloat() / totalToUpload))
            }
          }
        }
      }
      _modelState.emit(MainViewModelState(false, 1f))
    }

  private fun base64Encode(context: Context, pictureURI: Uri): String {
    val image: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      ImageDecoder.decodeBitmap(createSource(context.contentResolver, pictureURI))
    } else {
      @Suppress("DEPRECATION") MediaStore.Images.Media.getBitmap(
        context.contentResolver,
        pictureURI
      )
    }
    val byteArrayOS = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOS)
    return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT)
  }

  companion object {
    private const val TAG = "MainActivity"
  }
}
