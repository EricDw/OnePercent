package com.publicmethod.onepercent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ScoreCardViewModel(
    private val workerScope: WorkerScope = object : WorkerScope {
        override val coroutineContext: CoroutineContext =
            Dispatchers.Default
    },
    ioScope: IOScope = object : IOScope {
        override val coroutineContext: CoroutineContext =
            Dispatchers.IO
    }
) : ViewModel(), StateViewModel<ScoreCardViewState> {

    private val _state = MutableLiveData<ScoreCardViewState>()

    override val state: LiveData<ScoreCardViewState>
        get() = _state

    private val reducer = ioScope.actor<ActionResult>(
        context = ioScope.coroutineContext,
        capacity = Channel.UNLIMITED
    ) {

        var internalState = ScoreCardViewState()

        for (result in channel) {
            when (result) {
                is InitializeResult -> {
                    _state.value = internalState
                }
            }
        }

    }

    private val processor = ioScope.actor<Action>(
        context = ioScope.coroutineContext,
        capacity = Channel.UNLIMITED
    ) {

        for (action in channel) {
            when (action) {
                is InitializeAction -> {
                    reducer.send(InitializeResult)
                }
            }
        }

    }

    private val interpreter = workerScope.actor<ViewCommand>(
        context = workerScope.coroutineContext,
        capacity = Channel.UNLIMITED
    ) {

        for (command in channel) {
            when (command) {
                is InitializeCommand -> {
                    processor.send(InitializeAction)
                }
            }
        }

    }


    override fun sendCommand(command: ViewCommand) {
        workerScope.launch {
            interpreter.send(command)
        }
    }
}
