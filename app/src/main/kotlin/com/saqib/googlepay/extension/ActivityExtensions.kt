package com.saqib.googlepay.extension

import android.app.Activity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.saqib.googlepay.R

fun Activity.enableBackNavigation() {
    val activity = this
    findViewById<Toolbar>(R.id.toolbar).apply {
        setNavigationIcon(R.drawable.ic_arrow_back)
        setNavigationOnClickListener { (activity as AppCompatActivity).onBackPressed() }
    }
}

fun Activity.disableBackNavigation() {
    findViewById<Toolbar>(R.id.toolbar).navigationIcon = null
}

fun Activity.showProgressbar(shouldShow: Boolean) {
    findViewById<LinearLayout>(R.id.progressbar).visibility = if(shouldShow) VISIBLE else GONE
}
