package com.plan.formation.zupbank.customer.useCases

import com.plan.formation.zupbank.customer.CustomerModel
import com.plan.formation.zupbank.utils.validators.CpfValidator
import org.springframework.stereotype.Service

@Service
class ValidatesWhetherDocumentIsValid {
    fun handle(document: String) {
        if (!CpfValidator.myValidateCPF(document)) {
            throw RuntimeException ("User document: ${document} is invalid")
        }
    }
}