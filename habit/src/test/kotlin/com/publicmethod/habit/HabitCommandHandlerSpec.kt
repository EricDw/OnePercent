package com.publicmethod.habit

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals

object HabitCommandHandlerSpec : Spek({

    Feature("HabitCommandHandler") {

        val returnChannel = Channel<HabitReadModel>(
            capacity = Channel.CONFLATED
        )

        val eventStore: EventStore = inMemoryEventStore()
        val queryHandler: SendChannel<HabitQuery> = habitQueryHandler(
            returnChannel,
            eventStore,
            TestingScope
        )

        val handler = habitCommandHandler(
            eventStore,
            TestingScope
        )

        Scenario("Sending commands to handler") {

            val testId = "VALID_TEST_ID"
            val expectedRoutine = "Brush teeth"
            val expected = HabitReadModel(testId, expectedRoutine)

            When("Sending CreateHabit command") {
                val input = CreateHabit(testId, expectedRoutine)

                TestingScope.launch {
                    handler.send(input)
                }
            }

            And("Querying for HabitReadModel") {
                val query = HabitQuery(testId)
                TestingScope.launch {
                    queryHandler.send(query)
                }
            }

            Then("Should receive $expected") {
                lateinit var actual: HabitReadModel

                TestingScope.launch {
                    actual = returnChannel.receive()
                }

                assertEquals(expected, actual)
            }


        }

    }

})

fun inMemoryEventStore(): EventStore = object : EventStore {

    val events: MutableMap<EventId, MutableList<HabitEvent>> =
        mutableMapOf()

    override fun eventsForId(eventId: EventId): List<HabitEvent> =
        events[eventId] ?: emptyList()

    override fun add(event: HabitEvent) {
        val stream: MutableList<HabitEvent> =
            events[event.id] ?: mutableListOf()

        stream.add(event)

        events[event.id] = stream

    }
}
