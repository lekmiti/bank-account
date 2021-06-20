package domain.core

import java.time.Instant
import java.util.*

interface DomainEvent {
    val id: UUID
    val date: Instant
}
