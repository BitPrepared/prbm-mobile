package it.bitprepared.prbm.mobile.activity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RemoteInterface {

    @FormUrlEncoded
    @POST("upload.php")
    Call<String> uploadPrbm(@Field("filename") String filename,
                            @Field("content") String content);

    @FormUrlEncoded
    @POST("image.php")
    Call<String> uploadImage(@Field("name") String filename,
                                   @Field("image") String content);
}
