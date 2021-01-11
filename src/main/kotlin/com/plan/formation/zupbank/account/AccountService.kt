package com.plan.formation.zupbank.account

import com.plan.formation.zupbank.account.dtos.AccountView
import com.plan.formation.zupbank.account.useCases.EnsureCustomerHasOneAccountUseCase
import com.plan.formation.zupbank.customer.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class AccountService {
    @Autowired private lateinit var accountRepository: AccountRepository
    @Autowired private lateinit var customerRepository: CustomerRepository

    @Autowired private lateinit var ensureCustomerHasOneAccountUseCase: EnsureCustomerHasOneAccountUseCase

    fun listAccounts(): List<AccountView> = accountRepository.findAll().map { account -> account.toView() }

    fun createAccount(document: String): AccountView {
        val customerFound = customerRepository.findByDocument(document)
            .orElseThrow() { RuntimeException("Customer not found") }

        ensureCustomerHasOneAccountUseCase.handle(customerFound)

        val account = AccountModel(customer = customerFound)

        val createdAccount = accountRepository.save(account)
        return createdAccount.toView()
    }

    fun updateAccount(accountNumber: String, accountToUpdate: AccountModel): AccountView {
        // Update Here

        accountRepository.save(accountToUpdate)
        return accountToUpdate.toView()

    }

    fun deleteAccount(accountNumber: String) {
        val accountToDelete = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow { RuntimeException ("Account not found") }

        return accountRepository.delete(accountToDelete)
    }

    fun findAccount(accountNumber: String): AccountView {
        val customerFound = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow { RuntimeException ("Account not found") }
        return customerFound.toView()
    }
}

fun AccountModel.toView() = AccountView(
    account_number = "$accountNumber",
    balance = "$balance",
    create_date = "$createDate".format (DateTimeFormatter.ofPattern ( "M / d / y H: m: ss" )),
    end_date = "$endDate".format (DateTimeFormatter.ofPattern ( "M / d / y H: m: ss" )),
    customer_name = "${customer.name}",
    customer_document = "${customer.document}"
)
