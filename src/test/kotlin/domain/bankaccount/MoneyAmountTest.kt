package domain.bankaccount

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy
import org.junit.jupiter.api.Test


internal class MoneyAmountTest {
    @Test
    fun `should add money amounts`() {
        assertThat(10.usd().plus(10.usd())).isEqualTo(20.usd())
    }

    @Test
    fun `should validate matching currencies when adding amounts`() {
        assertThatThrownBy { 10.usd().plus(10.eur()) }
            .isExactlyInstanceOf(ConflictCurrencyException::class.java)
            .hasMessage("unmatched currencies USD EUR")
    }

    @Test
    fun `should subtract money amounts`() {
        assertThat(10.usd().minus(10.usd())).isEqualTo(0.usd())
    }

    @Test
    fun `should validate matching currencies when subtracting amounts`() {
        assertThatThrownBy { 10.usd().minus(10.eur()) }
            .isExactlyInstanceOf(ConflictCurrencyException::class.java)
            .hasMessage("unmatched currencies USD EUR")
    }
}