package com.publicmethod.habit

typealias Routine = String
typealias HabitId = String

data class CreateHabit(
    val id: HabitId,
    val routine: Routine
)
