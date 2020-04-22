package com.saqib.googlepay.extension

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun <T> onUiThread(block: suspend () -> T): Job = GlobalScope.launch(Dispatchers.Main) {
    block()
}

fun <T> onBackgroundIOThread(block: suspend () -> T): Job = GlobalScope.launch(Dispatchers.IO) {
    block()
}
