package com.publicmethod.habit

sealed class Habit

object EmptyHabit : Habit()

class UnratedHabit private constructor(
    val id: HabitId,
    val routine: Routine
) : Habit() {
    companion object {
        fun create(habitId: HabitId, routine: Routine): UnratedHabit =
                UnratedHabit(
                    habitId,
                    routine
                )
    }
}
