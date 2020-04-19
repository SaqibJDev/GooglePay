package com.saqib.googlepay.ui.plugins.transitobject

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.saqib.googlepay.R
import com.saqib.googlepay.extension.onBackgroundIOThread
import com.saqib.googlepay.extension.onUiThread
import com.saqib.googlepay.gpay.GooglePayTransitPassReceivedEvent
import com.saqib.googlepay.gpay.model.TransitPass
import com.saqib.googlepay.gpay.service.TransitService

private const val SAVE_TO_GOOGLE_PAY_BASE_URL = "https://pay.google.com/gp/v/save/"

class TransitObjectPresenter(private val activityContext: Context, private val view: TransitObjectView) {
    private lateinit var service: TransitService
    private var lastObjectId: String? = null

    fun onAddObject(context: Context, transitObject: TransitPass) {
        view.setLoading(true)
        createService(context)
        onBackgroundIOThread {
            val objectId = service.insertObject(transitObject = transitObject.getTransitObject())
            showUIConfirmation(context, objectId)
        }
    }

    private fun createService(context: Context) {
        if (this::service.isInitialized) return
        service = TransitService(context = context)
    }

    private fun showUIConfirmation(context: Context, id: String?) = onUiThread {
        view.setLoading(false)
        id?.let {
            view.showSaveToGoogleButton(true)
            lastObjectId = it
            return@onUiThread
        }
        Toast.makeText(context, context.resources.getString(R.string.object_insertion_failed), Toast.LENGTH_LONG).show()
        view.showSaveToGoogleButton(false)
    }

    fun onSaveToGPayAppClicked() {
        lastObjectId?.let {
            val jwt = service.generateSignedJwt(it)
            Log.i("Google Pay", "JWT = $jwt")
            postIntent(jwt)
        }
    }

    private fun postIntent(jwt: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(SAVE_TO_GOOGLE_PAY_BASE_URL + jwt)
        activityContext.startActivity(intent)
    }
}
