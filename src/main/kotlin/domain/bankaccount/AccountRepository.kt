package domain.bankaccount

interface AccountRepository {
    fun find(iban: Iban): Account?
    fun save(account: Account): Account
}