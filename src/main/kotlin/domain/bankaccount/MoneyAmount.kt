package domain.bankaccount

import java.math.BigDecimal

data class MoneyAmount(val amount: BigDecimal, val currency: String) {

    fun plus(moneyAmount: MoneyAmount): MoneyAmount {
        validateCurrencies(this.currency, moneyAmount.currency)
        return copy(amount = amount.add(moneyAmount.amount))
    }

    fun minus(moneyAmount: MoneyAmount): MoneyAmount {
        validateCurrencies(this.currency, moneyAmount.currency)
        return copy(amount = amount.subtract(moneyAmount.amount))
    }
}

fun Int.usd() = MoneyAmount(BigDecimal(this), "USD")
fun Int.eur() = MoneyAmount(BigDecimal(this), "EUR")
