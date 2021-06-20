package domain.account


import domain.core.DomainEvent
import java.time.Instant
import java.util.*

interface AccountEvent : DomainEvent

data class AccountCreated(
    override val id: UUID = UUID.randomUUID(),
    override val date: Instant = Instant.now(),
    val iban: Iban,
    val currency: String,
    val balance: Balance
) : AccountEvent

data class AccountCredited(
    override val id: UUID = UUID.randomUUID(),
    override val date: Instant = Instant.now(),
    val iban: Iban,
    val amount: MoneyAmount,
    val balance: Balance
) : AccountEvent

data class AccountDebited(
    override val id: UUID = UUID.randomUUID(),
    override val date: Instant = Instant.now(),
    val iban: Iban,
    val amount: MoneyAmount,
    val balance: Balance,
) : AccountEvent
