package com.publicmethod.onepercent

import androidx.lifecycle.LiveData

interface StateViewModel<T: ViewState> {
    val state: LiveData<T>
    fun sendCommand(command: ViewCommand)
}
