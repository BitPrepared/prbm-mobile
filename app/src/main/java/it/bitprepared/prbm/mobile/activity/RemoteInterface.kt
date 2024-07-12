package it.bitprepared.prbm.mobile.activity

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RemoteInterface {
    @FormUrlEncoded
    @POST("upload.php")
    suspend fun uploadPrbm(
        @Field("filename") filename: String, @Field("content") content: String
    ): String

    @FormUrlEncoded
    @POST("image.php")
    suspend fun uploadImage(
        @Field("name") filename: String, @Field("image") content: String
    ): String
}
