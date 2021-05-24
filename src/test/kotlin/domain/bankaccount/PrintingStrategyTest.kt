package domain.bankaccount

import domain.bankaccount.Transaction.Companion.aDeposit
import domain.bankaccount.Transaction.Companion.aWithdraw
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class PrintingStrategyTest {
    @Test
    fun `should format statement as json`() {
        val now = LocalDateTime.of(2021, 5, 4, 0, 0)

        val transactions = listOf(
            aDeposit(amount = 500.usd(), tempBalance = 1000.usd(), date = now),
            aWithdraw(amount = 200.usd(), tempBalance = 800.usd(), date = now.plusDays(1)),
        )
        val statement = PrintingStrategy.JsonFormatPrinter.print(transactions)
        assertThat(statement).isEqualTo("""[{"date":{"date":{"year":2021,"month":5,"day":4},"time":{"hour":0,"minute":0,"second":0,"nano":0}},"amount":{"amount":500,"currency":"USD"},"tempBalance":{"amount":1000,"currency":"USD"},"type":"A_DEPOSIT"},{"date":{"date":{"year":2021,"month":5,"day":5},"time":{"hour":0,"minute":0,"second":0,"nano":0}},"amount":{"amount":200,"currency":"USD"},"tempBalance":{"amount":800,"currency":"USD"},"type":"A_WITHDRAW"}]""")
    }

    @Test
    fun `should format statement as xml`() {
        val now = LocalDateTime.of(2021, 5, 4, 0, 0)

        val transactions = listOf(
            aDeposit(amount = 500.usd(), tempBalance = 1000.usd(), date = now),
            aWithdraw(amount = 200.usd(), tempBalance = 800.usd(), date = now.plusDays(1)),
        )
        val statement = PrintingStrategy.XmlFormatPrinter.print(transactions)
        assertThat(statement).isEqualTo("""<ArrayList><item><date><year>2021</year><monthValue>5</monthValue><dayOfMonth>4</dayOfMonth><hour>0</hour><minute>0</minute><second>0</second><nano>0</nano><dayOfWeek>TUESDAY</dayOfWeek><dayOfYear>124</dayOfYear><month>MAY</month><chronology><id>ISO</id><calendarType>iso8601</calendarType></chronology></date><amount><amount>500</amount><currency>USD</currency></amount><tempBalance><amount>1000</amount><currency>USD</currency></tempBalance><type>A_DEPOSIT</type></item><item><date><year>2021</year><monthValue>5</monthValue><dayOfMonth>5</dayOfMonth><hour>0</hour><minute>0</minute><second>0</second><nano>0</nano><dayOfWeek>WEDNESDAY</dayOfWeek><dayOfYear>125</dayOfYear><month>MAY</month><chronology><id>ISO</id><calendarType>iso8601</calendarType></chronology></date><amount><amount>200</amount><currency>USD</currency></amount><tempBalance><amount>800</amount><currency>USD</currency></tempBalance><type>A_WITHDRAW</type></item></ArrayList>""")
    }
}