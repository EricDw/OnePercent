package com.publicmethod.habit

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlin.coroutines.CoroutineContext

@Suppress("EXPERIMENTAL_API_USAGE")
fun habitCommandHandler(
    eventStore: EventStore,
    ioScope: IOScope = object : IOScope {
        override val coroutineContext: CoroutineContext =
            Dispatchers.IO
    }
): SendChannel<CreateHabit> = ioScope.actor(
    context = ioScope.coroutineContext,
    capacity = Channel.UNLIMITED
) {

    fun List<HabitEvent>.fold(): Habit =
        fold(EmptyHabit, ::habitFolder)

    fun handleCommand(command: CreateHabit) {
        val habit = eventStore.eventsForId(command.id).fold()
        if (habit is EmptyHabit) {
            val newHabit = UnratedHabit.create(
                command.id,
                command.routine
            )
            eventStore.add(
                HabitCreated(
                    newHabit.id,
                    newHabit.routine
                )
            )
        }
    }

    for (command in channel) {
        when (command) {
            is CreateHabit -> handleCommand(command)
        }
    }

}
