package domain.account

interface AccountRepository {
    fun find(iban: Iban): Account?
    fun save(account: Account): Account
    fun save(vararg accounts: Account)

}