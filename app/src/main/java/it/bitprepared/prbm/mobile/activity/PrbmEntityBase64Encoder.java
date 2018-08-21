package it.bitprepared.prbm.mobile.activity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import it.bitprepared.prbm.mobile.model.PrbmEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class PrbmEntityBase64Encoder implements JsonSerializer<PrbmEntity> {

    Context context;

    public PrbmEntityBase64Encoder(Context context) {
        this.context = context;
    }

    @Override
    public JsonElement serialize(PrbmEntity src, Type typeOfSrc, JsonSerializationContext context) {

        //Type entityType = new TypeToken<PrbmEntity>(){}.getType();
        JsonObject serialized = context.serialize(src, typeOfSrc).getAsJsonObject();
        if (!src.getPictureName().isEmpty()){
            //String base64 = base64Encode(src.getPictureURI());
            //serialized.addProperty("imageEncoded", base64);
            serialized.addProperty("imageEncoded", "AAAAAAAAAAAA");
        }
        return serialized;
    }

    private String base64Encode(Uri pictureURI) {
        Bitmap image = null;
        try {
            image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), pictureURI);
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
            String encoded = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
            Log.e("TAG", "Encoded: " + encoded);
            return encoded;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
