package domain.bankaccount


interface AccountUseCase {
    fun deposit(depositCommand: DepositCommand): Account
    fun withdraw(withdrawCommand: WithdrawCommand): Account
    fun printUsing(printStrategy: PrintCommand): String
}

data class DepositCommand(val iban: Iban, val amount: MoneyAmount)
data class WithdrawCommand(val iban: Iban, val amount: MoneyAmount)
data class PrintCommand(val iban: Iban, val printingStrategy: PrintingStrategy)

class AccountUseCaseService(private val repository: AccountRepository) : AccountUseCase {

    @Synchronized
    override fun deposit(depositCommand: DepositCommand): Account =
        withExistingAccount(depositCommand.iban) {
            it.deposit(depositCommand.amount)
            repository.save(it)
        }

    @Synchronized
    override fun withdraw(withdrawCommand: WithdrawCommand): Account =
        withExistingAccount(withdrawCommand.iban) {
            it.withdraw(withdrawCommand.amount)
            repository.save(it)
        }

    override fun printUsing(printCommand: PrintCommand): String =
        withExistingAccount(printCommand.iban) {
            it.printUsing(printCommand.printingStrategy)
        }


    private fun <T> withExistingAccount(iban: Iban, body: (account: Account) -> T): T {
        val account = repository.find(iban)
        return account?.let { body(it) } ?: throw DataNotFoundException("No account with iban $iban")
    }

}