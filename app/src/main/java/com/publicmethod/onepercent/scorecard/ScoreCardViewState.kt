package com.publicmethod.onepercent.scorecard

import com.publicmethod.habit.HabitReadModel
import com.publicmethod.onepercent.mvi.ViewState

data class ScoreCardViewState(
    val habits: List<HabitReadModel> = emptyList()
): ViewState
