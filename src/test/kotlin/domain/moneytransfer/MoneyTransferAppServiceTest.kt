package domain.moneytransfer

import domain.account.*
import domain.core.EventsPublisher
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class MoneyTransferAppServiceTest {
    private val accountRepositoryMock = mockk<AccountRepository>(relaxed = true)
    private val moneyTransferServiceMock = mockk<MoneyTransferService>(relaxed = true)
    private val eventsPublisherMock = mockk<EventsPublisher>(relaxed = true)
    private val moneyTransferAppService =
        MoneyTransferAppService(accountRepositoryMock, moneyTransferServiceMock, eventsPublisherMock)

    private val ibanA = "FR7630001007941234567890185"
    private val ibanB = "FR5530001007941234567890166"

    @Test
    fun `should transfer money from account A to account B`() {
        // given
        val spiedAccountA = spyk(Account(iban = ibanA, currency = "USD"))
        val spiedAccountB = spyk(Account(iban = ibanB, currency = "USD"))
        every { accountRepositoryMock.find(ibanA) } returns spiedAccountA
        every { accountRepositoryMock.find(ibanB) } returns spiedAccountB

        // when
        val transferMoneyCommand = TransferMoneyCommand(from = ibanA, to = ibanB, 100.usd())
        moneyTransferAppService.transferMoney(transferMoneyCommand)


        // then
        verify(exactly = 1) { moneyTransferServiceMock.transferMoney(spiedAccountA, spiedAccountB, 100.usd()) }
        verify(exactly = 1) { accountRepositoryMock.save(spiedAccountA, spiedAccountB) }
        verify(exactly = 1) { eventsPublisherMock.publish(any()) }
    }


}