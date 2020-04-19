package com.saqib.googlepay.gpay.jwt

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.saqib.googlepay.gpay.GooglePayConfig
import com.saqib.googlepay.gpay.GooglePayConfig.getInstance
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import net.oauth.jsontoken.JsonToken
import net.oauth.jsontoken.crypto.RsaSHA256Signer
import org.joda.time.Instant
import java.security.InvalidKeyException
import java.security.Key
import java.util.*

class Jwt(val context: Context) {
    private val audience: String
    private val type: String
    private val iss: String
    private val origins: ArrayList<String>
    private val iat: Instant
    private val payload: JsonObject
    private var signer: RsaSHA256Signer? = null
    private val config: GooglePayConfig = getInstance(context)

    fun addTransitClass(resourcePayload: JsonElement?) {
        if (payload["transitClasses"] == null) {
            val transitClasses = JsonArray()
            payload.add("transitClasses", transitClasses)
        }
        val newPayload = payload["transitClasses"] as JsonArray
        newPayload.add(resourcePayload)
        payload.add("transitClasses", newPayload)
        return
    }

    fun addTransitObject(resourcePayload: JsonElement?) {
        if (payload["transitObjects"] == null) {
            val transitObjects = JsonArray()
            payload.add("transitObjects", transitObjects)
        }
        val newPayload = payload["transitObjects"] as JsonArray
        newPayload.add(resourcePayload)
        payload.add("transitObjects", newPayload)
    }

    private fun generateUnsignedJwt(): JsonToken {
        val token = JsonToken(signer)
        token.audience = audience
        token.setParam("typ", type)
        token.issuedAt = iat
        val gson = Gson()
        token.payloadAsJsonObject.add("payload", payload)
        token.payloadAsJsonObject.add("origins", gson.toJsonTree(origins))
        return token
    }

    fun generateSignedJwt(): String {
        val jwtToSign = generateUnsignedJwt()
        return getSignedJwt(jwtToSign)
    }

    private fun getHeaders(jwtToSign: JsonToken): Map<String, Any> {
        val headers: MutableMap<String, Any> = HashMap()
        headers["typ"] = "JWT"
        headers[JsonToken.ALGORITHM_HEADER] = jwtToSign.signatureAlgorithm.nameForJson
        val keyId = jwtToSign.keyId
        if (keyId != null) {
            headers[JsonToken.KEY_ID_HEADER] = keyId
        }
        return headers
    }

    fun getSignedJwt(jwtToSign: JsonToken): String {
        val key: Key = config.serviceAccountKey
        return Jwts.builder()
            .setPayload(jwtToSign.payloadAsJsonObject.toString())
            .setHeader(getHeaders(jwtToSign))
            .signWith(key, SignatureAlgorithm.RS256)
            .compact()
    }

    init {
        audience = config.AUDIENCE
        type = config.JWT_TYPE
        iss = config.SERVICE_ACCOUNT_EMAIL_ADDRESS
        origins = config.origins
        iat = Instant(Calendar.getInstance().timeInMillis - 5000L)
        payload = JsonObject()
        try {
            signer = RsaSHA256Signer(iss, config.KEY_ID, config.serviceAccountKey)
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }
    }
}
