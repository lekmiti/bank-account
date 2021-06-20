package domain.core


abstract class Aggregate {

    private var events: Collection<DomainEvent> = emptyList()

    protected fun addEvent(event: DomainEvent) {
        events += event
    }

    fun events() = events
}