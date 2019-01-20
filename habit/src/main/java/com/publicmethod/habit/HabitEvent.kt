package com.publicmethod.habit

typealias EventId = String

sealed class HabitEvent(val id: EventId)

data class HabitCreated(
    val habitId: HabitId,
    val routine: Routine
) : HabitEvent(
    habitId
)
