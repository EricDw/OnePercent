package com.publicmethod.habit

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object TestingScope : IOScope {
    override val coroutineContext: CoroutineContext =
            Dispatchers.Unconfined
}
