package com.plan.formation.zupbank.transaction

import com.google.gson.Gson
import com.plan.formation.zupbank.transaction.dtos.TransactionDepositWithdrawalView
import com.plan.formation.zupbank.transaction.dtos.TransactionTransferRequestView
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTests {

    @Autowired
    private val mockMvc: MockMvc? = null

    val gson = Gson()

    @Test
    fun deposit() {
        val transactionBody =
            TransactionDepositWithdrawalView(transaction_amount = BigDecimal(2500))

        mockMvc!!.post("/transactions/deposit/{document}", "01943939179") {
            contentType = MediaType.APPLICATION_JSON
            content = gson.toJson(transactionBody)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.transaction_type") { value("DEPOSIT") }
        }
    }

    @Test
    fun withdrawal() {
        val transactionBody =
            TransactionDepositWithdrawalView(transaction_amount = BigDecimal(2500))

        mockMvc!!.post("/transactions/withdrawal/{document}", "01943939179") {
            contentType = MediaType.APPLICATION_JSON
            content = gson.toJson(transactionBody)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.transaction_type") { value("WITHDRAWAL") }
        }
    }

    @Test
    fun transfer() {
        val transactionBody =
            TransactionTransferRequestView(transaction_amount = BigDecimal(2500), account = "617cffc9")

        mockMvc!!.post("/transactions/transfer/{document}", "01943939179") {
            contentType = MediaType.APPLICATION_JSON
            content = gson.toJson(transactionBody)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.transaction_type") { value("TRANSFER") }
        }
    }

    @Test
    fun balance() {
        mockMvc!!.get("/transactions/balance/{document}", "01943939179") {
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.balance") { isNotEmpty() }
        }
    }

    @Test
    fun statement() {
        mockMvc!!.get("/transactions/statement/{document}", "01943939179") {
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$") { isArray() }
            jsonPath("$") { isNotEmpty() }
        }
    }
}
