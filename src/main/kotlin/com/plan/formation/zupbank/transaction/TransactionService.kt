package com.plan.formation.zupbank.transaction

import com.plan.formation.zupbank.account.AccountRepository
import com.plan.formation.zupbank.customer.CustomerRepository
import com.plan.formation.zupbank.transaction.dtos.TransactionDepositView
import com.plan.formation.zupbank.transaction.dtos.TransactionView
import com.plan.formation.zupbank.utils.enums.TransactionType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionService {
    @Autowired private lateinit var transactionRepository: TransactionRepository
    @Autowired private lateinit var customerRepository: CustomerRepository
    @Autowired private lateinit var accountRepository: AccountRepository

    fun listTransactions(): List<TransactionModel> = transactionRepository.findAll()

    fun deposit(customer: String, transactionRequest: TransactionDepositView): TransactionView {
        val customer = customerRepository.findByDocument(customer)
            .orElseThrow { RuntimeException("Customer not found") }

        val account = customer.account?.accountNumber?.let {
            accountRepository.findByAccountNumber(it)
                .orElseThrow { RuntimeException("Account not found") }
        }

        if (account === null) {
            throw RuntimeException("Account not found")
        }

        val newAccountBalance = account.balance!!.plus(transactionRequest.transaction_amount)

        account.apply {
            this.balance = newAccountBalance
        }

        accountRepository.save(account)

        val transactionToSave = TransactionModel(transactionType = TransactionType.DEPOSIT, customer = customer, account = account, transactionAmount = transactionRequest.transaction_amount)

        val transaction = transactionRepository.save(transactionToSave)

        return transaction.toView()
    }

    fun withdrawal(customer: String, transactionRequest: TransactionDepositView): TransactionView {
        val customer = customerRepository.findByDocument(customer)
            .orElseThrow { RuntimeException("Customer not found") }

        val account = customer.account?.accountNumber?.let {
            accountRepository.findByAccountNumber(it)
                .orElseThrow { RuntimeException("Account not found") }
        }

        if (account === null) {
            throw RuntimeException("Account not found")
        }

        if(account.balance!! < transactionRequest.transaction_amount) {
            throw RuntimeException("Balance unavailable for this operation")
        }

        val newAccountBalance = account.balance!!.subtract(transactionRequest.transaction_amount)

        account.apply {
            this.balance = newAccountBalance
        }

        accountRepository.save(account)

        val transactionToSave = TransactionModel(transactionType = TransactionType.WITHDRAWAL, customer = customer, account = account, transactionAmount = transactionRequest.transaction_amount)

        val transaction = transactionRepository.save(transactionToSave)

        return transaction.toView()
    }

}

fun TransactionModel.toView() = TransactionView(
    transaction_type = "$transactionType",
    transaction_amount = "$transactionAmount",
    transaction_date = "$transactionDate",
    customer = "${customer.name}",
    account_number = "${account.accountNumber}",
    account_balance = "${account.balance}"
)
