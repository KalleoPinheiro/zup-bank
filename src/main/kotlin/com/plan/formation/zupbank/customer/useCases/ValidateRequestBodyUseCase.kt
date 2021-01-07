package com.plan.formation.zupbank.customer.useCases

import com.plan.formation.zupbank.customer.CustomerModel
import com.plan.formation.zupbank.utils.CpfValidator
import org.springframework.stereotype.Service

@Service
class ValidateRequestBodyUseCase {
    fun handle(customerRequest: CustomerModel) {
        if(customerRequest.name.length < 3 || customerRequest.name.length > 200) {
            throw RuntimeException ("User name must be between 3 and 200 characters")
        }

        if (!CpfValidator.myValidateCPF(customerRequest.document)) {
            throw RuntimeException ("User document: ${customerRequest.document} is invalid")
        }
    }
}