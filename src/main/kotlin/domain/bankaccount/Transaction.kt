package domain.bankaccount

import java.time.LocalDateTime

data class Transaction(
    val date: LocalDateTime = LocalDateTime.now(),
    val amount: MoneyAmount,
    val tempBalance: MoneyAmount,
    val type: OperationType
) {
    companion object {
        fun aDeposit(amount: MoneyAmount, tempBalance: MoneyAmount, date: LocalDateTime = LocalDateTime.now()) =
            Transaction(
                amount = amount,
                tempBalance = tempBalance,
                type = OperationType.A_DEPOSIT,
                date = date
            )

        fun aWithdraw(amount: MoneyAmount, tempBalance: MoneyAmount, date: LocalDateTime = LocalDateTime.now()) =
            Transaction(
                amount = amount,
                tempBalance = tempBalance,
                type = OperationType.A_WITHDRAW,
                date = date
            )
    }
}

enum class OperationType(value: String) {
    A_WITHDRAW("withdraw"),
    A_DEPOSIT("deposit")
}
