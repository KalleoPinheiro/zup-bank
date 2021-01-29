package com.plan.formation.zupbank.account.useCases

import com.plan.formation.zupbank.account.AccountModel
import com.plan.formation.zupbank.account.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class EnsureAccountIsValid {
    @Autowired private lateinit var repository: AccountRepository

    fun handle(account: String): AccountModel {
        val accountVerified = repository.findByAccountNumber(account)
            .orElseThrow() { RuntimeException("Account not found") }

        if (accountVerified.endDate !== null) {
            throw RuntimeException("Account is inactive")
        }

        return accountVerified
    }
}