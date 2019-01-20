package com.publicmethod.habit

fun habitFolder(
    habit: Habit,
    event: HabitEvent
): Habit = when (habit) {
    EmptyHabit -> when (event) {
        is HabitCreated -> with(event) {
           return@with UnratedHabit.create(
                habitId,
                routine
            )
        }
    }
    is UnratedHabit -> TODO()
}