package com.publicmethod.onepercent

data class ScoreCardViewState(
    val habits: List<HabitReadModel> = emptyList()
): ViewState
