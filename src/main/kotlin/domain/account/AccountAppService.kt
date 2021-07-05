package domain.account

import domain.core.EventsPublisher


interface AccountUseCase {
    fun deposit(depositCommand: DepositCommand): Account
    fun withdraw(withdrawCommand: WithdrawCommand): Account
    fun printUsing(printStrategy: PrintCommand): String
}

data class DepositCommand(val iban: Iban, val amount: MoneyAmount)
data class WithdrawCommand(val iban: Iban, val amount: MoneyAmount)
data class PrintCommand(val iban: Iban, val printingStrategy: PrintingStrategy)

class AccountAppService(
    override val accountRepository: AccountRepository,
    private val eventsPublisher: EventsPublisher
) : AccountUseCase, AccountHook {

    @Synchronized
    override fun deposit(depositCommand: DepositCommand): Account =
        withExistingAccount(depositCommand.iban) {
            it.deposit(depositCommand.amount)
            accountRepository.save(it)
            eventsPublisher.publish(it.events())
            it
        }

    @Synchronized
    override fun withdraw(withdrawCommand: WithdrawCommand): Account =
        withExistingAccount(withdrawCommand.iban) {
            it.withdraw(withdrawCommand.amount)
            accountRepository.save(it)
            eventsPublisher.publish(it.events())
            it
        }

    override fun printUsing(printCommand: PrintCommand): String =
        withExistingAccount(printCommand.iban) {
            it.printUsing(printCommand.printingStrategy)
        }


}