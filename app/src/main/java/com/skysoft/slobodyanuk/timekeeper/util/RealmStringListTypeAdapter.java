package com.skysoft.slobodyanuk.timekeeper.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.skysoft.slobodyanuk.timekeeper.data.RealmString;

import java.io.IOException;

import io.realm.RealmList;
import lombok.NoArgsConstructor;

/**
 * Created by Serhii Slobodyanuk on 22.11.2016.
 */

@NoArgsConstructor
public class RealmStringListTypeAdapter extends TypeAdapter<RealmList<RealmString>> {

    public static final TypeAdapter<RealmList<RealmString>> INSTANCE =
            new RealmStringListTypeAdapter().nullSafe();

    @Override
    public void write(JsonWriter out, RealmList<RealmString> src) throws IOException {
        out.beginArray();
        for (RealmString realmString : src) {
            out.value(realmString.getValue());
        }
        out.endArray();
    }

    @Override
    public RealmList<RealmString> read(JsonReader in) throws IOException {
        RealmList<RealmString> realmStrings = new RealmList<>();
        in.beginArray();
        while (in.hasNext()) {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
            } else {
                RealmString realmString = new RealmString();
                realmString.setValue(in.nextString());
                realmStrings.add(realmString);
            }
        }
        in.endArray();
        return realmStrings;
    }
}