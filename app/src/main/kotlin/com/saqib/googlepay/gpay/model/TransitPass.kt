package com.saqib.googlepay.gpay.model

import com.google.api.services.walletobjects.model.*
import com.saqib.googlepay.gpay.GooglePayConfig

class TransitPass(
    private val transitClassId: String,
    private val objectId: String,
    private val selectedTripType: String,
    val origin: String,
    val destination: String,
    val originCode: String,
    val destinationCode: String,
    val passengerName: String,
    val seatNumber: String,
    val barcodeValue: String
) {
    fun getTransitObject(): TransitObject {
        val objectId = "${GooglePayConfig.ISSUER_ID}.$objectId"
        return TransitObject().apply {
            classId = "${GooglePayConfig.ISSUER_ID}.$transitClassId"
            id = objectId
            state = "active"
            tripType = selectedTripType
            ticketLeg = TicketLeg().apply {
                originStationCode = originCode
                originName = LocalizedString().setDefaultValue(TranslatedString().setLanguage("en-US").setValue(origin))
//                departureDateTime = // Add Time
//                arrivalDateTime =  // Add Time
                destinationStationCode = destinationCode
                destinationName = LocalizedString().setDefaultValue(TranslatedString().setLanguage("en-US").setValue(destination))
                ticketSeat = TicketSeat().apply {
                    coach = "1"
                    seat = seatNumber
                }
                transitOperatorName = LocalizedString().setDefaultValue(TranslatedString().setLanguage("en-US").setValue("Flying Bus transits"))
            }
            passengerType = "singlePassenger"
            passengerNames = passengerName
            barcode = Barcode().apply {
                type = "qrCode"
                value = barcodeValue
                alternateText = "Reservation: $barcodeValue"
            }
            infoModuleData = InfoModuleData().apply {
                labelValueRows = mutableListOf<LabelValueRow>().apply {

                }
            }
        }
    }
}
