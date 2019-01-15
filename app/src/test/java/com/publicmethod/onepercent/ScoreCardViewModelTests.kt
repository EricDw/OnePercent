package com.publicmethod.onepercent

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ScoreCardViewModelTests {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: StateViewModel<ScoreCardViewState>
    private lateinit var actualViewState: ScoreCardViewState

    @Before
    fun setUp() {
        viewModel = ScoreCardViewModel(
            TestingScope,
            TestingScope
        )
        viewModel.state.observeForever {
            actualViewState = it
        }
    }

    @Test
    fun `given InitializeCommand when sendCommand then return emptyHabitsList`() {
        // Arrange
        val input = InitializeCommand
        val expected = ScoreCardViewState()

        // Act
        viewModel.sendCommand(input)
        val actual = actualViewState

        // Assert
        Assert.assertEquals(expected, actual)

    }

}