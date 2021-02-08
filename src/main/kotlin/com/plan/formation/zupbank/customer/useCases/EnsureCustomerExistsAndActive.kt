package com.plan.formation.zupbank.customer.useCases

import com.plan.formation.zupbank.account.useCases.EnsureAccountIsValid
import com.plan.formation.zupbank.customer.CustomerModel
import com.plan.formation.zupbank.customer.CustomerRepository
import org.springframework.stereotype.Service

@Service
class EnsureCustomerExistsAndActive(
    private var repository: CustomerRepository,
    private var ensureAccountIsValid: EnsureAccountIsValid
) {
    fun handle(document: String): CustomerModel {
        val existingCustomer = repository.findByDocument(document)
            .orElseThrow { RuntimeException("Customer not found") }

        existingCustomer.account?.accountNumber?.let { ensureAccountIsValid.handle(it) }

        return existingCustomer
    }
}