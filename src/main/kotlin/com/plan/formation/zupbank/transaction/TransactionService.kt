package com.plan.formation.zupbank.transaction

import com.plan.formation.zupbank.account.AccountRepository
import com.plan.formation.zupbank.account.useCases.EnsureAccountIsValid
import com.plan.formation.zupbank.customer.useCases.EnsureCustomerExistsAndActive
import com.plan.formation.zupbank.transaction.dtos.*
import com.plan.formation.zupbank.transaction.useCases.EnsureAccountBalance
import com.plan.formation.zupbank.transaction.useCases.EnsureAccountsDifferent
import com.plan.formation.zupbank.utils.enums.TransactionType
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionService(
    private var transactionRepository: TransactionRepository,
    private var ensureCustomerExistsAndActive: EnsureCustomerExistsAndActive,
    private var accountRepository: AccountRepository,
    private var ensureAccountIsValid: EnsureAccountIsValid,
    private var ensureAccountBalance: EnsureAccountBalance,
    private var ensureAccountsDifferent: EnsureAccountsDifferent
) {
    fun listTransactions(): List<TransactionModel> = transactionRepository.findAll()

    fun deposit(
        customerDocument: String,
        transactionRequestDeposit: TransactionDepositWithdrawalView
    ): TransactionView {
        val customer = ensureCustomerExistsAndActive.handle(customerDocument)

        val account = accountRepository.findByAccountNumber(customer.account?.accountNumber!!)
            .orElseThrow { RuntimeException("Account not found") }

        if (transactionRequestDeposit.transaction_amount <= BigDecimal.ZERO) {
            throw RuntimeException("Transaction amount can't be less than or equal 0")
        }

        val newAccountBalance = account.balance!!.plus(transactionRequestDeposit.transaction_amount)

        account.apply {
            this.balance = newAccountBalance
        }

        accountRepository.save(account)

        val transactionToSave = TransactionModel(
            transactionType = TransactionType.DEPOSIT,
            customer = customer,
            account = account,
            transactionAmount = transactionRequestDeposit.transaction_amount
        )

        val transaction = transactionRepository.save(transactionToSave)

        return transaction.toView()
    }

    fun withdrawal(
        customerDocument: String,
        transactionRequestWithdrawal: TransactionDepositWithdrawalView
    ): TransactionView {
        val customer = ensureCustomerExistsAndActive.handle(customerDocument)

        val account = accountRepository.findByAccountNumber(customer.account?.accountNumber!!)
            .orElseThrow { RuntimeException("Account not found") }

        if (account.balance!! < transactionRequestWithdrawal.transaction_amount) {
            throw RuntimeException("Balance unavailable for this operation")
        }

        val newAccountBalance = account.balance!!.subtract(transactionRequestWithdrawal.transaction_amount)

        account.apply {
            this.balance = newAccountBalance
        }

        accountRepository.save(account)

        val transactionToSave = TransactionModel(
            transactionType = TransactionType.WITHDRAWAL,
            customer = customer,
            account = account,
            transactionAmount = transactionRequestWithdrawal.transaction_amount
        )

        val transaction = transactionRepository.save(transactionToSave)

        return transaction.toView()
    }

    fun transfer(
        customerDocument: String,
        transactionRequest: TransactionTransferRequestView
    ): TransactionTransferView {
        val customer = ensureCustomerExistsAndActive.handle(customerDocument)
        val originAccount = accountRepository.findByAccountNumber(customer.account?.accountNumber!!)
            .orElseThrow { RuntimeException("Account not found") }
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

        val transactionToSave = TransactionModel(
            transactionType = TransactionType.TRANSFER,
            customer = customer,
            account = destinyAccount,
            transactionAmount = transactionRequest.transaction_amount
        )

        val transaction = transactionRepository.save(transactionToSave)

        return transaction.toTransferView()
    }

    fun balance(customerDocument: String): TransactionBalanceView {
        val customer = ensureCustomerExistsAndActive.handle(customerDocument)

        val transactionToSave = TransactionModel(
            transactionType = TransactionType.BALANCE,
            customer = customer,
            account = customer.account!!,
            transactionAmount = BigDecimal.ZERO
        )

        val transaction = transactionRepository.save(transactionToSave)
        return transaction.toTransferBalanceView()
    }

    fun statement(customerDocument: String): List<TransactionModel> {
        val customer = ensureCustomerExistsAndActive.handle(customerDocument)

        val transactions = transactionRepository.findAll()
            .filter {
                it.account.customer.document == customerDocument &&
                        it.transactionType in listOf(
                    TransactionType.TRANSFER,
                    TransactionType.DEPOSIT,
                    TransactionType.WITHDRAWAL
                )
            }

        val transactionToSave = TransactionModel(
            transactionType = TransactionType.STATEMENT,
            customer = customer,
            account = customer.account!!,
            transactionAmount = BigDecimal.ZERO
        )

        transactionRepository.save(transactionToSave)

        return transactions
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

fun TransactionModel.toTransferBalanceView() = TransactionBalanceView(
    balance = "${customer.account?.balance}"
)
