package com.plan.formation.zupbank.transaction.useCases

import com.plan.formation.zupbank.account.AccountModel
import com.plan.formation.zupbank.account.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.math.BigDecimal

@Service
class EnsureAccountBalance {
    @Autowired
    private lateinit var repository: AccountRepository

    fun handle(account: AccountModel, amount: BigDecimal) {
        if (account.balance!! < amount) {
            throw RuntimeException("Insufficient funds")
        }
    }
}