package com.saqib.googlepay.ui.plugins.transitobject

interface TransitObjectView {
    fun setLoading(isLoading: Boolean)
    fun navigateBack()
    fun showSaveToGoogleButton(visible: Boolean)
}
