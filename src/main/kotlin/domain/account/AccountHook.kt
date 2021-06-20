package domain.account

interface AccountHook {
    val accountRepository: AccountRepository

    fun <T> withExistingAccount(iban: Iban, body: (account: Account) -> T): T {
        val account = accountRepository.find(iban)
        return account?.let { body(it) } ?: throw DataNotFoundException("No account with iban $iban")
    }
}