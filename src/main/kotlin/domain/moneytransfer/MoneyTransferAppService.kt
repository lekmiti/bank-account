package domain.moneytransfer

import domain.account.*
import domain.core.DomainEvent
import domain.core.EventsPublisher
import java.time.Instant
import java.util.*

interface MoneyTransferUseCase {
    fun transferMoney(transferMoneyCommand: TransferMoneyCommand)
}

data class TransferMoneyCommand(val from: Iban, val to: Iban, val amount: MoneyAmount)

class MoneyTransferAppService(
    override val accountRepository: AccountRepository,
    private val moneyTransferService: MoneyTransferService,
    private val eventsPublisher: EventsPublisher
) : MoneyTransferUseCase, AccountHook {

    @Synchronized
    override fun transferMoney(transferMoneyCommand: TransferMoneyCommand) =
        withExistingAccounts(transferMoneyCommand.from, transferMoneyCommand.to) { from, to ->
            moneyTransferService.transferMoney(from, to, transferMoneyCommand.amount)
            accountRepository.save(from, to)
            publishTransferMoneyEvents(from, to, transferMoneyCommand.amount)
        }

    private fun publishTransferMoneyEvents(from: Account, to: Account, amount: MoneyAmount) {
        val domainEvents = from.events() + to.events() +
                TransferApplied(from = from.iban, to = to.iban, amount = amount)
        eventsPublisher.publish(domainEvents)
    }
}

data class TransferApplied(
    override val id: UUID = UUID.randomUUID(),
    override val date: Instant = Instant.now(),
    val from: Iban,
    val to: Iban,
    val amount: MoneyAmount
) : DomainEvent