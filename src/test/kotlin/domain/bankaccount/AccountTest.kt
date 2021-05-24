package domain.bankaccount

import org.assertj.core.api.AssertionsForClassTypes
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

internal class AccountTest {

    @Test
    fun `should deposit money in the account`() {
        val account = Account(iban = "FR7630001007941234567890185", currency = "USD")
        assertThat(account.deposit(1000.usd())).isEqualTo(1000.usd())
        assertThat(account.transaxtions.size).isEqualTo(1)
    }

    @Test
    fun `should not able to deposit money with a different currency`() {
        val account = Account(iban = "FR7630001007941234567890185", currency = "USD")
        AssertionsForClassTypes.assertThatThrownBy { account.deposit(10.eur()) }
            .isExactlyInstanceOf(ConflictCurrencyException::class.java)
            .hasMessage("EUR unmatched with account currency USD ")
    }

    @Test
    fun `should withdraw money from the account`() {
        val account = Account(iban = "FR7630001007941234567890185", currency = "USD", balance = 1000.usd())
        assertThat(account.withdraw(500.usd())).isEqualTo(500.usd())
        assertThat(account.transaxtions.size).isEqualTo(1)
    }

    @Test
    fun `should not able to withdraw money with a different currency`() {
        val account = Account(iban = "FR7630001007941234567890185", currency = "USD")
        AssertionsForClassTypes.assertThatThrownBy { account.withdraw(10.eur()) }
            .isExactlyInstanceOf(ConflictCurrencyException::class.java)
            .hasMessage("EUR unmatched with account currency USD ")
    }
}