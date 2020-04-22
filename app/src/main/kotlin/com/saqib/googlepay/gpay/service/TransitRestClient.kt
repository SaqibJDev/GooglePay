package com.saqib.googlepay.gpay.service

import android.content.Context
import android.util.Log
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.GenericJson
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.walletobjects.Walletobjects
import com.google.api.services.walletobjects.Walletobjects.Builder
import com.google.api.services.walletobjects.model.TransitClass
import com.google.api.services.walletobjects.model.TransitObject
import com.saqib.googlepay.gpay.GooglePayConfig
import com.saqib.googlepay.gpay.GooglePayConfig.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class TransitRestClient(private val context: Context) : CoroutineScope by MainScope() {
    private val httpTransport = NetHttpTransport()
    private val config: GooglePayConfig = GooglePayConfig.getInstance(context)
    private lateinit var credentials: GoogleCredential
    private lateinit var client: Walletobjects

    fun insertTransitClass(transitClass: TransitClass): GenericJson {
        var response: GenericJson? = null
        makeOauthCredential(context)
        createWalletClient()

        try {
            response = client.transitclass().insert(transitClass).execute()
            response!!["code"] = 200
            Log.i("Google Pay", response.toPrettyString())
        } catch (gException: GoogleJsonResponseException) {
            Log.i(TAG, ">>>> [START] Google Server Error response class:")
            Log.i(TAG, gException.details.message)
            Log.i(TAG, ">>>> [END] Google Server Error response\n")
            response = gException.details
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    fun insertTransitObject(transitObject: TransitObject): GenericJson {
        var response: GenericJson? = null
        makeOauthCredential(context)
        createWalletClient()

        try {
            response = client.transitobject().insert(transitObject).execute()
            response!!["code"] = 200
            Log.i(TAG, response.toPrettyString())
        } catch (gException: GoogleJsonResponseException) {
            Log.i(TAG, ">>>> [START] Google Server Error response:")
            Log.i(TAG, gException.details.message)
            Log.i(TAG, ">>>> [END] Google Server Error response\n")
            response = gException.details
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    private fun makeOauthCredential(context: Context) {
        if (this::credentials.isInitialized) return
        // Create a JsonFactory to be used for the walletobjects client
        val jsonFactory: JsonFactory = GsonFactory()

        try {
            val inputStream = context.assets.open("private_key.json")
            credentials = GoogleCredential
                .fromStream(inputStream, httpTransport, jsonFactory)
                .createScoped(config.scopes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getTransitObject(context: Context, objectId: String?): GenericJson? {
        var response: GenericJson? = null
        makeOauthCredential(context)
        createWalletClient()
        try {
            response = client.transitobject()[objectId].execute()
            response["code"] = 200
            Log.i(TAG, "response: $response")
        } catch (gException: GoogleJsonResponseException) {
            Log.i(TAG, ">>>> [START] Google Server Error response:")
            Log.i(TAG, gException.details.message)
            Log.i(TAG, ">>>> [END] Google Server Error response\n")
            response = gException.details
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    private fun createWalletClient() {
        if (this::client.isInitialized) return
        val jsonFactory: JsonFactory = GsonFactory()
        client = Builder(httpTransport, jsonFactory, credentials)
            .setApplicationName(config.APPLICATION_NAME)
            .build()
    }
}
