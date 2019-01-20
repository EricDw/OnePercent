package com.publicmethod.habit

interface EventStore {
    fun eventsForId(eventId: EventId): List<HabitEvent>
    fun add(event: HabitEvent)
}
