package com.saqib.googlepay.gpay.model

import com.google.api.services.walletobjects.model.Image
import com.google.api.services.walletobjects.model.ImageUri
import com.google.api.services.walletobjects.model.TransitClass
import com.saqib.googlepay.gpay.GooglePayConfig.ISSUER_ID

/******************************
 *
 * Define an Transit Pass Class
 * This class is a wrapper model class to hold application logic/representation and generate Google wallet Transit class via get method.
 * See https://developers.google.com/pay/passes/reference/v1/transitclass for Google Pay Transit class for reference
 *
 */
class TransitPassClass(
    val classId: String,
    private val issuer: String,
    private val selectedTransitType: String,
    private val imageUrl: String,
    private val backgroundColorCode: String

) {
    fun getTransitClass(): TransitClass = TransitClass().apply {
        id = "$ISSUER_ID.$classId"
        issuerName = issuer
        reviewStatus = "underReview"
        transitType = selectedTransitType
        logo = Image().setSourceUri(
            ImageUri().setUri(imageUrl)
                .setDescription("Transport Service")
        )
        hexBackgroundColor = backgroundColorCode
    }
}
