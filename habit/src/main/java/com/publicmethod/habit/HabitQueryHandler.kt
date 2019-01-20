package com.publicmethod.habit

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlin.coroutines.CoroutineContext

@Suppress("EXPERIMENTAL_API_USAGE")
fun habitQueryHandler(
    returnChannel: SendChannel<HabitReadModel>,
    eventStore: EventStore,
    ioScope: IOScope = object : IOScope {
        override val coroutineContext: CoroutineContext =
                Dispatchers.IO
    }
): SendChannel<HabitQuery> = ioScope.actor(
    context = ioScope.coroutineContext,
    capacity = Channel.UNLIMITED
) {

    var model = HabitReadModel("", "")

    fun List<HabitEvent>.fold(): Habit =
        fold(EmptyHabit, ::habitFolder)

    suspend fun process(query: HabitQuery) {
        val habit = eventStore.eventsForId(query.habitId).fold()
        model = when (habit) {
            EmptyHabit -> model
            is UnratedHabit -> model.copy(
                id = habit.id,
                routine = habit.routine
            )
        }
        returnChannel.send(
            model
        )
    }

    for (query in channel) {
        when(query) {
           is HabitQuery -> process(query)
        }
    }


}
