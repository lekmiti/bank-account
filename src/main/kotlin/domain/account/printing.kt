package domain.account

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.google.gson.Gson

interface PrintingStrategy {
    fun print(transactions: List<Transaction>): String

    object JsonFormatPrinter : PrintingStrategy {
        override fun print(transactions: List<Transaction>): String =
            Gson().toJson(transactions)
    }

    object XmlFormatPrinter : PrintingStrategy {
        override fun print(transactions: List<Transaction>): String =
            XmlMapper().writeValueAsString(transactions)
    }
}