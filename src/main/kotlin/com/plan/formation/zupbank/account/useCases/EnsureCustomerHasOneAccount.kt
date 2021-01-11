package com.plan.formation.zupbank.account.useCases

import com.plan.formation.zupbank.customer.CustomerModel
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class EnsureCustomerHasOneAccountUseCase {
    fun handle(customer: CustomerModel){
        if (customer.account !== null) {
            throw RuntimeException("Customer already has an associated account")
        }
    }
}