package com.saqib.googlepay.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saqib.googlepay.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
    }

}
