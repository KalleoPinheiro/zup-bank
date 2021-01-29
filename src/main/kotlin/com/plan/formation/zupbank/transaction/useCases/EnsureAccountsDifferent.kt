package com.plan.formation.zupbank.transaction.useCases

import org.springframework.stereotype.Service

@Service
class EnsureAccountsDifferent {
    fun handle (originAccountNumber: String, destinyAccountNumber: String) {
        if(originAccountNumber === destinyAccountNumber) {
            throw RuntimeException("Accounts cannot be the same")
        }
    }
}