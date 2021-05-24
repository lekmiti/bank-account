package domain.bankaccount

data class ConflictCurrencyException(override val message: String) : RuntimeException(message)

data class DataNotFoundException(override val message: String) : RuntimeException(message)


fun validateCurrencies(
    currency: String,
    otherCurrency: String,
    lazyMessage: () -> String = { "unmatched currencies $currency $otherCurrency" }
) {
    if (currency != otherCurrency)
        throw ConflictCurrencyException(lazyMessage())

}
