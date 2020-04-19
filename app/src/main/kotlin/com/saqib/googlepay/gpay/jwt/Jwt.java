package com.saqib.googlepay.gpay.jwt;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.saqib.googlepay.gpay.Config;

import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.crypto.RsaSHA256Signer;

import org.joda.time.Instant;

import java.security.InvalidKeyException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static net.oauth.jsontoken.JsonToken.ALGORITHM_HEADER;
import static net.oauth.jsontoken.JsonToken.KEY_ID_HEADER;

public class Jwt {
    private String audience;
    private String type;
    private String iss;
    private ArrayList<String> origins;
    private Instant iat;
    private JsonObject payload;
    private RsaSHA256Signer signer;
    private Config config;

    public Jwt(Context context) {
        config = Config.getInstance(context);
        audience = config.getAudience();
        type = config.getJwtType();
        iss = config.getServiceAccountEmailAddress();
        origins = config.getOrigins();
        iat = new Instant(Calendar.getInstance().getTimeInMillis() - 5000L);
        payload = new JsonObject();

        try {
            this.signer = new RsaSHA256Signer(this.iss, config.KEY_ID, config.getServiceAccountPrivateKey());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public void addTransitClass(JsonElement resourcePayload) {
        if (this.payload.get("transitClasses") == null) {
            JsonArray transitClasses = new JsonArray();
            this.payload.add("transitClasses", transitClasses);

        }
        JsonArray newPayload = (JsonArray) this.payload.get("transitClasses");
        newPayload.add(resourcePayload);
        this.payload.add("transitClasses", newPayload);

        return;
    }

    public void addTransitObject(JsonElement resourcePayload) {
        if (payload.get("transitObjects") == null) {
            JsonArray transitObjects = new JsonArray();
            payload.add("transitObjects", transitObjects);
        }
        JsonArray newPayload = (JsonArray) payload.get("transitObjects");
        newPayload.add(resourcePayload);
        payload.add("transitObjects", newPayload);
    }

    private JsonToken generateUnsignedJwt() {
        JsonToken token = new JsonToken(this.signer);
        token.setAudience(this.audience);
        token.setParam("typ", this.type);
        token.setIssuedAt(this.iat);

        Gson gson = new Gson();
        token.getPayloadAsJsonObject().add("payload", this.payload);
        token.getPayloadAsJsonObject().add("origins", gson.toJsonTree(this.origins));
        return token;
    }

    public String generateSignedJwt() {
        JsonToken jwtToSign = this.generateUnsignedJwt();
        return getSignedJwt(jwtToSign);
    }

    private Map<String, Object> getHeaders(JsonToken jwtToSign) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put(ALGORITHM_HEADER, jwtToSign.getSignatureAlgorithm().getNameForJson());
        String keyId = jwtToSign.getKeyId();
        if (keyId != null) {
            headers.put(KEY_ID_HEADER, keyId);
        }
        return headers;
    }

    public String getSignedJwt(JsonToken jwtToSign) {
        Key key = config.getServiceAccountPrivateKey();
        return Jwts.builder()
                .setPayload(jwtToSign.getPayloadAsJsonObject().toString())
                .setHeader(getHeaders(jwtToSign))
                .signWith(key, SignatureAlgorithm.RS256)
                .compact();
    }
}
