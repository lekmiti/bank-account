package domain.account

import domain.account.Transaction.Companion.aDeposit
import domain.account.Transaction.Companion.aWithdraw
import domain.core.Aggregate
import java.math.BigDecimal

class Account(
    val iban: Iban,
    val currency: String,
    var balance: Balance = MoneyAmount(BigDecimal.ZERO, currency),
    var transactions: List<Transaction> = emptyList()
) : Aggregate() {

    init {
        check(
            """^(?:(?:IT|SM)\d{2}[A-Z]\d{22}|CY\d{2}[A-Z]\d{23}|NL\d{2}[A-Z]{4}\d{10}|LV\d{2}[A-Z]{4}\d{13}|(?:BG|BH|GB|IE)\d{2}[A-Z]{4}\d{14}|GI\d{2}[A-Z]{4}\d{15}|RO\d{2}[A-Z]{4}\d{16}|KW\d{2}[A-Z]{4}\d{22}|MT\d{2}[A-Z]{4}\d{23}|NO\d{13}|(?:DK|FI|GL|FO)\d{16}|MK\d{17}|(?:AT|EE|KZ|LU|XK)\d{18}|(?:BA|HR|LI|CH|CR)\d{19}|(?:GE|DE|LT|ME|RS)\d{20}|IL\d{21}|(?:AD|CZ|ES|MD|SA)\d{22}|PT\d{23}|(?:BE|IS)\d{24}|(?:FR|MR|MC)\d{25}|(?:AL|DO|LB|PL)\d{26}|(?:AZ|HU)\d{27}|(?:GR|MU)\d{28})${'$'}""".toRegex()
                .matches(iban)
        ) { "invalid iban $iban" }
        addEvent(AccountCreated(iban = iban, currency = currency, balance = balance))
    }

    fun deposit(amount: MoneyAmount): Balance {
        validateCurrencies(
            currency, amount.currency
        ) { "${amount.currency} unmatched with account currency $currency " }
        val depositTransaction = aDeposit(amount = amount, tempBalance = balance.plus(amount))
        transactions = transactions + depositTransaction
        balance = depositTransaction.tempBalance
        addEvent(AccountCredited(iban = iban, balance = balance, amount = amount))
        return balance
    }

    fun withdraw(amount: MoneyAmount): Balance {
        validateCurrencies(
            currency,
            amount.currency
        ) { "${amount.currency} unmatched with account currency $currency " }
        val withdrawTransaction = aWithdraw(amount = amount, tempBalance = balance.minus(amount))
        transactions = transactions + withdrawTransaction
        balance = withdrawTransaction.tempBalance
        addEvent(AccountDebited(iban = iban, balance = balance, amount = amount))
        return balance
    }

    fun printUsing(printingStrategy: PrintingStrategy): String =
        printingStrategy.print(transactions)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Account
        return iban == other.iban
    }

    override fun hashCode(): Int = iban.hashCode()


}

typealias Iban = String
typealias Balance = MoneyAmount