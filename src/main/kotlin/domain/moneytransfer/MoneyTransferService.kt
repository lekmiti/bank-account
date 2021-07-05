package domain.moneytransfer

import domain.account.Account
import domain.account.MoneyAmount

class MoneyTransferService {

    fun transferMoney(from: Account, to: Account, amount: MoneyAmount) {
        from.withdraw(amount)
        to.deposit(amount)
    }
}