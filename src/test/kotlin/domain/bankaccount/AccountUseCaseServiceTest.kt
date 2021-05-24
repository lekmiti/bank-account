package domain.bankaccount

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.Test

internal class AccountUseCaseServiceTest {
    private val accountRepositoryMock = mockk<AccountRepository>(relaxed = true)
    private val accountUseCaseService = AccountUseCaseService(accountRepositoryMock)

    private val iban = "FR7630001007941234567890185"

    @Test
    fun `should deposit money in the account`() {
        val spiedAccount = spyk(Account(iban = iban, currency = "USD"))
        every { accountRepositoryMock.find(iban) } returns spiedAccount

        // when
        accountUseCaseService.deposit(TransactionCommand(iban, 10.usd()))

        // then
        verify(exactly = 1) { spiedAccount.deposit(any()) }
        verify(exactly = 1) { accountRepositoryMock.save(spiedAccount) }
    }

    @Test
    fun `should throw exception when deposit in a non existing account`() {
        every { accountRepositoryMock.find(any()) } returns null

        AssertionsForClassTypes.assertThatThrownBy { accountUseCaseService.deposit(TransactionCommand(iban, 10.usd())) }
            .isExactlyInstanceOf(DataNotFoundException::class.java)
            .hasMessage("No account with iban $iban")
    }

    @Test
    fun `should withdraw money from the account`() {
        val spiedAccount = spyk(Account(iban = iban, currency = "USD"))
        every { accountRepositoryMock.find(iban) } returns spiedAccount

        // when
        accountUseCaseService.withdraw(TransactionCommand(iban, 10.usd()))

        // then
        verify(exactly = 1) { spiedAccount.withdraw(any()) }
        verify(exactly = 1) { accountRepositoryMock.save(spiedAccount) }
    }

    @Test
    fun `should throw exception when withdraw from a non existing account`() {
        every { accountRepositoryMock.find(any()) } returns null

        AssertionsForClassTypes.assertThatThrownBy { accountUseCaseService.deposit(TransactionCommand(iban, 10.usd())) }
            .isExactlyInstanceOf(DataNotFoundException::class.java)
            .hasMessage("No account with iban $iban")
    }
}