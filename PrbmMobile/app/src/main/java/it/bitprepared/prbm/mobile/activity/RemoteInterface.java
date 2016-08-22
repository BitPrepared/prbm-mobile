package it.bitprepared.prbm.mobile.activity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface RemoteInterface {

    @FormUrlEncoded
    @POST("upload.php")
    Observable<String> uploadPrbm(@Field("filename") String filename,
                                  @Field("content") String content);
}
