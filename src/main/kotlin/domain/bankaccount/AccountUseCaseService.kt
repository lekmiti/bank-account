package domain.bankaccount


interface AccountUseCase {
    fun deposit(transactionCommand: TransactionCommand): Account
    fun withdraw(transactionCommand: TransactionCommand): Account
    fun printUsing(printingStrategy: PrintingCommand): String
}

data class TransactionCommand(val iban: Iban, val amount: MoneyAmount)
data class PrintingCommand(val iban: Iban, val printingStrategy: PrintingStrategy)

class AccountUseCaseService(private val repository: AccountRepository) : AccountUseCase {

    @Synchronized
    override fun deposit(transactionCommand: TransactionCommand): Account =
        withExistingAccount(transactionCommand.iban) {
            it.deposit(transactionCommand.amount)
            repository.save(it)
        }

    @Synchronized
    override fun withdraw(transactionCommand: TransactionCommand): Account =
        withExistingAccount(transactionCommand.iban) {
            it.withdraw(transactionCommand.amount)
            repository.save(it)
        }

    override fun printUsing(printingCommand: PrintingCommand): String =
        withExistingAccount(printingCommand.iban) {
            it.printUsing(printingCommand.printingStrategy)
        }


    private fun <T> withExistingAccount(iban: Iban, body: (account: Account) -> T): T {
        val account = repository.find(iban)
        return account?.let { body(it) } ?: throw DataNotFoundException("No account with iban $iban")
    }

}