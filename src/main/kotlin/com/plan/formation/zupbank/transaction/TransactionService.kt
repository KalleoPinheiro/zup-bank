package com.plan.formation.zupbank.transaction

import com.plan.formation.zupbank.account.AccountRepository
import com.plan.formation.zupbank.account.useCases.EnsureAccountIsValid
import com.plan.formation.zupbank.customer.CustomerRepository
import com.plan.formation.zupbank.customer.useCases.EnsureCustomerExistsAndActive
import com.plan.formation.zupbank.transaction.dtos.TransactionDepositWithdrawalView
import com.plan.formation.zupbank.transaction.dtos.TransactionTransferRequestView
import com.plan.formation.zupbank.transaction.dtos.TransactionTransferView
import com.plan.formation.zupbank.transaction.dtos.TransactionView
import com.plan.formation.zupbank.transaction.useCases.EnsureAccountBalance
import com.plan.formation.zupbank.transaction.useCases.EnsureAccountsDifferent
import com.plan.formation.zupbank.utils.enums.TransactionType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TransactionService {
    @Autowired private lateinit var transactionRepository: TransactionRepository
    @Autowired private lateinit var customerRepository: CustomerRepository
    @Autowired private lateinit var accountRepository: AccountRepository
    @Autowired private lateinit var ensureCustomerExistsAndActive: EnsureCustomerExistsAndActive
    @Autowired private lateinit var ensureAccountIsValid: EnsureAccountIsValid
    @Autowired private lateinit var ensureAccountBalance: EnsureAccountBalance
    @Autowired private lateinit var ensureAccountsDifferent: EnsureAccountsDifferent

    fun listTransactions(): List<TransactionModel> = transactionRepository.findAll()

    fun deposit(customer: String, transactionRequestWithdrawal: TransactionDepositWithdrawalView): TransactionView {
        val customer = ensureCustomerExistsAndActive.handle(customer)

        val account = accountRepository.findByAccountNumber(customer.account?.accountNumber!!).orElseThrow { RuntimeException("Account not found") }

        if(account.balance!! < transactionRequestWithdrawal.transaction_amount) {
            throw RuntimeException("Balance unavailable for this operation")
        }

        val newAccountBalance = account.balance!!.plus(transactionRequestWithdrawal.transaction_amount)

        account.apply {
            this.balance = newAccountBalance
        }

        accountRepository.save(account)

        val transactionToSave = TransactionModel(transactionType = TransactionType.DEPOSIT, customer = customer, account = account, transactionAmount = transactionRequestWithdrawal.transaction_amount)

        val transaction = transactionRepository.save(transactionToSave)

        return transaction.toView()
    }

    fun withdrawal(customer: String, transactionRequestWithdrawal: TransactionDepositWithdrawalView): TransactionView {
        val customer = ensureCustomerExistsAndActive.handle(customer)

        val account = accountRepository.findByAccountNumber(customer.account?.accountNumber!!).orElseThrow { RuntimeException("Account not found") }

        if(account.balance!! < transactionRequestWithdrawal.transaction_amount) {
            throw RuntimeException("Balance unavailable for this operation")
        }

        val newAccountBalance = account.balance!!.subtract(transactionRequestWithdrawal.transaction_amount)

        account.apply {
            this.balance = newAccountBalance
        }

        accountRepository.save(account)

        val transactionToSave = TransactionModel(transactionType = TransactionType.WITHDRAWAL, customer = customer, account = account, transactionAmount = transactionRequestWithdrawal.transaction_amount)

        val transaction = transactionRepository.save(transactionToSave)

        return transaction.toView()
    }

    fun transfer(customer: String, transactionRequest: TransactionTransferRequestView): TransactionTransferView {
        val customer = ensureCustomerExistsAndActive.handle(customer)
        val originAccount = accountRepository.findByAccountNumber(customer.account?.accountNumber!!).orElseThrow { RuntimeException ("Account not found") }
        val destinyAccount = ensureAccountIsValid.handle(transactionRequest.account)

        ensureAccountsDifferent.handle(originAccount.accountNumber!!, destinyAccount.accountNumber!!)
        ensureAccountBalance.handle(originAccount, transactionRequest.transaction_amount)

        val newAccountOriginBalance = originAccount.balance!!.subtract(transactionRequest.transaction_amount)
        val newAccountDestinyBalance = destinyAccount.balance!!.plus(transactionRequest.transaction_amount)

        originAccount.apply {
            this.balance = newAccountOriginBalance
        }

        destinyAccount.apply {
            this.balance = newAccountDestinyBalance
        }

        accountRepository.save(originAccount)
        accountRepository.save(destinyAccount)

        val transactionToSave = TransactionModel(transactionType = TransactionType.TRANSFER, customer = customer, account = destinyAccount, transactionAmount = transactionRequest.transaction_amount)

        val transaction = transactionRepository.save(transactionToSave)

        return transaction.toTransferView()
    }

}

fun TransactionModel.toView() = TransactionView(
    transaction_type = "$transactionType",
    transaction_amount = "$transactionAmount",
    transaction_date = "$transactionDate",
    customer = customer.name,
    account_number = "${account.accountNumber}",
    account_balance = "${account.balance}",
    checking_copy = "$transactionType-$id"
)

fun TransactionModel.toTransferView() = TransactionTransferView(
    transaction_type = "$transactionType",
    transaction_amount = "$transactionAmount",
    transaction_date = "$transactionDate",
    customer_origin = customer.name,
    customer_destiny = account.customer.name,
    account_number_origin = customer.account?.accountNumber!!,
    account_number_destiny = "${account.accountNumber}",
    checking_copy = "$transactionType-$id"
)
