package com.saqib.googlepay.gpay.jwt;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.oauth.jsontoken.JsonToken;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

/**
 * Some utility functions for {@link JsonToken}s.
 */
public class JsonTokenUtil {

    static public final String DELIMITER = ".";

    public static String toBase64(JsonObject json) {
        return convertToBase64(toJson(json));
    }

    public static String toJson(JsonObject json) {
        return new Gson().toJson(json);
    }

    public static String convertToBase64(String source) {
        return new String(Base64.encodeBase64(StringUtils.getBytesUtf8(source)));
    }

    public static String decodeFromBase64String(String encoded) {
        return new String(Base64.decodeBase64(StringUtils.getBytesUtf8(encoded)));
    }

    public static String fromBase64ToJsonString(String source) {
        return StringUtils.newStringUtf8(Base64.decodeBase64(source));
    }

    public static String toDotFormat(String... parts) {
        return Joiner.on('.').useForNull("").join(parts);
    }

    private JsonTokenUtil() { }
}
