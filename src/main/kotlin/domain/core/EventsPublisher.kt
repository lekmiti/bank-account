package domain.core

interface EventsPublisher {
    fun publish(events: Collection<DomainEvent>)
}