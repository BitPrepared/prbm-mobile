package it.bitprepared.prbm.mobile.activity;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import it.bitprepared.prbm.mobile.model.EntityField;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.entities.EntityBuilding;
import it.bitprepared.prbm.mobile.model.entities.EntityCuriosity;
import it.bitprepared.prbm.mobile.model.entities.EntityFauna;
import it.bitprepared.prbm.mobile.model.entities.EntityFlower;
import it.bitprepared.prbm.mobile.model.entities.EntityInterview;
import it.bitprepared.prbm.mobile.model.entities.EntityMonument;
import it.bitprepared.prbm.mobile.model.entities.EntityNews;
import it.bitprepared.prbm.mobile.model.entities.EntityOther;
import it.bitprepared.prbm.mobile.model.entities.EntityPanorama;
import it.bitprepared.prbm.mobile.model.entities.EntityTree;
import it.bitprepared.prbm.mobile.model.entities.EntityWeather;

import java.lang.reflect.Type;

public class PrbmEntityDecoder implements JsonDeserializer<PrbmEntity> {

    @Override
    public PrbmEntity deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context) throws JsonParseException {
        JsonObject entityObject = json.getAsJsonObject();
        String caption = entityObject.get("caption").getAsString();
        String description = entityObject.get("description").getAsString();
        String timestamp = entityObject.get("timestamp").getAsString();
        String pictureName = entityObject.get("pictureName").getAsString();
        String type = entityObject.get("type").getAsString();

        JsonArray extraFieldsJson = entityObject.getAsJsonArray("extraFields");
        Type extraFieldType = new TypeToken<EntityField[]>() {
        }.getType();
        EntityField[] extraFields = context.deserialize(extraFieldsJson, extraFieldType);

        PrbmEntity entity;
        switch (type) {
            case "Fiore/Erba":
                entity = new EntityFlower(description, caption, timestamp, extraFields);
                entity.setPictureName(pictureName);
                return entity;
            case "Albero/Arbusto":
                entity = new EntityTree(description, caption, timestamp, extraFields);
                entity.setPictureName(pictureName);
                return entity;
            case "Fauna":
                entity = new EntityFauna(description, caption, timestamp, extraFields);
                entity.setPictureName(pictureName);
                return entity;
            case "Edificio":
                entity = new EntityBuilding(description, caption, timestamp, extraFields);
                entity.setPictureName(pictureName);
                return entity;
            case "Ambiente Naturale":
                entity = new EntityPanorama(description, caption, timestamp, extraFields);
                entity.setPictureName(pictureName);
                return entity;
            case "Meteo":
                entity = new EntityWeather(description, caption, timestamp, extraFields);
                entity.setPictureName(pictureName);
                return entity;
            case "Monumento":
                entity = new EntityMonument(description, caption, timestamp, extraFields);
                entity.setPictureName(pictureName);
                return entity;
            case "Intervista":
                entity = new EntityInterview(description, caption, timestamp, extraFields);
                entity.setPictureName(pictureName);
                return entity;
            case "Fatto di Cronaca":
                entity = new EntityNews(description, caption, timestamp, extraFields);
                entity.setPictureName(pictureName);
                return entity;
            case "Curiosità":
                entity = new EntityCuriosity(description, caption, timestamp, extraFields);
                entity.setPictureName(pictureName);
                return entity;
            case "Altro":
                entity = new EntityOther(description, caption, timestamp, extraFields);
                entity.setPictureName(pictureName);
                return entity;
        }
        return null;
    }
}
