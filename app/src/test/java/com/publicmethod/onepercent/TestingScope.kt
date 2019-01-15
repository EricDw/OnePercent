package com.publicmethod.onepercent

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object TestingScope : WorkerScope, IOScope {
    override val coroutineContext: CoroutineContext =
            Dispatchers.Unconfined
}