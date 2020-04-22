package com.saqib.googlepay.gpay.service

import android.content.Context
import android.util.Log
import com.google.api.client.json.GenericJson
import com.google.api.services.walletobjects.model.TransitClass
import com.google.api.services.walletobjects.model.TransitObject
import com.google.gson.JsonObject
import com.saqib.googlepay.gpay.GooglePayConfig.TAG
import com.saqib.googlepay.gpay.jwt.Jwt

private const val EXISTS_MESSAGE = "No changes will be made when saved by link. " +
        "To update info, use update() or patch(). " +
        "For an example, see " +
        "https://developers.google.com/pay/passes/guides/get-started/implementing-the-api/engage-through-google-pay#update-state\n"

class TransitService(private val context: Context, private val objectPayload: TransitObject? = null) {

    internal enum class WalletObjectType { CLASS, OBJECT }

    private val restClient = TransitRestClient(context)

    fun insertClass(transitClass: TransitClass): String? {
        var classResponse: GenericJson? = null
        return try {
            Log.i(TAG, "Class to be inserted = $transitClass")
            Log.i(TAG, "Making REST call to insert class")
            classResponse = restClient.insertTransitClass(transitClass)
            handleInsertCallStatusCode(classResponse, WalletObjectType.CLASS, transitClass.id, null)
            transitClass.id
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun insertObject(transitObject: TransitObject): String? {
        var objectResponse: GenericJson? = null
        return try {
            Log.i(TAG, "Object to be inserted = $transitObject")
            Log.i(TAG, "Making REST call to insert object")
            objectResponse = restClient.insertTransitObject(transitObject)
            handleInsertCallStatusCode(objectResponse, WalletObjectType.OBJECT, transitObject.id, transitObject.classId)
            transitObject.id
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun generateSignedJwt(objectId: String): String {
        val jwtPayload = JsonObject().apply { addProperty("id", objectId) }
        val googlePassJwt = Jwt(context).apply { addTransitObject(jwtPayload) }
        return googlePassJwt.generateSignedJwt()
    }

    @Throws(Exception::class)
    private fun handleInsertCallStatusCode(
        insertCallResponse: GenericJson,
        idType: WalletObjectType,
        id: String,
        checkClassId: String?
    ) {
        if (insertCallResponse["code"] as Int == 200) {
            Log.i(TAG, String.format("%s id (%s) insertion success!\n", idType, id))
        } else if (insertCallResponse["code"] as Int == 409) { // Id resource exists for this issuer account
            Log.i(TAG, String.format("%sId: (%s) already exists. %s", idType, id, EXISTS_MESSAGE))
            // for object insert, do additional check
            if (idType == WalletObjectType.OBJECT) {
                var objectResponse: GenericJson? = null
                objectResponse = restClient.getTransitObject(context, id)
                // check if object's classId matches target classId
                val classIdOfObjectId = objectResponse!!["classId"] as String?
                if (classIdOfObjectId != checkClassId && checkClassId != null) {
                    throw Exception(
                        String.format(
                            "the classId of inserted object is (%s). " +
                                    "It does not match the target classId (%s). The saved object will not " +
                                    "have the class properties you expect.",
                            classIdOfObjectId,
                            checkClassId
                        )
                    )
                }
            }
        } else {
            throw Exception(String.format("%s insert issue.", idType) + insertCallResponse.toPrettyString())
        }
        return
    }
}
