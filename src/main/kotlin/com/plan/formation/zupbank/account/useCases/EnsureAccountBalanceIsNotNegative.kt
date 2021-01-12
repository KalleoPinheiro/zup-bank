package com.plan.formation.zupbank.account.useCases

import com.plan.formation.zupbank.account.AccountModel
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.math.BigDecimal

@Service
class EnsureAccountBalanceIsNotNegative {
    fun handle(account: AccountModel) {
        if (account.balance!! < BigDecimal.ZERO) {
            throw RuntimeException("Account balance cannot be negative")
        }
    }
}