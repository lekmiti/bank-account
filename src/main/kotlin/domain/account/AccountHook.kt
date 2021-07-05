package domain.account

interface AccountHook {
    val accountRepository: AccountRepository

    fun <T> withExistingAccount(iban: Iban, body: (account: Account) -> T): T {
        val account = accountRepository.find(iban)
        return account?.let { body(it) } ?: throw DataNotFoundException("No account with iban $iban")
    }

    fun <T> withExistingAccounts(
        firstIban: Iban,
        secondIban: Iban,
        body: (firstAccount: Account, secondAccount: Account) -> T
    ): T {
        val firstIAccount =
            accountRepository.find(firstIban) ?: throw DataNotFoundException("No account with iban $firstIban")
        val secondAccount =
            accountRepository.find(secondIban) ?: throw DataNotFoundException("No account with iban $secondIban")
        return body(firstIAccount, secondAccount)
    }
}