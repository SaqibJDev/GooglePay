package com.saqib.googlepay.ui.plugins.transitclass

import android.content.Context
import android.widget.Toast
import com.saqib.googlepay.R
import com.saqib.googlepay.extension.onBackgroundIOThread
import com.saqib.googlepay.extension.onUiThread
import com.saqib.googlepay.gpay.model.TransitPassClass
import com.saqib.googlepay.gpay.service.TransitService

class TransitClassPresenter(private val view: TransitClassView) {

    fun onAddClass(context: Context, transitClass: TransitPassClass) {
        view.setLoading(true)
        val service = TransitService(context = context)
        onBackgroundIOThread {
            val id = service.insertClass(transitClass = transitClass.getTransitClass())
            showUIConfirmation(context, id)
        }
    }

    private fun showUIConfirmation(context: Context, classId: String?) = onUiThread {
        view.setLoading(false)
        val message = if (classId != null) context.resources.getString(R.string.class_inserted, classId ) else context.resources.getString(R.string.class_insertion_failed)
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
