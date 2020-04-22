package com.saqib.googlepay.gpay

import android.content.Context
import android.util.Log
import com.google.api.client.util.PemReader
import com.google.api.client.util.SecurityUtils
import org.json.JSONObject
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec

object GooglePayConfig {
    const val TAG = "Google Pay"
    const val SERVICE_ACCOUNT_EMAIL_ADDRESS = "" // CHANGE ME < PUT YOUR SERVICE ACCOUNT EMAIL_ADDRESS >
    const val ISSUER_ID = "" // CHANGE ME < PUT YOUR ISSUER ID HERE >
    const val APPLICATION_NAME = "" // CHANGE ME < PUT YOUR APPLICATION NAME HERE > this isn't required, Used by the Google Pay API for Passes Client library

    const val JWT_TYPE = "savetoandroidpay"
    const val AUDIENCE = "google"
    val origins = arrayListOf("http://localhost:8080")
    val scopes = listOf("https://www.googleapis.com/auth/wallet_object.issuer")

    private var serviceAccountPrivateKey: RSAPrivateKey? = null

    var KEY_ID: String? = null
    private lateinit var context: Context

    val serviceAccountKey: RSAPrivateKey
        get() = serviceAccountPrivateKey!!

    fun getInstance(context: Context): GooglePayConfig {
        if (!this::context.isInitialized) {
            this.context = context
            initialize()
        }
        return this
    }

    private fun initialize() {
        // Load the private key as a java RSAPrivateKey object from service account file
        var content: String? = null
        try {
            content = readFromFile(context)
            val privateKeyJson = JSONObject(content)
            val privateKeyPkcs8 = privateKeyJson["private_key"] as String
            KEY_ID = privateKeyJson["private_key_id"] as String
            Log.i(TAG, "key = $privateKeyPkcs8")
            val reader: Reader = StringReader(privateKeyPkcs8)
            val section = PemReader.readFirstSectionAndClose(reader, "PRIVATE KEY")
            val bytes = section.base64DecodedBytes
            val keySpec = PKCS8EncodedKeySpec(bytes)
            val keyFactory = SecurityUtils.getRsaKeyFactory()
            serviceAccountPrivateKey = keyFactory.generatePrivate(keySpec) as RSAPrivateKey
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readFromFile(context: Context): String? {
        var json: String? = null
        try {
            val inputStream = context.assets.open("private_key.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return json
    }
}
