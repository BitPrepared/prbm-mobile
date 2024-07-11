package it.bitprepared.prbm.mobile.activity

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.Prbm
import it.bitprepared.prbm.mobile.model.PrbmEntity
import java.io.ByteArrayOutputStream
import java.io.IOException

class MainViewModel : ViewModel() {

    fun uploadPrbmJSONs(context: Context) {
        TODO("Port me to use Coroutines")
        val remoteInterface = UserData.getInstance().restInterface
        val barProgressDialog = ProgressDialog(context)
        barProgressDialog.setTitle(context.getString(R.string.save_on_disk))
        barProgressDialog.setMessage(context.getString(R.string.saving_all_prbms))
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        barProgressDialog.isIndeterminate = true
        barProgressDialog.setCancelable(false)
        barProgressDialog.setCancelable(false)
        barProgressDialog.show()
        val gson = GsonBuilder()
            .create()

        val disposable = Observable.defer<List<Prbm>> {
            val list = UserData.getInstance().allPrbm
            for (prbm in list) {
                val title = escape(prbm.title)
                val authors = escape(prbm.authors)
                val filename = "$title-$authors.json"
                val json = gson.toJson(prbm)
                try {
                    Log.d(TAG, "Uploading JSON to remote server: $filename")
                    remoteInterface.uploadPrbm(filename, json).execute()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val entityList: MutableList<PrbmEntity> = ArrayList()
                for (unit in prbm.units) {
                    entityList.addAll(unit.farLeft)
                    entityList.addAll(unit.farRight)
                    entityList.addAll(unit.nearLeft)
                    entityList.addAll(unit.nearRight)
                }
                var imagesToUpload = 0
                for (entity in entityList) {
                    if (!entity.pictureName.isEmpty()) {
                        imagesToUpload++
                    }
                }
                Log.d(TAG, "Images to upload for this PRBM: $imagesToUpload")
                var index = 0

                for (entity in entityList) {
                    if (!entity.pictureName.isEmpty()) {
                        index++
                        val picname = entity.pictureName
                        val picencoded = base64Encode(context, entity.pictureURI)
                        try {
                            Log.d(
                                TAG,
                                "Uploading image $index/$imagesToUpload to remote server: $picname"
                            )
                            remoteInterface.uploadImage(picname, picencoded).execute()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            Observable.just<List<Prbm>>(list)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ l: List<Prbm> ->
                barProgressDialog.dismiss()
                Toast.makeText(
                    context,
                    l.size.toString() + " PRBM Sincronizzati",
                    Toast.LENGTH_SHORT
                ).show()
            }, { t: Throwable ->
                barProgressDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(
                    context,
                    "Errore durante la Sincronizzazione!",
                    Toast.LENGTH_SHORT
                ).show()
            })
    }


    private fun escape(value: String): String {
        return value.replace("\\W+".toRegex(), "-")
    }

    private fun base64Encode(context: Context, pictureURI: Uri): String {
        val image: Bitmap
        try {
            image = MediaStore.Images.Media.getBitmap(context.contentResolver, pictureURI)
            val byteArrayOS = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOS)
            return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return ""
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}